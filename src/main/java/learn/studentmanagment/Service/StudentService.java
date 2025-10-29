package learn.studentmanagment.Service;

import learn.studentmanagment.Dto.StudentDto;
import learn.studentmanagment.Dto.StudentRequestDto;
import java.util.List;

public interface StudentService {
    StudentDto createStudent(StudentRequestDto requestDto);
    List<StudentDto> getAllStudents();
    StudentDto getStudentById(Long id);
    StudentDto updateStudent(Long id, StudentRequestDto requestDto);
    void deleteStudent(Long id);

}