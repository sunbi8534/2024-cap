package cap.capServer.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class MusicListDto {
    int id;
    String title;
    boolean progress;
    List<String> tags;
    String mediaUrl;
    String mediaUrl2;
    String mediaType;
    String coverImageUrl;
}
