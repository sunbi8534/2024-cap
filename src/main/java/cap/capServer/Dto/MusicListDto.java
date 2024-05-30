package cap.capServer.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MusicListDto {
    int id;
    String title;
    boolean progress;
    String url;
    String mediaType;
}
