package learn.studentmanagment.Service.IMPL;

import learn.studentmanagment.Entity.Student;
import learn.studentmanagment.Repository.StudentRepository;
import learn.studentmanagment.Service.StudentService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;

    @Override
    public List<Student> getAllStudent() {
        return studentRepository.findAll();
    }

    @Override
    public Student saveStudent(Student student) {
        return studentRepository.save(student);
    }

    @Override
    public Student getStudentById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + id));
    }

    @Override
    public Student updateStudent(Long id, Student student) {
        Student existing = getStudentById(id);
        existing.setFirstName(student.getFirstName());
        existing.setLastName(student.getLastName());
        existing.setAge(student.getAge());
        existing.setEmail(student.getEmail());
        return studentRepository.save(existing);
    }

    @Override
    public void deleteStudentById(Long id) {
        studentRepository.deleteById(id);
    }
}
