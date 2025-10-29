package learn.studentmanagment.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "course_materials")
@Getter
@Setter
@NoArgsConstructor
public class CourseMaterial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // The original name of the file (e.g., "lecture-notes.pdf")
    @Column(name = "file_name")
    private String fileName;

    // The unique name stored on disk (e.g., "uuid-lecture-notes.pdf")
    @Column(name = "stored_file_name")
    private String storedFileName;

    @Column(name = "content_type")
    private String contentType;

    private Long size;

    @Column(name = "upload_timestamp")
    private LocalDateTime uploadTimestamp;

    // Many files can be uploaded by one teacher
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id", nullable = false)
    private Teacher uploadedBy;
}