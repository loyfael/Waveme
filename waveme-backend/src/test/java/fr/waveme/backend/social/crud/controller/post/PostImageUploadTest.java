package fr.waveme.backend.social.crud.controller.post;

import fr.waveme.backend.security.jwt.JwtUtils;
import fr.waveme.backend.social.crud.controller.PostController;
import fr.waveme.backend.social.crud.repository.PostRepository;
import fr.waveme.backend.social.crud.sequence.SequenceGeneratorService;
import fr.waveme.backend.social.crud.service.MinioService;
import fr.waveme.backend.utils.RateLimiter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.Field;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PostImageUploadTest {
    private final MinioService minioService = mock(MinioService.class);
    private final PostRepository postRepository = mock(PostRepository.class);
    private final JwtUtils jwtUtils = mock(JwtUtils.class);
    private final SequenceGeneratorService sequence = mock(SequenceGeneratorService.class);

    private final PostController controller = new PostController(
            minioService, postRepository, null, jwtUtils,
            sequence, null, null, null
    );

    @Test
    void uploadPostImage_shouldWorkNormally() throws Exception {
        // given
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "a.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "test data".getBytes()
        );

        String bucket = "bucket";
        String description = "Un super mème";
        String authHeader = "Bearer fake.jwt.token";
        String ipAddress = "127.0.0.1";

        when(jwtUtils.getSocialUserIdFromJwtToken(authHeader)).thenReturn("user-id-123");
        when(sequence.generateSequence("posts")).thenReturn(123L);

        // Mock uploadImage pour retourner un ID plausible
        when(minioService.uploadImage(any(MultipartFile.class), eq(bucket), any()))
                .thenReturn("713008b1");

        // when
        ResponseEntity<String> response = controller.uploadPostImage(file, bucket, description, authHeader, ipAddress);

        // then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        String body = response.getBody();
        assertNotNull(body);
        assertFalse(body.isBlank(), "Le body est vide");

        // ✅ Vérifie que l’ID ressemble à un identifiant alphanumérique
        assertTrue(body.matches("^[a-zA-Z0-9\\-]{8,}$"), "Le body ne ressemble pas à un ID");
    }

    @Test
    void uploadPostImage_shouldRejectEmptyFile() {
        MultipartFile file = mock(MultipartFile.class);
        when(file.isEmpty()).thenReturn(true);

        ResponseEntity<String> response = controller.uploadPostImage(file, "bucket", "desc", "Bearer token", null);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("File is missing", response.getBody());
    }

    @SuppressWarnings("unchecked")
    @Test
    void uploadPostImage_shouldCatchException() throws Exception {

        Field mapField = RateLimiter.class.getDeclaredField("rateLimiterMap");
        mapField.setAccessible(true);
        Map<String, ?> map = (Map<String, ?>) mapField.get(null);
        map.clear();

        MultipartFile file = mock(MultipartFile.class);
        when(file.isEmpty()).thenReturn(false);
        when(file.getOriginalFilename()).thenReturn("err.jpg");

        when(jwtUtils.getSocialUserIdFromJwtToken(any())).thenThrow(new RuntimeException("Token error"));

        ResponseEntity<String> response = controller.uploadPostImage(
                file, "bucket", "desc", "Bearer token", null
        );

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().contains("Token error"));
    }
}
