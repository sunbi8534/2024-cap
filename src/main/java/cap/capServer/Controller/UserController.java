package cap.capServer.Controller;

import cap.capServer.Dto.GetMusicResponse;
import cap.capServer.Dto.MusicListDto;
import cap.capServer.Service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Tag(name = "User-controller", description = "유저에 대한 정보를 얻을 수 있는 컨트롤러 입니다.")
public class UserController {
    UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user/musics")
    public List<MusicListDto> getMusicProgress(@RequestParam String username) {
        return userService.getMusicProgress(username);
    }

    @GetMapping("/user/music")
    public GetMusicResponse getMusicUrl(@RequestParam String username, String music) {
        return userService.getMusicUrl(username, music);
    }
}
