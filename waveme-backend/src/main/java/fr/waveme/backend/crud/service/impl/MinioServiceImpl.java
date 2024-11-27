package fr.waveme.backend.crud.service.impl;

import fr.waveme.backend.crud.service.MinioService;
import io.minio.*;
import io.minio.http.Method;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@Service
public class MinioServiceImpl implements MinioService {
    private final MinioClient minioClient;

    @Autowired
    public MinioServiceImpl(MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    @Override
    public String uploadImage(MultipartFile file, String bucketName) throws Exception {
        // Ensure bucket exists
        if (!doesBucketExist(bucketName)) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
        }

        // Upload file
        String objectName = file.getOriginalFilename();
        minioClient.putObject(PutObjectArgs.builder()
                .bucket(bucketName)
                .object(objectName)
                .stream(file.getInputStream(), file.getSize(), -1)
                .contentType(file.getContentType())
                .build());

        // Generate URL for the uploaded file
        return getFileUrl(bucketName, objectName);
    }

    @Override
    public InputStream getImage(String bucketName, String objectName) throws Exception {
        return minioClient.getObject(GetObjectArgs.builder()
                .bucket(bucketName)
                .object(objectName)
                .build());
    }

    @Override
    public void createBucket(String bucketName) throws Exception {
        if (!doesBucketExist(bucketName)) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
        }
    }

    // Vérifie si un bucket existe
    private boolean doesBucketExist(String bucketName) throws Exception {
        return minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
    }

    // Génère une URL pour télécharger un fichier
    public String getFileUrl(String bucketName, String objectName) throws Exception {
        // Générer une URL pré-signée (valide pendant 7 jours)
        return minioClient.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs.builder()
                        .bucket(bucketName)
                        .object(objectName)
                        .method(Method.GET) // Méthode HTTP pour accéder à l'objet
                        .expiry(7 * 24 * 60 * 60) // Expiration de l'URL en secondes (7 jours ici)
                        .build()
        );
    }
}
