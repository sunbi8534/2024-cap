package cap.capServer.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostDto {
    int id;
    String mediaMode;
    String url;
    String url2;
    String imageUrl;
}
