package cap.capServer.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class MediaInfo {
    String username;
    String mediaTitle;
    String mediaMode;
    String instrument;
    String content_name;
    List<String> tags;
}
