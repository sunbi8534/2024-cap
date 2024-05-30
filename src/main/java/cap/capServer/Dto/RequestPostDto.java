package cap.capServer.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RequestPostDto {
    String username;
    int mediaId;
    String mediaTitle;
    String mediaType;
    String postTitle;
    String postContent;
}
