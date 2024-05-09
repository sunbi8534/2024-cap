package cap.capServer.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ResponsePostDto {
    int id;
    String username;
    String mediaTitle;
    String postTitle;
    String postContent;
    String mediaURL;
}
