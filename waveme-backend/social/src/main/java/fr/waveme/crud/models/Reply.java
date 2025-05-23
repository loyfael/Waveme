package fr.waveme.crud.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "reply")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Reply {
    @Id
    private Long id;

    private Long commentId;
    private String userId;

    private Integer upVote;
    private Integer downVote;

    private String description;

    private String createdAt;
    private String updatedAt;
}
