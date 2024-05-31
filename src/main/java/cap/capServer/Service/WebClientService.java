package cap.capServer.Service;

import cap.capServer.Dto.SendDto;
import cap.capServer.Repository.S3Repository;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
public class WebClientService {
    private WebClient webClient;
    private S3Repository s3Repository;

    public WebClientService(WebClient.Builder webClientBuilder, S3Repository s3Repository) {
        this.webClient = webClientBuilder.baseUrl("http://106.248.38.71:40729").build();
        this.s3Repository = s3Repository;
    }

//    public void sendPostRequestAsync(int id, Map<String, Object> requestBody) {
//        CompletableFuture.runAsync(() -> {
//            webClient.post()
//                    .uri("/test")
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .bodyValue(requestBody)
//                    .retrieve()
//                    .bodyToMono(String.class)
//                    .subscribe(response -> {
//                        s3Repository.saveGeneratedUrl(id, response);
//                        System.out.println("Response: " + response);
//                    }, error -> {
//                        System.err.println("Error: " + error.getMessage());
//                    });
//        });
//    }

    public CompletableFuture<String> sendPostRequestAsync(String targetUrl, String jsonPayload) {
        return CompletableFuture.supplyAsync(() -> {
            HttpURLConnection connection = null;
            try {
                URL url = new URL(targetUrl);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json; utf-8");
                connection.setRequestProperty("Accept", "application/json");
                connection.setDoOutput(true);

                // JSON 데이터를 요청 본문에 작성
                try (OutputStream os = connection.getOutputStream()) {
                    byte[] input = jsonPayload.getBytes(StandardCharsets.UTF_8);
                    os.write(input, 0, input.length);
                }

                // 응답 코드 확인
                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) { // HTTP 200
                    // 서버로부터 응답을 읽음
                    try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                        StringBuilder response = new StringBuilder();
                        String responseLine;
                        while ((responseLine = br.readLine()) != null) {
                            response.append(responseLine.trim());
                        }
                        return response.toString();
                    }
                } else {
                    throw new RuntimeException("Request failed with response code: " + responseCode);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        });
    }

}
