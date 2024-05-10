package cap.capServer.Service;

import cap.capServer.Dto.LoginResultDto;
import cap.capServer.Dto.UserEnrollDto;
import cap.capServer.Repository.LoginRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Service
public class LoginService {
    private LoginRepository loginRepository;

    public LoginService(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }
    public LoginResultDto checkUser(String id, String pw) {
        return loginRepository.checkUser(id, makeHashcode(pw));
    }

    public String checkDupNickname(String nickname) {
        return loginRepository.checkDupNickname(nickname);
    }

    public String checkDupId(String id) {
        return loginRepository.checkDupId(id);
    }

    public String enrollUser(UserEnrollDto user) {
        user.setPw(makeHashcode(user.getPw()));
        return loginRepository.enrollUser(user);
    }

    public String makeHashcode(String str) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = md.digest(str.getBytes());

            StringBuilder hashHex = new StringBuilder();
            for (byte b : hashBytes) {
                hashHex.append(String.format("%02x", b));
            }

            return hashHex.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}

