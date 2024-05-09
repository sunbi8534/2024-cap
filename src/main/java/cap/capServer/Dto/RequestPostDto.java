package cap.capServer.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RequestPostDto {
    String username;
    String mediaTitle;
    String postTitle;
    String postContent;
}
