package fr.waveme.payload.request;

import lombok.Data;

import java.util.List;

@Data
public class PostRequest {
    private String content;
    private List<String> images;
}
