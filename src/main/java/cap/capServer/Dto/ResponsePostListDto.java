package cap.capServer.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ResponsePostListDto {
    int id;
    String username;
    String mediaTitle;
    String mediaType;
    String coverImageUrl;
    String postTitle;
    int numLikes;
    boolean hasLiked;
    int numComments;
}
