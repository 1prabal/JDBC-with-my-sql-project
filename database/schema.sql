CREATE DATABASE IF NOT EXISTS student_performance_db;
USE student_performance_db;
CREATE TABLE IF NOT EXISTS students (
 student_id INT PRIMARY KEY, student_name VARCHAR(100) NOT NULL,
 email VARCHAR(150) NOT NULL UNIQUE, course_name VARCHAR(100) NOT NULL,
 programming_score DECIMAL(5,2) NOT NULL, mathematics_score DECIMAL(5,2) NOT NULL,
 CHECK (programming_score BETWEEN 0 AND 100),
 CHECK (mathematics_score BETWEEN 0 AND 100)
);
CREATE TABLE IF NOT EXISTS student_skills (
 student_id INT NOT NULL, skill_name VARCHAR(80) NOT NULL,
 PRIMARY KEY(student_id, skill_name),
 FOREIGN KEY(student_id) REFERENCES students(student_id) ON DELETE CASCADE
);
INSERT IGNORE INTO students VALUES
(101,'Aarav Sharma','aarav@example.com','B.Tech AIML',92,88),
(102,'Riya Verma','riya@example.com','B.Tech CSE',45,82),
(103,'Kabir Singh','kabir@example.com','B.Tech AIML',74,68);
INSERT IGNORE INTO student_skills VALUES
(101,'Java'),(101,'Python'),(101,'SQL'),(102,'Python'),(102,'SQL'),(103,'Java'),(103,'Spring Boot');