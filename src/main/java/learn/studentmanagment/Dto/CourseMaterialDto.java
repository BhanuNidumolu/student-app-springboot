package learn.studentmanagment.Dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CourseMaterialDto {
    private Long id;
    private String fileName;
    private String contentType;
    private Long sizeInBytes;
    private String uploadedByTeacher; // We'll map "teacher.firstName + lastName"
    private LocalDateTime uploadTimestamp;
}