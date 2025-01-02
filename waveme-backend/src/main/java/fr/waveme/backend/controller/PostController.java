package fr.waveme.backend.controller;

import fr.waveme.backend.crud.models.Post;
import fr.waveme.backend.crud.models.User;
import fr.waveme.backend.crud.repository.PostRepository;
import fr.waveme.backend.crud.repository.UserRepository;
import fr.waveme.backend.crud.service.MinioService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private static final Logger logger = LoggerFactory.getLogger(PostController.class);
    private final MinioService minioService;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public PostController(MinioService minioService, UserRepository userRepository, PostRepository postRepository) {
        this.minioService = minioService;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
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
                @RequestParam("bucket") String bucketName,
                @RequestParam("userId") Long userId,
                @RequestParam("description") String description)
            {

        try {
            // Vérifiez si le fichier est reçu
            if (file == null || file.isEmpty()) {
                return ResponseEntity.badRequest().body("File is missing");
            }
            logger.info("Received file: {}, bucket: {}", file.getOriginalFilename(), bucketName);

            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            Post post = new Post();
            post.setUser(user);
            post.setDescription(description);
            post.setUpVote(0);
            post.setDownVote(0);

            // Appelle le service pour uploader l'image
            String url = minioService.uploadImage(file, bucketName, post);
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
     * @param bucketName URL de l'image
     * @return URL pré-signée de l'image
     */
    @GetMapping("/image-url")
    public ResponseEntity<byte[]> downloadImage(
            @RequestParam("objectName") String objectName,
            @RequestParam("bucket") String bucketName) {
        try {
            // Vérifiez si un post avec ce nom de fichier existe
            Post post = postRepository.findByImageUrlContaining(objectName)
                    .orElseThrow(() -> new RuntimeException("Post with the specified object name not found"));

            // Télécharger l'image depuis MinIO
            InputStream inputStream = minioService.downloadImage(bucketName, objectName);
            byte[] imageBytes = inputStream.readAllBytes();

            // Renvoyer l'image
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + objectName + "\"")
                    .contentType(MediaType.IMAGE_JPEG) // Ajustez selon le type réel
                    .body(imageBytes);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null); // Gérez l'erreur
        }
    }
}
