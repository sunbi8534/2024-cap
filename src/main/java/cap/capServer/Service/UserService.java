package cap.capServer.Service;

import cap.capServer.Dto.MusicListDto;
import cap.capServer.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    public List<MusicListDto> getMusicProgress(String nickname) {
        return userRepository.getMusicProgress(nickname);
    }

    public String getMusicUrl(String nickname, String fileName) {
        return userRepository.getMusicUrl(nickname, fileName);
    }
}
