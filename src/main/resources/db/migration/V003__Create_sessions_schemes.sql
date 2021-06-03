# Создание схем сеансов (сессий)


USE java_exam;


CREATE TABLE session_schema (
    id bigint(45) not null auto_increment,
    name VARCHAR(100) null,
    PRIMARY KEY (id)
) ENGINE = InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE session_schema_unit (
    id bigint(45) not null auto_increment,
    session_schema bigint(45) not null,
    field_of_knowledge bigint(45) not null,
    topics VARCHAR(1000) not null,
    quantity_questions VARCHAR(1000) not null,
    PRIMARY KEY (id),
    FOREIGN KEY (session_schema) references session_schema(id),
    FOREIGN KEY (field_of_knowledge) references field_of_knowledge(id)
) ENGINE = InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


ALTER TABLE user
ADD COLUMN session_schema bigint(45) null AFTER enabled,
ADD FOREIGN KEY (session_schema) references session_schema(id);