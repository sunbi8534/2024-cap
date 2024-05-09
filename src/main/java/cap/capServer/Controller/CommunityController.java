package cap.capServer.Controller;

import cap.capServer.Dto.RequestPostDto;
import cap.capServer.Dto.ResponsePostDto;
import cap.capServer.Dto.ResponsePostListDto;
import cap.capServer.Service.CommunityService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CommunityController {
    CommunityService communityService;

    public CommunityController(CommunityService communityService) {
        this.communityService = communityService;
    }

    @Operation(summary = "음악 공유 API", description = "음악 게시판 공유에 필요한 정보들을 받은 후 게시판에 등록한다.")
    @PostMapping("/user/share")
    public String post(@RequestBody RequestPostDto postDto) {
        return communityService.post(postDto);
    }

    @Operation(summary = "게시글 리스트를 얻는 API", description = "게시판 리스트 목록을 반환한다.")
    @GetMapping("/community/postlist")
    public List<ResponsePostListDto> getPosts() {
        return communityService.getPosts();
    }

    @Operation(summary = "id에 해당하는 게시글 정보를 얻는 API", description = "id에 해당하는 게시글 정보를 반환한다.")
    @GetMapping("/community/post")
    public ResponsePostDto getPost(@RequestParam int id) {
        return communityService.getPost(id);
    }
}
