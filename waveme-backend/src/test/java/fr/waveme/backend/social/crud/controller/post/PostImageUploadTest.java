package fr.waveme.backend.social.crud.controller.post;

import fr.waveme.backend.social.crud.controller.PostController;
import fr.waveme.backend.social.crud.service.PostService;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class PostImageUploadTest {

    PostService postService = mock(PostService.class);
    PostController controller = new PostController(postService);

    @Test
    void uploadPostImage_shouldSucceed() {
        MultipartFile file = new MockMultipartFile("file", "image.jpg", MediaType.IMAGE_JPEG_VALUE, "image-data".getBytes());

        assertDoesNotThrow(() -> {
            controller.uploadPostImage(file, "bucket", "Une image", "token", "127.0.0.1");
        });

        verify(postService).uploadPostImage(file, "bucket", "Une image", "token");
    }

    @Test
    void uploadPostImage_shouldRejectEmptyFile() {
        MultipartFile file = mock(MultipartFile.class);
        when(file.isEmpty()).thenReturn(true);

        var res = controller.uploadPostImage(file, "bucket", "Une image", "token", "127.0.0.1");

        assertNotNull(res);
        assertEquals(400, res.getStatusCode().value());
        assertEquals("File is missing", res.getBody());

        verify(postService, never()).uploadPostImage(any(), any(), any(), any());
    }
}
