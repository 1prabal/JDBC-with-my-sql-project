package com.prabal.studentperformance;

import com.prabal.studentperformance.dao.StudentDAO;
import com.prabal.studentperformance.model.Student;
import com.prabal.studentperformance.service.StudentService;
import java.sql.SQLException;
import java.util.*;

public class Main {
    private static final Scanner in=new Scanner(System.in);
    private static final StudentService service=new StudentService(new StudentDAO());

    public static void main(String[] args) {
        System.out.println("=== STUDENT PERFORMANCE MANAGEMENT ===");
        while(true) {
            System.out.println("\n1 Add student\n2 Search by ID\n3 Search by email\n4 Search by course" +
                "\n5 Merit list\n6 Students sharing skills\n7 Programming improvement list" +
                "\n8 Display all students\n0 Exit");
            int choice=integer("Choice: ");
            try {
                switch(choice) {
                    case 1 -> addStudent();
                    case 2 -> { Student s=service.byId(integer("Student ID: ")); if(s==null) System.out.println("Not found."); else show(s); }
                    case 3 -> { System.out.print("Email: "); showList(service.byEmail(in.nextLine().trim())); }
                    case 4 -> { System.out.print("Course (full/partial): "); showList(service.byCourse(in.nextLine().trim())); }
                    case 5 -> { System.out.println("--- MERIT LIST ---"); showList(service.merit()); }
                    case 6 -> service.sameSkills().forEach((skill,students)-> {
                        System.out.println("\nSkill: "+skill);
                        students.forEach(s->System.out.println(s.getStudentId()+" - "+s.getStudentName()));
                    });
                    case 7 -> { System.out.println("--- PROGRAMMING < 50 AND MATH >= 75 ---"); showList(service.needsProgrammingHelp()); }
                    case 8 -> showList(service.all());
                    case 0 -> { System.out.println("Goodbye!"); return; }
                    default -> System.out.println("Invalid choice.");
                }
            } catch(SQLException e) {
                System.out.println("Database error: "+e.getMessage());
                if(e.getErrorCode()==1062) System.out.println("Duplicate student ID or email. Use unique values.");
            } catch(IllegalArgumentException e) { System.out.println("Validation: "+e.getMessage()); }
        }
    }
    private static void addStudent() throws SQLException {
        int id=integer("Student ID: ");
        System.out.print("Student name: "); String name=in.nextLine().trim();
        System.out.print("Email: "); String email=in.nextLine().trim();
        System.out.print("Course name: "); String course=in.nextLine().trim();
        double prog=decimal("Programming score (0-100): ");
        double math=decimal("Mathematics score (0-100): ");
        System.out.print("Technical skills (comma separated): ");
        List<String> skills=Arrays.stream(in.nextLine().split(",")).map(String::trim).filter(x->!x.isBlank()).distinct().toList();
        Student s=new Student(id,name,email,course,prog,math,skills);
        service.add(s); System.out.println("Student saved successfully."); show(s);
    }
    private static int integer(String prompt) {
        while(true) { System.out.print(prompt); try{return Integer.parseInt(in.nextLine().trim());}
            catch(NumberFormatException e){System.out.println("Enter a whole number.");} }
    }
    private static double decimal(String prompt) {
        while(true) { System.out.print(prompt); try {
            double x=Double.parseDouble(in.nextLine().trim());
            if(x>=0 && x<=100 && Double.isFinite(x)) return x;
            System.out.println("Score must be between 0 and 100.");
        } catch(NumberFormatException e){System.out.println("Enter a valid number.");} }
    }
    private static void showList(List<Student> list) {
        if(list.isEmpty()) {System.out.println("No matching students."); return;}
        list.forEach(Main::show);
    }
    private static void show(Student s) {
        System.out.println("\n------------------------------");
        System.out.println("ID: "+s.getStudentId()+" | Name: "+s.getStudentName());
        System.out.println("Email: "+s.getEmail()+" | Course: "+s.getCourseName());
        System.out.printf("Programming: %.2f | Mathematics: %.2f%n",s.getProgrammingScore(),s.getMathematicsScore());
        System.out.printf("Average: %.2f | Category: %s%n",s.getAverageScore(),s.getPerformanceCategory());
        System.out.println("Stronger subject: "+s.getStrongerSubject());
        System.out.println("Technical skills: "+String.join(", ",s.getTechnicalSkills()));
        System.out.println("------------------------------");
    }
}