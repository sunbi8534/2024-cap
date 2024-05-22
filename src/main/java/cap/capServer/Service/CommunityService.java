package cap.capServer.Service;

import cap.capServer.Dto.RequestPostDto;
import cap.capServer.Dto.ResponsePostDto;
import cap.capServer.Dto.ResponsePostListDto;
import cap.capServer.Dto.WriteCommentDto;
import cap.capServer.Repository.CommunityRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CommunityService {
    CommunityRepository communityRepository;

    public CommunityService(CommunityRepository communityRepository) {
        this.communityRepository = communityRepository;
    }

    public String post(RequestPostDto postDto) {
        return communityRepository.post(postDto);
    }

    public List<ResponsePostListDto> getPosts(String username) {
        return communityRepository.getPosts(username);
    }

    public ResponsePostDto getPost(int id) {
        return communityRepository.getPost(id);
    }

    public boolean likePost(int id, String username) {
        return communityRepository.likePost(id, username);
    }

    public boolean writeComment(int postId, WriteCommentDto writeCommentDto) {
        return communityRepository.writeComment(postId, writeCommentDto);
    }

    public boolean likeComment(int postId, int commentId, String username) {
        return communityRepository.likeComment(postId, commentId, username);
    }
}
