package cap.capServer.Dto;

import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Setter
public class FileUploadResponse {
    String nickname;
    MultipartFile file;

}
