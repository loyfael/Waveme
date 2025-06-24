package fr.waveme.backend.social.crud.dto.pub.react;

import fr.waveme.backend.social.crud.dto.pub.UserSimpleInfoDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class PostVoteDetailsDto {
  int upVote;
  int downVote;
  List<UserSimpleInfoDto> upvoters;
  List<UserSimpleInfoDto> downvoters;
}
