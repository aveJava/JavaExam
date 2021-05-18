# Создание базы данных java_exam (базовая структура)


CREATE SCHEMA IF NOT EXISTS java_exam
CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;
USE java_exam;


CREATE TABLE exam_session (
    id bigint(45) not null auto_increment,
    date date not null,
    user_id bigint(45) not null,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE exam_question (
    id bigint(45) not null auto_increment,
    topic_id bigint(45) not null,
    question text(2000) not null,
    answers text(2000) not null,
    correct_answer_number tinyint(2) not null,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE user_answer (
    id bigint(45) not null auto_increment,
    session_id bigint(45) not null,
    question_id bigint(45) not null,
    selected_answer_number tinyint(2) not null,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE user (
    id bigint(45) not null auto_increment,
    username varchar(35),
    password varchar(255),
    enabled bit not null,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE role (
    role varchar(70) not null,
    PRIMARY KEY (role)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE user_roles (
    user_id bigint(45) not null,
    authority varchar(70) not null,
    PRIMARY KEY (user_id, authority)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE field_of_knowledge (
    id bigint(45) not null auto_increment,
    name varchar(100) not null,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE exam_question_topic (
    id bigint(45) not null auto_increment,
    field_of_knowledge_id bigint(45) not null,
    name varchar(100) not null,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
