package cap.capServer.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class GetPostInfoDto {
    int id;
    String username;
    String mediaTitle;
    String postTitle;
    String postContent;
    String mediaURL;
    int numLikes;
    boolean hasLiked;
}
