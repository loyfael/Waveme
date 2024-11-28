package fr.waveme.backend.controller;

import fr.waveme.backend.crud.service.MinioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private static final Logger logger = LoggerFactory.getLogger(PostController.class);
    private final MinioService minioService;

    public PostController(MinioService minioService) {
        this.minioService = minioService;
    }

    /**
     * Endpoint pour uploader une image liée à un post.
     *
     * @param file       fichier à uploader
     * @param bucketName nom du bucket où stocker l'image
     * @return URL de l'image uploadée
     */
    @PostMapping("/upload-image")
    public ResponseEntity<String> uploadPostImage(
            @RequestParam("file") MultipartFile file,
            @RequestParam("bucket") String bucketName) {
        try {
            // Vérifiez si le fichier est reçu
            if (file == null || file.isEmpty()) {
                return ResponseEntity.badRequest().body("File is missing");
            }
            logger.info("Received file: {}, bucket: {}", file.getOriginalFilename(), bucketName);

            // Appelle le service pour uploader l'image
            String url = minioService.uploadImage(file, bucketName);
            logger.info("File uploaded successfully to MinIO. URL: {}", url);
            return ResponseEntity.ok(url);
        } catch (Exception e) {
            logger.error("Error during file upload: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }

    /**
     * Endpoint pour récupérer une URL pré-signée pour une image.
     *
     * @param bucketName nom du bucket contenant l'image
     * @param file nom de l'image
     * @return URL pré-signée de l'image
     */
    @GetMapping("/image-url")
    public ResponseEntity<String> getPostImageUrl(
            @RequestParam("bucket") String bucketName,
            @RequestParam("object") MultipartFile file) {
        try {
            String url = minioService.uploadImage(file, bucketName);
            return ResponseEntity.ok(url);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }
}
