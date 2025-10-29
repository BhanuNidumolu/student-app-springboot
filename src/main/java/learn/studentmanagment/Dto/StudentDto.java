package learn.studentmanagment.Dto;

import lombok.Data;

@Data
public class StudentDto {
    private Long id;
    private String firstName;
    private String lastName;
    private Integer age;
    private String email;
    private String username; // Show the associated username
}