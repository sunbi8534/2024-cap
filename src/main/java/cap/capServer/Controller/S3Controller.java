package cap.capServer.Controller;

import cap.capServer.Dto.FileUploadResponse;
import cap.capServer.Service.S3Uploader;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController
@Tag(name = "S3-controller", description = "파일 관련 처리를 위한 컨트롤러 입니다.") //클래스에 대한 설명을 할 수 있는 어노테이션이다.
public class S3Controller {
    S3Uploader s3Uploader;

    public S3Controller(S3Uploader s3Uploader) {
        this.s3Uploader = s3Uploader;
    }

    @GetMapping("/test")
    public String test() {
        return "this is test";
    }

    @Operation(summary = "파일 업로드 API", description = "닉네임정보를 받아 파일을 업로드합니다.")
    @PostMapping(value = "/user/generate")
    public void uploadFile(
            @Parameter(description = "사용자 닉네임", required = true, example = "minho")
            @RequestPart(value = "name") String nickname,
            @Parameter(description = "오디오 파일", required = true, example = "song.mp3")
            @RequestPart(value = "file", required = false) MultipartFile file
    ) {

        FileUploadResponse response = new FileUploadResponse();
        String fileName = "";
        if(file != null){ // 파일 업로드한 경우에만

            try{// 파일 업로드
                fileName = s3Uploader.upload(file, "file", nickname); // S3 버킷의 file 디렉토리 안에 저장됨
                System.out.println("fileName = " + fileName);
            }catch (IOException e){
                e.printStackTrace();
            }
        }

//        try {
//            return ResponseEntity.status(HttpStatus.OK)
//                    .contentType(MediaType.valueOf(file.getContentType()))
//                    .body(file.getBytes());
//        }catch (IOException e) {
//            e.printStackTrace();
//        }
//        return null;
    }
}

