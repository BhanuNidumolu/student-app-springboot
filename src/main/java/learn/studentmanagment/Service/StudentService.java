package learn.studentmanagment.Service;

import learn.studentmanagment.Entity.Student;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface StudentService {
    List<Student> getAllStudent();
    Student saveStudent(Student student);
    Student getStudentById(Long id);
    Student updateStudent(Long id, Student student);
    void deleteStudentById(Long id);
}
