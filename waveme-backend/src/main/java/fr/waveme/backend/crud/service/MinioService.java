package fr.waveme.backend.crud.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

public interface MinioService {

    /**
     * Upload a file to the specified bucket in MinIO.
     *
     * @param file       the file to upload
     * @param bucketName the bucket to store the file
     * @return the public URL of the uploaded file
     * @throws Exception if an error occurs during upload
     */
    String uploadImage(MultipartFile file, String bucketName) throws Exception;

    /**
     * Get an InputStream for a file in the specified bucket.
     *
     * @param bucketName the bucket containing the file
     * @param objectName the name of the file
     * @return an InputStream for the file
     * @throws Exception if an error occurs during retrieval
     */
    InputStream getImage(String bucketName, String objectName) throws Exception;

    /**
     * Create a bucket in MinIO if it does not exist.
     *
     * @param bucketName the name of the bucket to create
     * @throws Exception if an error occurs during bucket creation
     */
    void createBucket(String bucketName) throws Exception;

    /**
     * Get file url
     *
     * @param bucketName
     * @param objectName
     * @return
     * @throws Exception
     */
    String getFileUrl(String bucketName, String objectName) throws Exception;
}
