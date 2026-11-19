# Student Performance Management System

Java 21 + Maven + JDBC + MySQL console project for IntelliJ IDEA.

## Setup
1. Install JDK 21 and MySQL Server 8.
2. Open MySQL Workbench and execute `database/schema.sql`. It creates database `student_performance_db`, tables `students` and `student_skills`, plus sample records (IDs 101-103).
3. Open `src/main/java/com/prabal/studentperformance/db/DBConnection.java` and replace `YOUR_MYSQL_PASSWORD` with your MySQL root password. Change USER if required.
4. Open this repository folder in IntelliJ IDEA, set Project SDK to JDK 21, and allow Maven to import dependencies.
5. Run `src/main/java/com/prabal/studentperformance/Main.java`.

## Features
- Add students with validation and duplicate ID/email protection
- Search by ID, email or course
- Calculate average, performance category and stronger subject
- Generate merit list sorted by average descending
- Group students who share technical skills
- Find students with Programming < 50 and Mathematics >= 75

## Structure
- `model/Student.java`: encapsulated student model and calculations
- `dao/StudentDAO.java`: JDBC database operations and queries
- `service/StudentService.java`: service layer
- `db/DBConnection.java`: MySQL connection settings
- `database/schema.sql`: database and table creation script

Average = (Programming + Mathematics) / 2. Categories: >=85 Excellent, >=70 Good, >=50 Satisfactory, otherwise Needs Improvement. Equal scores are reported as a tie.

The project uses a normalized `student_skills` table to store multiple skills per student. Use IDs other than 101, 102 and 103 unless you remove the sample rows.