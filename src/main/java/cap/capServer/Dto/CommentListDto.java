package cap.capServer.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CommentListDto {
    int id;
    String username;
    String content;
    int numLikes;
    boolean hasLiked;
}
