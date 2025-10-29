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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class) // This initializes all the @Mock and @InjectMocks
class StudentServiceImplTest {

    // @Mock creates a fake, "mocked" version of these classes.
    @Mock
    private StudentRepository studentRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    // @InjectMocks creates a real instance of StudentServiceImpl
    // and injects all the @Mock objects into it.
    @InjectMocks
    private StudentServiceImpl studentService;

    private StudentRequestDto requestDto;
    private Role studentRole;
    private User testUser;
    private Student testStudent;

    @BeforeEach
    void setUp() {
        // --- ARRANGE ---
        // This setup runs before each test method.
        requestDto = new StudentRequestDto();
        requestDto.setFirstName("Jane");
        requestDto.setLastName("Doe");
        requestDto.setEmail("jane.doe@example.com");
        requestDto.setUsername("janedoe");
        requestDto.setPassword("password123");
        requestDto.setAge(21);

        studentRole = new Role("ROLE_STUDENT");
        studentRole.setId(1L);

        testUser = new User();
        testUser.setUsername("janedoe");
        testUser.setPassword("hashedpassword123");
        testUser.setRoles(Set.of(studentRole));

        testStudent = new Student();
        testStudent.setId(1L);
        testStudent.setFirstName("Jane");
        testStudent.setLastName("Doe");
        testStudent.setUser(testUser);
    }

    @Test
    @DisplayName("Should create student successfully when all data is valid")
    void createStudent_Success() {
        // --- ARRANGE ---
        // We tell our mocks what to do when they are called.

        // When userRepository.findByUsername is called with "janedoe", return empty.
        when(userRepository.findByUsername("janedoe")).thenReturn(Optional.empty());

        // When roleRepository.findByName is called, return our studentRole.
        when(roleRepository.findByName("ROLE_STUDENT")).thenReturn(Optional.of(studentRole));

        // When passwordEncoder.encode is called, return a fake hash.
        when(passwordEncoder.encode("password123")).thenReturn("hashedpassword123");

        // When studentRepository.save is called, return our testStudent.
        when(studentRepository.save(any(Student.class))).thenReturn(testStudent);

        // --- ACT ---
        // We call the real method we want to test.
        StudentDto resultDto = studentService.createStudent(requestDto);

        // --- ASSERT ---
        // We check if the results are what we expected.
        assertNotNull(resultDto);
        assertEquals("Jane", resultDto.getFirstName());
        assertEquals("janedoe", resultDto.getUsername());

        // We also *verify* that our mocks were called the correct number of times.
        verify(userRepository, times(1)).findByUsername("janedoe");
        verify(passwordEncoder, times(1)).encode("password123");
        verify(studentRepository, times(1)).save(any(Student.class));
    }

    @Test
    @DisplayName("Should throw RuntimeException when username is already taken")
    void createStudent_FailsWhenUsernameTaken() {
        // --- ARRANGE ---
        // This time, we tell the mock to return a *real* user.
        when(userRepository.findByUsername("janedoe")).thenReturn(Optional.of(testUser));

        // --- ACT & ASSERT ---
        // We assert that calling the method now *throws* the exception.
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            studentService.createStudent(requestDto);
        });

        assertEquals("Username is already taken", exception.getMessage());

        // Verify the code stopped before trying to save.
        verify(studentRepository, times(0)).save(any(Student.class));
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when student role is missing")
    void createStudent_FailsWhenRoleMissing() {
        // --- ARRANGE ---
        when(userRepository.findByUsername("janedoe")).thenReturn(Optional.empty());
        // This time, the role is missing
        when(roleRepository.findByName("ROLE_STUDENT")).thenReturn(Optional.empty());

        // --- ACT & ASSERT ---
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            studentService.createStudent(requestDto);
        });

        assertTrue(exception.getMessage().contains("ROLE_STUDENT not found"));

        // Verify the code stopped before trying to save.
        verify(studentRepository, times(0)).save(any(Student.class));
    }
}