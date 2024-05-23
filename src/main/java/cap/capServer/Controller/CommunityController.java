package cap.capServer.Controller;

import cap.capServer.Dto.*;
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
    @PostMapping("/user/share")
    public String post(@RequestBody RequestPostDto postDto) {
        return communityService.post(postDto);
    }

    @Operation(summary = "게시글 리스트를 얻는 API", description = "게시판 리스트 목록을 반환한다.")
    @GetMapping("/community/postlist")
    public List<ResponsePostListDto> getPosts(@RequestParam String username) {
        return communityService.getPosts(username);
    }

    @Operation(summary = "id에 해당하는 게시글 정보를 얻는 API", description = "id에 해당하는 게시글 정보를 반환한다.")
    @GetMapping("/community/post")
    public ResponsePostDto getPost(@RequestParam int id, @RequestParam String username) {
        return communityService.getPost(id, username);
    }

    @PostMapping("/community/likePost")
    public boolean likePost(@RequestParam int id, @RequestParam String username) {
        return communityService.likePost(id, username);
    }

    @PostMapping("/community/writeComment")
    public boolean writeComment(@RequestParam int postId, @RequestBody WriteCommentDto writeCommentDto) {
        return communityService.writeComment(postId, writeCommentDto);
    }

    @PostMapping("/community/likeComment")
    public boolean likeComment(@RequestParam int postId, @RequestParam int commentId,
                               @RequestBody LikeCommentDto dto) {
        return communityService.likeComment(postId, commentId, dto.getUsername());
    }

}
