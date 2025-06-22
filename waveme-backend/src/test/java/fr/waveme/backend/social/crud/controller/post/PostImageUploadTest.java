package fr.waveme.backend.social.crud.controller.post;

import fr.waveme.backend.security.jwt.JwtUtils;
import fr.waveme.backend.social.crud.controller.PostController;
import fr.waveme.backend.social.crud.repository.PostRepository;
import fr.waveme.backend.social.crud.sequence.SequenceGeneratorService;
import fr.waveme.backend.social.crud.service.MinioService;
import fr.waveme.backend.social.crud.service.PostService;
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

    private final PostService postService = mock(PostService.class);
    private final PostController controller = new PostController(postService);

    @Test
    void uploadPostImage_shouldSucceed() {
        MockMultipartFile file = new MockMultipartFile("file", "meme.jpg", MediaType.IMAGE_JPEG_VALUE, "content".getBytes());

        when(postService.uploadPostImage(eq(file), eq("bucket"), eq("desc"), eq("Bearer token")))
                .thenReturn(ResponseEntity.ok("short-url"));

        ResponseEntity<String> res = controller.uploadPostImage(file, "bucket", "desc", "Bearer token", "127.0.0.1");

        assertEquals(HttpStatus.OK, res.getStatusCode());
        assertEquals("short-url", res.getBody());
    }

    @Test
    void uploadPostImage_shouldRejectEmptyFile() {
        MultipartFile file = mock(MultipartFile.class);
        when(file.isEmpty()).thenReturn(true);

        ResponseEntity<String> res = controller.uploadPostImage(file, "bucket", "desc", "Bearer token", "127.0.0.1");

        assertEquals(HttpStatus.BAD_REQUEST, res.getStatusCode());
        assertEquals("File is missing", res.getBody());
    }
}
