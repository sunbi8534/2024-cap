package cap.capServer.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class UserEnrollDto {
    String id;
    String pw;
    String nickname;
}
