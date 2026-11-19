package com.prabal.studentperformance.model;

import java.util.*;

public class Student {
    private int studentId;
    private String studentName, email, courseName;
    private double programmingScore, mathematicsScore;
    private List<String> technicalSkills = new ArrayList<>();

    public Student() {}
    public Student(int id, String name, String email, String course, double programming,
                   double mathematics, List<String> skills) {
        setStudentId(id); setStudentName(name); setEmail(email); setCourseName(course);
        setProgrammingScore(programming); setMathematicsScore(mathematics); setTechnicalSkills(skills);
    }

    public double getAverageScore() { return (programmingScore + mathematicsScore) / 2.0; }
    public String getPerformanceCategory() {
        double avg = getAverageScore();
        if (avg >= 85) return "Excellent";
        if (avg >= 70) return "Good";
        if (avg >= 50) return "Satisfactory";
        return "Needs Improvement";
    }
    public String getStrongerSubject() {
        if (programmingScore > mathematicsScore) return "Programming";
        if (mathematicsScore > programmingScore) return "Mathematics";
        return "Both subjects are equal";
    }

    public int getStudentId() { return studentId; }
    public void setStudentId(int v) { if (v <= 0) throw new IllegalArgumentException("ID must be positive."); studentId = v; }
    public String getStudentName() { return studentName; }
    public void setStudentName(String v) { studentName = required(v, "Name"); }
    public String getEmail() { return email; }
    public void setEmail(String v) {
        v = required(v, "Email");
        if (!v.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) throw new IllegalArgumentException("Invalid email.");
        email = v;
    }
    public String getCourseName() { return courseName; }
    public void setCourseName(String v) { courseName = required(v, "Course"); }
    public double getProgrammingScore() { return programmingScore; }
    public void setProgrammingScore(double v) { score(v); programmingScore = v; }
    public double getMathematicsScore() { return mathematicsScore; }
    public void setMathematicsScore(double v) { score(v); mathematicsScore = v; }
    public List<String> getTechnicalSkills() { return Collections.unmodifiableList(technicalSkills); }
    public void setTechnicalSkills(List<String> v) {
        technicalSkills = new ArrayList<>();
        if (v != null) v.stream().filter(Objects::nonNull).map(String::trim)
            .filter(s -> !s.isBlank()).distinct().forEach(technicalSkills::add);
    }
    private static void score(double v) {
        if (!Double.isFinite(v) || v < 0 || v > 100) throw new IllegalArgumentException("Scores must be from 0 to 100.");
    }
    private static String required(String v, String field) {
        if (v == null || v.isBlank()) throw new IllegalArgumentException(field + " cannot be empty.");
        return v.trim();
    }
    @Override public String toString() {
        return studentId + " | " + studentName + " | Avg: " + String.format("%.2f", getAverageScore())
            + " | " + getPerformanceCategory();
    }
}