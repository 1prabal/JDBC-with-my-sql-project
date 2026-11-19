package com.prabal.studentperformance.service;
import com.prabal.studentperformance.dao.StudentDAO;
import com.prabal.studentperformance.model.Student;
import java.sql.SQLException;
import java.util.*;
public class StudentService {
    private final StudentDAO dao;
    public StudentService(StudentDAO dao) { this.dao=dao; }
    public void add(Student s) throws SQLException { if(s==null) throw new IllegalArgumentException("Student is required."); dao.insert(s); }
    public Student byId(int id) throws SQLException { return dao.findById(id); }
    public List<Student> byEmail(String e) throws SQLException { return dao.findByEmail(e); }
    public List<Student> byCourse(String c) throws SQLException { return dao.findByCourse(c); }
    public List<Student> all() throws SQLException { return dao.all(); }
    public List<Student> merit() throws SQLException { return dao.meritList(); }
    public Map<String,List<Student>> sameSkills() throws SQLException { return dao.sameSkills(); }
    public List<Student> needsProgrammingHelp() throws SQLException { return dao.programmingImprovement(); }
}