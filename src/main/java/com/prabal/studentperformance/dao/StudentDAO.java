package com.prabal.studentperformance.dao;

import com.prabal.studentperformance.db.DBConnection;
import com.prabal.studentperformance.model.Student;
import java.sql.*;
import java.util.*;

public class StudentDAO {
    public void insert(Student s) throws SQLException {
        String sql = "INSERT INTO students VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection c = DBConnection.getConnection()) {
            c.setAutoCommit(false);
            try {
                try (PreparedStatement p = c.prepareStatement(sql)) {
                    p.setInt(1,s.getStudentId()); p.setString(2,s.getStudentName());
                    p.setString(3,s.getEmail()); p.setString(4,s.getCourseName());
                    p.setDouble(5,s.getProgrammingScore()); p.setDouble(6,s.getMathematicsScore());
                    p.executeUpdate();
                }
                try (PreparedStatement p = c.prepareStatement("INSERT INTO student_skills VALUES (?, ?)")) {
                    for (String skill : s.getTechnicalSkills()) {
                        p.setInt(1,s.getStudentId()); p.setString(2,skill); p.addBatch();
                    }
                    p.executeBatch();
                }
                c.commit();
            } catch (SQLException e) { c.rollback(); throw e; }
        }
    }
    public Student findById(int id) throws SQLException {
        List<Student> a = query("SELECT * FROM students WHERE student_id=?", p -> p.setInt(1,id));
        return a.isEmpty() ? null : a.get(0);
    }
    public List<Student> findByEmail(String email) throws SQLException {
        return query("SELECT * FROM students WHERE LOWER(email)=LOWER(?)", p -> p.setString(1,email));
    }
    public List<Student> findByCourse(String course) throws SQLException {
        return query("SELECT * FROM students WHERE course_name LIKE ? ORDER BY student_name", p -> p.setString(1,"%"+course+"%"));
    }
    public List<Student> all() throws SQLException {
        return query("SELECT * FROM students ORDER BY student_id", p -> {});
    }
    public List<Student> meritList() throws SQLException {
        return query("SELECT * FROM students ORDER BY (programming_score+mathematics_score)/2.0 DESC, student_name", p -> {});
    }
    public List<Student> programmingImprovement() throws SQLException {
        return query("SELECT * FROM students WHERE programming_score<50 AND mathematics_score>=75 ORDER BY mathematics_score DESC", p -> {});
    }
    public Map<String,List<Student>> sameSkills() throws SQLException {
        Map<String,List<Student>> map = new LinkedHashMap<>();
        String sql = "SELECT ss.skill_name,s.* FROM student_skills ss JOIN students s ON s.student_id=ss.student_id " +
          "WHERE ss.skill_name IN (SELECT skill_name FROM student_skills GROUP BY skill_name HAVING COUNT(DISTINCT student_id)>1) " +
          "ORDER BY ss.skill_name,s.student_name";
        try (Connection c=DBConnection.getConnection(); PreparedStatement p=c.prepareStatement(sql); ResultSet r=p.executeQuery()) {
            while(r.next()) map.computeIfAbsent(r.getString("skill_name"), k -> new ArrayList<>()).add(mapStudent(r));
        }
        for (List<Student> list : map.values()) for (Student s : list) s.setTechnicalSkills(loadSkills(s.getStudentId()));
        return map;
    }
    private List<Student> query(String sql, Binder binder) throws SQLException {
        List<Student> list=new ArrayList<>();
        try(Connection c=DBConnection.getConnection(); PreparedStatement p=c.prepareStatement(sql)) {
            binder.bind(p);
            try(ResultSet r=p.executeQuery()) {
                while(r.next()) { Student s=mapStudent(r); s.setTechnicalSkills(loadSkills(s.getStudentId())); list.add(s); }
            }
        }
        return list;
    }
    private List<String> loadSkills(int id) throws SQLException {
        List<String> a=new ArrayList<>();
        try(Connection c=DBConnection.getConnection(); PreparedStatement p=c.prepareStatement(
            "SELECT skill_name FROM student_skills WHERE student_id=? ORDER BY skill_name")) {
            p.setInt(1,id); try(ResultSet r=p.executeQuery()) { while(r.next()) a.add(r.getString(1)); }
        }
        return a;
    }
    private Student mapStudent(ResultSet r) throws SQLException {
        return new Student(r.getInt("student_id"),r.getString("student_name"),r.getString("email"),
            r.getString("course_name"),r.getDouble("programming_score"),r.getDouble("mathematics_score"),List.of());
    }
    @FunctionalInterface private interface Binder { void bind(PreparedStatement p) throws SQLException; }
}