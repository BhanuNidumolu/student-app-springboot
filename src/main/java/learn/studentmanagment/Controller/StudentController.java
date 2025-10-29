package learn.studentmanagment.Controller;

import jakarta.validation.Valid;
import learn.studentmanagment.Dto.StudentDto;
import learn.studentmanagment.Dto.StudentRequestDto;
import learn.studentmanagment.Service.StudentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    // CREATE (ADMIN only)
    @PostMapping
    public ResponseEntity<StudentDto> createStudent(@Valid @RequestBody StudentRequestDto requestDto) {
        StudentDto createdStudent = studentService.createStudent(requestDto);
        return new ResponseEntity<>(createdStudent, HttpStatus.CREATED);
    }

    // READ ALL (ADMIN, TEACHER, STUDENT)
    @GetMapping
    public ResponseEntity<List<StudentDto>> getAllStudents() {
        List<StudentDto> students = studentService.getAllStudents();
        return ResponseEntity.ok(students);
    }

    // READ ONE (ADMIN, TEACHER, STUDENT)
    @GetMapping("/{id}")
    public ResponseEntity<StudentDto> getStudentById(@PathVariable Long id) {
        StudentDto student = studentService.getStudentById(id);
        return ResponseEntity.ok(student);
    }

    // UPDATE (ADMIN only)
    @PutMapping("/{id}")
    public ResponseEntity<StudentDto> updateStudent(@PathVariable Long id, @Valid @RequestBody StudentRequestDto requestDto) {
        StudentDto updatedStudent = studentService.updateStudent(id, requestDto);
        return ResponseEntity.ok(updatedStudent);
    }

    // DELETE (ADMIN only)
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.ok("Student deleted successfully");
    }
}