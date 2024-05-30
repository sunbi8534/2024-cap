package cap.capServer.Service;

import cap.capServer.Repository.S3Repository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
public class WebClientService {
    private WebClient webClient;
    private S3Repository s3Repository;

    public WebClientService(WebClient.Builder webClientBuilder, S3Repository s3Repository) {
        this.webClient = webClientBuilder.baseUrl("http://125.136.64.90:41268").build();
        this.s3Repository = s3Repository;
    }

    public void sendPostRequestAsync(int id, Map<String, Object> requestBody) {
        CompletableFuture.runAsync(() -> {
            webClient.post()
                    .uri("/start_generation")
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(String.class)
                    .subscribe(response -> {
                        s3Repository.saveGeneratedUrl(id, response);
                        System.out.println("Response: " + response);
                    }, error -> {
                        System.err.println("Error: " + error.getMessage());
                    });
        });
    }
}