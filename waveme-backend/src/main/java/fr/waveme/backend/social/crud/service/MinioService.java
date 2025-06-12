package fr.waveme.backend.social.crud.service;

import fr.waveme.backend.social.crud.models.Post;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

/**
 * MinioService provides methods to upload and download images using MinIO.
 * It defines the contract for interacting with MinIO for image storage.
 */
public interface MinioService {

    /**
     * Upload an image to MinIO.
     *
     * @param file       the file to upload
     * @param bucketName the bucket name
     * @return the name of the object stored
     */
    String uploadImage(MultipartFile file, String bucketName, Post post);

    /**
     * Download an image from MinIO.
     *
     * @param bucketName the bucket name
     * @param objectName the name of the object to fetch
     * @return the InputStream of the fetched object
     */
    InputStream downloadImage(String bucketName, String objectName);

    /**
     * Upload a raw image file to MinIO.
     *
     * @param file       the file to upload
     * @param bucketName the bucket name
     * @param objectName the name of the object to store
     */
    void uploadRawImage(MultipartFile file, String bucketName, String objectName);
}
