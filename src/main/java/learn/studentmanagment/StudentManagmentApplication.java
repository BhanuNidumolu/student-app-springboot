package learn.studentmanagment;

import learn.studentmanagment.Entity.Student;
import learn.studentmanagment.Repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class StudentManagmentApplication  {

    public static void main(String[] args) {
        SpringApplication.run(StudentManagmentApplication.class, args);
    }
//@Autowired
//private StudentRepository studentRepository;
//    @Override
//    public void run(String... args) throws Exception {
//        Student newStudent = new Student().builder()
//                .age("22")
//                .firstName("John")
//                .lastName("Doe")
//                .email("sbhdbakd@gmail.com").build();
//        studentRepository.save(newStudent);
//        Student newStudent2 = new Student().builder()
//                .age("21").firstName("bhanu").lastName("Nidumolu")
//                .email("bhanunidumol@gmail.com").build();
//        studentRepository.save(newStudent2);
    }
