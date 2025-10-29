package learn.studentmanagment.Dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class StudentRequestDto {

    @NotEmpty
    private String firstName;

    @NotEmpty
    private String lastName;

    @NotNull
    private Integer age;

    @NotEmpty
    @Email
    private String email;

    // We also need login info when creating a student
    @NotEmpty
    @Size(min = 4)
    private String username;

    @NotEmpty
    @Size(min = 8)
    private String password;
}