package learn.studentmanagment.Controller;

import io.swagger.v3.oas.annotations.Operation;
import learn.studentmanagment.Dto.CourseMaterialDto;
import learn.studentmanagment.Entity.CourseMaterial;
import learn.studentmanagment.Entity.Teacher;
import learn.studentmanagment.Entity.User;
import learn.studentmanagment.Repository.UserRepository;
import learn.studentmanagment.Service.FileStorageService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

//import static jdk.javadoc.internal.doclets.formats.html.markup.HtmlStyle.parameters;

@RestController
@RequestMapping("/api/materials")
public class CourseMaterialController {

    private final FileStorageService fileStorageService;
    private final UserRepository userRepository;

    public CourseMaterialController(FileStorageService fileStorageService,
                                    UserRepository userRepository) {
        this.fileStorageService = fileStorageService;
        this.userRepository = userRepository;
    }

    /**
     * UPLOAD Endpoint (Teacher Only)
     */
//    @Operation(description = "upload the files",parameters = {"file"})
    @PostMapping(path = "/upload",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CourseMaterialDto> uploadFile(@RequestParam("file") MultipartFile file,
                                                        Principal principal) {
        // Get the authenticated User, then get their Teacher profile
        User user = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Teacher teacher = user.getTeacher();
        if (teacher == null) {
            return ResponseEntity.status(403).body(null); // Or throw exception
        }

        CourseMaterial material = fileStorageService.storeFile(file, teacher);

        // Return a DTO with file details
        CourseMaterialDto dto = new CourseMaterialDto();
        dto.setId(material.getId());
        dto.setFileName(material.getFileName());
        dto.setContentType(material.getContentType());
        dto.setSizeInBytes(material.getSize());
        dto.setUploadedByTeacher(teacher.getFirstName() + " " + teacher.getLastName());
        dto.setUploadTimestamp(material.getUploadTimestamp());

        return ResponseEntity.ok(dto);
    }

    /**
     * LIST Endpoint (Student & Teacher)
     */
    @GetMapping
    public ResponseEntity<List<CourseMaterialDto>> listFiles() {
        List<CourseMaterialDto> files = fileStorageService.listAllFiles();
        return ResponseEntity.ok(files);
    }

    /**
     * DOWNLOAD Endpoint (Student & Teacher)
     */
    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long id) {
        // Load file metadata and resource
        CourseMaterial material = fileStorageService.getMaterial(id);
        Resource resource = fileStorageService.loadFileAsResource(id);

        // Build the download response
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(material.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + material.getFileName() + "\"")
                .body(resource);
    }
}