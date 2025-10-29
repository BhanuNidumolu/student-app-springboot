package learn.studentmanagment.Service;

import learn.studentmanagment.Dto.CourseMaterialDto;
import learn.studentmanagment.Entity.CourseMaterial;
import learn.studentmanagment.Entity.Teacher;
import learn.studentmanagment.Repository.CourseMaterialRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class FileStorageService {

    private final Path fileStorageLocation;
    private final CourseMaterialRepository materialRepository;

    public FileStorageService(@Value("${file.upload-dir}") String uploadDir,
                              CourseMaterialRepository materialRepository) {
        this.materialRepository = materialRepository;
        this.fileStorageLocation = Paths.get(uploadDir).toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new RuntimeException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    @Transactional
    public CourseMaterial storeFile(MultipartFile file, Teacher teacher) {
        // 1. Sanitize and create a unique filename
        String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());
        String storedFileName = UUID.randomUUID().toString() + "_" + originalFileName;

        try {
            // Check for invalid characters
            if (originalFileName.contains("..")) {
                throw new RuntimeException("Filename contains invalid path sequence " + originalFileName);
            }

            // 2. Save the file to the filesystem
            Path targetLocation = this.fileStorageLocation.resolve(storedFileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            // 3. Create metadata and save to database
            CourseMaterial material = new CourseMaterial();
            material.setFileName(originalFileName);
            material.setStoredFileName(storedFileName);
            material.setContentType(file.getContentType());
            material.setSize(file.getSize());
            material.setUploadedBy(teacher);
            material.setUploadTimestamp(LocalDateTime.now());

            return materialRepository.save(material);

        } catch (IOException ex) {
            throw new RuntimeException("Could not store file " + originalFileName, ex);
        }
    }

    @Transactional(readOnly = true)
    public Resource loadFileAsResource(Long fileId) {
        try {
            CourseMaterial material = getMaterial(fileId);
            Path filePath = this.fileStorageLocation.resolve(material.getStoredFileName()).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists()) {
                return resource;
            } else {
                throw new RuntimeException("File not found: " + material.getFileName());
            }
        } catch (MalformedURLException ex) {
            throw new RuntimeException("File not found", ex);
        }
    }

    @Transactional(readOnly = true)
    public CourseMaterial getMaterial(Long fileId) {
         return materialRepository.findById(fileId)
                .orElseThrow(() -> new RuntimeException("File metadata not found for id: " + fileId));
    }

    @Transactional(readOnly = true)
    public List<CourseMaterialDto> listAllFiles() {
        return materialRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private CourseMaterialDto mapToDto(CourseMaterial material) {
        CourseMaterialDto dto = new CourseMaterialDto();
        dto.setId(material.getId());
        dto.setFileName(material.getFileName());
        dto.setContentType(material.getContentType());
        dto.setSizeInBytes(material.getSize());
        dto.setUploadTimestamp(material.getUploadTimestamp());

        // Avoid N+1 query if teacher is lazy loaded
        Teacher teacher = material.getUploadedBy();
        dto.setUploadedByTeacher(teacher.getFirstName() + " " + teacher.getLastName());

        return dto;
    }
}