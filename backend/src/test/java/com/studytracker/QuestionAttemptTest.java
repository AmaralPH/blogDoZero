package com.studytracker;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.studytracker.config.JwtService;
import com.studytracker.questions.AttemptRepository;
import com.studytracker.questions.Question;
import com.studytracker.questions.QuestionRepository;
import com.studytracker.user.User;
import com.studytracker.user.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("dev")
class QuestionAttemptTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AttemptRepository attemptRepository;

    private String token;
    private Question question;

    @BeforeEach
    void setUp() {
        attemptRepository.deleteAll();
        questionRepository.deleteAll();
        userRepository.deleteAll();
        User user = new User();
        user.setUsername("question-user");
        user.setPassword(passwordEncoder.encode("password"));
        user.setDisplayName("Question User");
        user = userRepository.save(user);
        token = jwtService.generateToken(user);

        Question q = new Question();
        q.setOwner(user);
        q.setPrompt("What is 2+2?");
        q.setAnswer("4");
        question = questionRepository.save(q);
    }

    @AfterEach
    void cleanUp() {
        attemptRepository.deleteAll();
        questionRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void createAttemptShouldPersist() throws Exception {
        AttemptPayload payload = new AttemptPayload(true, "Easy question");
        MvcResult result = mockMvc.perform(post("/questions/" + question.getId() + "/attempts")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isOk())
                .andReturn();

        JsonNode node = objectMapper.readTree(result.getResponse().getContentAsString());
        assertThat(node.get("correct").asBoolean()).isTrue();
        assertThat(attemptRepository.count()).isEqualTo(1);
    }

    private record AttemptPayload(Boolean correct, String notes) {}
}
