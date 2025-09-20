package com.studytracker;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.studytracker.config.JwtService;
import com.studytracker.sessions.StudySessionRepository;
import com.studytracker.user.User;
import com.studytracker.user.UserRepository;
import java.time.LocalDateTime;
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
class StudySessionControllerTest {

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
    private StudySessionRepository studySessionRepository;

    private String token;

    @BeforeEach
    void setUp() {
        studySessionRepository.deleteAll();
        userRepository.deleteAll();
        User user = new User();
        user.setUsername("session-user");
        user.setPassword(passwordEncoder.encode("password"));
        user.setDisplayName("Session User");
        user = userRepository.save(user);
        token = jwtService.generateToken(user);
    }

    @AfterEach
    void clean() {
        studySessionRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void createSessionAndFetchToday() throws Exception {
        LocalDateTime end = LocalDateTime.now().withSecond(0).withNano(0);
        LocalDateTime start = end.minusMinutes(30);
        SessionPayload payload = new SessionPayload(start, end, 30L, false);
        mockMvc.perform(post("/sessions")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isOk());

        MvcResult todayResult = mockMvc.perform(get("/sessions/today")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andReturn();

        JsonNode array = objectMapper.readTree(todayResult.getResponse().getContentAsString());
        assertThat(array).isNotEmpty();
        assertThat(array.get(0).get("durationMinutes").asLong()).isEqualTo(30);
    }

    private record SessionPayload(
            LocalDateTime startedAt, LocalDateTime endedAt, Long durationMinutes, Boolean manualEntry) {}
}
