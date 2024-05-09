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
    String postTitle;
}
