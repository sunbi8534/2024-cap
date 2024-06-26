package cap.capServer.Service;

import cap.capServer.Dto.SendDto;
import cap.capServer.Repository.S3Repository;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class S3Uploader {
    private final AmazonS3Client amazonS3Client;
    private S3Repository s3Repository;
    private WebClientService webClientService;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Autowired
    public S3Uploader(AmazonS3Client amazonS3Client,
                      S3Repository s3Repository,
                      WebClientService webClientService) {
        this.amazonS3Client = amazonS3Client;
        this.s3Repository = s3Repository;
        this.webClientService = webClientService;
    }

    // MultipartFile을 전달받아 File로 전환한 후 S3에 업로드
    public void upload(MultipartFile multipartFile, MultipartFile coverImageFile, String dirName,
                         String nickname, String mediaTitle, String mediaMode, String instrument, String content_name,
                         List<String> tags, int tempo) throws IOException { // dirName의 디렉토리가 S3 Bucket 내부에 생성됨
        File uploadFile = convert(multipartFile)
                .orElseThrow(() -> new IllegalArgumentException("MultipartFile -> File 전환 실패"));
        File imageFile = convert(coverImageFile)
                .orElseThrow(() -> new IllegalArgumentException("MultipartFile -> imageFile 전환 실패"));
        upload(uploadFile, imageFile, dirName, nickname, mediaTitle, mediaMode, tags, instrument, content_name, tempo);
    }

    private void upload(File uploadFile, File imageFile, String dirName, String nickname, String mediaTitle, String mediaMode,
                        List<String> tags, String instrument, String content_name, int tempo) {
        String fileName = dirName + "/" + uploadFile.getName();
        String uploadFileUrl = putS3(uploadFile, fileName);
        String imageName = dirName + "/" + imageFile.getName();
        String uploadImageUrl = putS3(imageFile, imageName);
        int id;
        if (uploadFileUrl != null) {
            id = s3Repository.saveFileURL(mediaTitle, mediaMode, uploadFileUrl, uploadImageUrl, nickname);
            s3Repository.saveTags(id, tags);
        } else {
            id = 0;
        }
//        SendDto dto = new SendDto();
//        dto.setUser(nickname);
//        dto.setInstrument(instrument);
//        dto.setS3_url(uploadFileUrl);
//        dto.setContent_name(content_name);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("s3_url", uploadFileUrl);
        requestBody.put("user", nickname);
        requestBody.put("instrument", instrument);
        requestBody.put("content_name", content_name);
        requestBody.put("tempo", tempo);

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = "";
        try {
            jsonString = objectMapper.writeValueAsString(requestBody);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        String targetUrl = "http://112.118.185.174:40094/start_generation";
        CompletableFuture<String> futureResponse = webClientService.sendPostRequestAsync(targetUrl, jsonString);
        // 비동기 응답 처리
        futureResponse.thenAccept(response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                String url = jsonObject.getString("url");
                String url2 = jsonObject.getString("url2");
                System.out.println(url);
                System.out.println(url2);
                s3Repository.saveGeneratedUrl(id, url, url2);

            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }).exceptionally(ex -> {
            System.err.println("Request failed: " + ex.getMessage());
            return null;
        });

        removeNewFile(uploadFile);  // convert()함수로 인해서 로컬에 생성된 File 삭제 (MultipartFile -> File 전환 하며 로컬에 파일 생성됨
        removeNewFile(imageFile);
    }

    private String putS3(File uploadFile, String fileName) {
        amazonS3Client.putObject(
                new PutObjectRequest(bucket, fileName, uploadFile)
                        .withCannedAcl(CannedAccessControlList.PublicRead)	// PublicRead 권한으로 업로드 됨
        );
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

    private void removeNewFile(File targetFile) {
        if(targetFile.delete()) {
            System.out.println("파일이 삭제되었습니다.");
        }else {
            System.out.println("파일이 삭제되지 못했습니다.");
        }
    }

    private Optional<File> convert(MultipartFile multipartFile) throws  IOException {
        byte[] bytes = multipartFile.getBytes();
        File file = new File(multipartFile.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(bytes);
        }

        //Mad::Lib를 이용해서 mp3파일을 midi파일로 변환하면 된다.
        return Optional.of(file);

    }

    public void deleteImageFromS3(String imageAddress){
        String key = getKeyFromImageAddress(imageAddress);
        try{
            amazonS3Client.deleteObject(new DeleteObjectRequest(bucket, key));
        }catch (Exception e){
            throw new AmazonS3Exception("delete error");
        }
    }

    private String getKeyFromImageAddress(String imageAddress){
        try{
            URL url = new URL(imageAddress);
            String decodingKey = URLDecoder.decode(url.getPath(), "UTF-8");
            return decodingKey.substring(1); // 맨 앞의 '/' 제거
        }catch (MalformedURLException | UnsupportedEncodingException e){
            throw new AmazonS3Exception("URL error");
        }
    }
}

