package cap.capServer.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ResponsePostDto {
    int id;
    String username;
    String mediaTitle;
    String postTitle;
    String mediaType;
    String coverImageUrl;
    String postContent;
    String mediaUrl;
    String mediaUrl2;
    int numLikes;
    boolean hasLiked;
    List<CommentListDto> commentList;

    public ResponsePostDto(GetPostInfoDto infoDto) {
        id = infoDto.getId();
        username = infoDto.getUsername();
        mediaTitle = infoDto.getMediaTitle();
        postTitle = infoDto.getPostTitle();
        postContent = infoDto.getPostContent();
        mediaUrl = infoDto.getMediaURL();
        mediaUrl2 = infoDto.getMediaURL2();
        coverImageUrl = infoDto.getCoverImageUrl();
        numLikes = infoDto.getNumLikes();
        hasLiked = false;
        commentList = null;
    }
}
