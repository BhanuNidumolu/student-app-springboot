package learn.studentmanagment.Service.IMPL;

import learn.studentmanagment.Dto.StudentDto;
import learn.studentmanagment.Dto.StudentRequestDto;
import learn.studentmanagment.Entity.Role;
import learn.studentmanagment.Entity.Student;
import learn.studentmanagment.Entity.User;
import learn.studentmanagment.Exception.ResourceNotFoundException;
import learn.studentmanagment.Repository.RoleRepository;
import learn.studentmanagment.Repository.StudentRepository;
import learn.studentmanagment.Repository.UserRepository;
import learn.studentmanagment.Service.StudentService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public StudentServiceImpl(StudentRepository studentRepository,
                              UserRepository userRepository,
                              RoleRepository roleRepository,
                              PasswordEncoder passwordEncoder) {
        this.studentRepository = studentRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public StudentDto createStudent(StudentRequestDto requestDto) {
        if (userRepository.findByUsername(requestDto.getUsername()).isPresent()) {
            throw new ResourceNotFoundException("Username is already taken");
        }

        // 1. Find the student role
       Role studentRole = roleRepository.findByName("ROLE_STUDENT")
        .orElseThrow(() -> new ResourceNotFoundException("ROLE_STUDENT not found. Please seed the database."));

        // 2. Create and save the User
        User user = new User();
        user.setUsername(requestDto.getUsername());
        user.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        user.setRoles(Set.of(studentRole));
        user.setEnabled(true);

        // 3. Create and save the Student
        Student student = new Student();
        student.setFirstName(requestDto.getFirstName());
        student.setLastName(requestDto.getLastName());
        student.setEmail(requestDto.getEmail());
        student.setAge(requestDto.getAge());

        // 4. Link them together
        student.setUser(user);
        user.setStudent(student);

        // 5. Save the student (user will be saved by cascade)
        Student savedStudent = studentRepository.save(student);

        return mapToDto(savedStudent);
    }

    @Override
    public List<StudentDto> getAllStudents() {
        return studentRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public StudentDto getStudentById(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));
        return mapToDto(student);
    }

    @Override
    @Transactional
    public StudentDto updateStudent(Long id, StudentRequestDto requestDto) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));

        // Update student fields
        student.setFirstName(requestDto.getFirstName());
        student.setLastName(requestDto.getLastName());
        student.setEmail(requestDto.getEmail());
        student.setAge(requestDto.getAge());

        // Update associated user
        User user = student.getUser();
        user.setUsername(requestDto.getUsername());
        // Only update password if a new one is provided (simple check)
        if (requestDto.getPassword() != null && !requestDto.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        }

        Student updatedStudent = studentRepository.save(student);
        return mapToDto(updatedStudent);
    }

    @Override
    public void deleteStudent(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));
        studentRepository.delete(student);
    }


    // --- Helper Method ---
    private StudentDto mapToDto(Student student) {
        StudentDto dto = new StudentDto();
        dto.setId(student.getId());
        dto.setFirstName(student.getFirstName());
        dto.setLastName(student.getLastName());
        dto.setEmail(student.getEmail());
        dto.setAge(student.getAge());
        if (student.getUser() != null) {
            dto.setUsername(student.getUser().getUsername());
        }
        return dto;
    }
}