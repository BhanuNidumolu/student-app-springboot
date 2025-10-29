package learn.studentmanagment;

import learn.studentmanagment.Entity.Role;
import learn.studentmanagment.Entity.Teacher;
import learn.studentmanagment.Entity.User;
import learn.studentmanagment.Repository.RoleRepository;
import learn.studentmanagment.Repository.TeacherRepository;
import learn.studentmanagment.Repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

@SpringBootApplication
public class StudentManagmentApplication {

    public static void main(String[] args) {
        SpringApplication.run(StudentManagmentApplication.class, args);
    }

    /**
     * This bean runs on startup. It's used to "seed" the database
     * with default roles and users so you can test the API.
     */
    @Bean
    @Transactional
    CommandLineRunner run(RoleRepository roleRepository, UserRepository userRepository, TeacherRepository teacherRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            // Create Roles if they don't exist
            Role adminRole = getOrCreateRole(roleRepository, "ROLE_ADMIN");
            Role teacherRole = getOrCreateRole(roleRepository, "ROLE_TEACHER");
            Role studentRole = getOrCreateRole(roleRepository, "ROLE_STUDENT");

            // Create an Admin user
            if (userRepository.findByUsername("admin").isEmpty()) {
                User adminUser = new User();
                adminUser.setUsername("admin");
                adminUser.setPassword(passwordEncoder.encode("admin123"));
                adminUser.setRoles(Set.of(adminRole));
                adminUser.setEnabled(true);
                userRepository.save(adminUser);
            }

            // Create a Teacher user
            if (userRepository.findByUsername("teacher").isEmpty()) {
                // 1. Create User
                User teacherUser = new User();
                teacherUser.setUsername("teacher");
                teacherUser.setPassword(passwordEncoder.encode("teacher123"));
                teacherUser.setRoles(Set.of(teacherRole));
                teacherUser.setEnabled(true);

                // 2. Create Teacher Profile
                Teacher teacherProfile = new Teacher();
                teacherProfile.setFirstName("Sarah");
                teacherProfile.setLastName("Connor");
                teacherProfile.setEmail("s.connor@school.edu");

                // 3. Link them
                teacherProfile.setUser(teacherUser);
                teacherUser.setTeacher(teacherProfile);

                // 4. Save the user (profile will cascade)
                userRepository.save(teacherUser);
            }

            // Create a Student user
            if (userRepository.findByUsername("student").isEmpty()) {
                User studentUser = new User();
                studentUser.setUsername("student");
                studentUser.setPassword(passwordEncoder.encode("student123"));
                studentUser.setRoles(Set.of(studentRole));
                studentUser.setEnabled(true);
                userRepository.save(studentUser);
            }
        };
    }

    private Role getOrCreateRole(RoleRepository roleRepository, String roleName) {
        Optional<Role> role = roleRepository.findByName(roleName);
        if (role.isPresent()) {
            return role.get();
        } else {
            return roleRepository.save(new Role(roleName));
        }
    }
}