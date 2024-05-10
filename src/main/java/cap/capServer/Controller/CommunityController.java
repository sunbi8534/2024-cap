package cap.capServer.Controller;

import cap.capServer.Dto.RequestPostDto;
import cap.capServer.Dto.ResponsePostDto;
import cap.capServer.Dto.ResponsePostListDto;
import cap.capServer.Service.CommunityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "Community-controller", description = "게시판과 관련된 처리를 하는 컨트롤러 입니다.") //클래스에 대한 설명을 할 수 있는 어노테이션이다.
public class CommunityController {
    CommunityService communityService;

    public CommunityController(CommunityService communityService) {
        this.communityService = communityService;
    }

    @Operation(summary = "음악 공유 API", description = "음악 게시판 공유에 필요한 정보들을 받은 후 게시판에 등록한다.")
    @PostMapping("/community/share")
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
