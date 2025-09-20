CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    display_name VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL
);

CREATE TABLE subjects (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    user_id BIGINT NOT NULL REFERENCES users(id)
);

CREATE TABLE topics (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    subject_id BIGINT NOT NULL REFERENCES subjects(id) ON DELETE CASCADE,
    user_id BIGINT NOT NULL REFERENCES users(id)
);

CREATE TABLE study_sessions (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id),
    subject_id BIGINT REFERENCES subjects(id),
    topic_id BIGINT REFERENCES topics(id),
    started_at TIMESTAMP NOT NULL,
    ended_at TIMESTAMP,
    duration_minutes BIGINT NOT NULL,
    manual_entry BOOLEAN NOT NULL
);

CREATE TABLE questions (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id),
    subject_id BIGINT REFERENCES subjects(id),
    topic_id BIGINT REFERENCES topics(id),
    prompt TEXT NOT NULL,
    answer TEXT,
    created_at TIMESTAMPTZ NOT NULL
);

CREATE TABLE question_attempts (
    id BIGSERIAL PRIMARY KEY,
    question_id BIGINT NOT NULL REFERENCES questions(id) ON DELETE CASCADE,
    user_id BIGINT NOT NULL REFERENCES users(id),
    correct BOOLEAN NOT NULL,
    notes TEXT,
    attempted_at TIMESTAMPTZ NOT NULL
);

CREATE TABLE daily_goals (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id),
    goal_date DATE NOT NULL,
    target_minutes INTEGER NOT NULL,
    CONSTRAINT uq_daily_goal UNIQUE (user_id, goal_date)
);
