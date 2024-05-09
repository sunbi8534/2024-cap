package cap.capServer.Service;

import cap.capServer.Dto.RequestPostDto;
import cap.capServer.Dto.ResponsePostDto;
import cap.capServer.Dto.ResponsePostListDto;
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

    public List<ResponsePostListDto> getPosts() {
        return communityRepository.getPosts();
    }

    public ResponsePostDto getPost(int id) {
        return communityRepository.getPost(id);
    }
}
