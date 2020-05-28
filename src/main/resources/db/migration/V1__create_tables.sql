CREATE TABLE IF NOT EXISTS topic (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(40) NOT NULL UNIQUE,
    correct INT NOT NULL,
    wrong INT NOT NULL,
    questions_count INT NOT NULL,
    total_time INT NOT NULL
);

CREATE TABLE IF NOT EXISTS question (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE ,
    answer VARCHAR(255) NOT NULL,
    correct INT NOT NULL,
    wrong INT NOT NULL,
    total_time BIGINT NOT NULL,
    topic_id INT NOT NULL,
    FOREIGN KEY (topic_id) REFERENCES topic(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS question_instance (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    start_time BIGINT NOT NULL,
    question_id INT NOT NULL,
    FOREIGN KEY (question_id) REFERENCES question (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS user (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(20) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL
);