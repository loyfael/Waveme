package fr.waveme.backend.social.crud.service.impl;

import fr.waveme.backend.social.crud.models.Post;
import fr.waveme.backend.social.crud.repository.PostRepository;
import fr.waveme.backend.social.crud.service.MinioService;
import fr.waveme.backend.social.storage.MinioProperties;
import io.minio.*;
import io.minio.errors.MinioException;
import io.minio.http.Method;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;


/**
 * MinioServiceImpl provides methods to upload and download images using MinIO.
 * It implements the MinioService interface and uses MinioClient for interactions with the MinIO server.
 */
@Service
public class MinioServiceImpl implements MinioService {
    private final MinioClient minioClient;
    private final PostRepository postRepository;
    private final MinioProperties minioProperties;

    public MinioServiceImpl(MinioClient minioClient, PostRepository postRepository, MinioProperties minioProperties) {
        this.minioClient = minioClient;
        this.postRepository = postRepository;
        this.minioProperties = minioProperties;
    }

    @Override
    public String uploadImage(MultipartFile file, String bucketName, Post post) {
        try {

            String fileName = file.getOriginalFilename();

            Optional<Post> existingPost = postRepository.findByImageUrl(fileName);

            if (existingPost.isPresent()) {
                throw new RuntimeException("A file with the same name already exists");
            }

            String objectName = System.currentTimeMillis() + "_" + file.getOriginalFilename();

            // Upload file to MinIO
            minioClient.putObject(
                 PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .stream(file.getInputStream(), file.getSize(), -1)
                    .contentType(file.getContentType())
                    .build()
            );

            String url = minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .expiry(60 * 60 * 64) // 24h
                            .method(Method.GET)
                            .build()
            );

            post.setImageUrl(url);
            postRepository.save(post);

            return url;
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload file: " + e.getMessage(), e);
        }
    }

    @Override
    public InputStream downloadImage(String bucketName, String objectName) {
        try {
            return minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .build()
            );
        } catch (MinioException | IOException | InvalidKeyException | NoSuchAlgorithmException e) {
            throw new RuntimeException("Error downloading image: " + e.getMessage(), e);
        }
    }

    @Override
    public String uploadRawImage(MultipartFile file, String bucketName, String objectName) {
        try {
            if (objectName == null || objectName.isBlank()) {
                objectName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            }

            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .stream(file.getInputStream(), file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build()
            );

            return minioProperties.getEndpoint() + "/" + bucketName + "/" + objectName;
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload profile image: " + e.getMessage(), e);
        }
    }
}
