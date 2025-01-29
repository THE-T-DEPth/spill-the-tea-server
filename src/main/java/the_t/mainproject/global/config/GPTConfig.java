package the_t.mainproject.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class GPTConfig {

    @Value("${openai.secret-key}")
    private String secretKey;
    @Value("${openai.model}")
    private String model;
    @Value("${openai.url}")
    private String apiUrl;

    public String getSecretKey() {
        return secretKey;
    }
    public String getModel() {
        return model;
    }
    public String getApiUrl() {
        return apiUrl;
    }

    @Bean
    public RestTemplate restTemplate(){
        RestTemplate template = new RestTemplate();
        template.getInterceptors().add((request, body, execution) -> {
            String authHeader = "Bearer " + secretKey;
            request.getHeaders().add("Authorization", authHeader);
            return execution.execute(request, body);
        });

        return template;

    }
}
