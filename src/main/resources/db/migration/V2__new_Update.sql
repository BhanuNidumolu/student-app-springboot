use studentm;

CREATE TABLE students (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          first_name VARCHAR(255) NOT NULL,
                          last_name VARCHAR(255) NOT NULL,
                          age INT,
                          email VARCHAR(255) NOT NULL UNIQUE
);
