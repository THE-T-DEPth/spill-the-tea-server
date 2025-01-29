package the_t.mainproject.domain.gpt.dto.req;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GPTRequest {
    private String model;
    private List<GPTMessage> messages; // 필드명 변경 금지
    private int temperature;
    private int maxTokens;
    private int topP;
    private int frequencyPenalty;
    private int presencePenalty;

    @Builder
    public GPTRequest(String model, String prompt) {
        this.model = model;
        this.messages = new ArrayList<>();
        this.messages.add(new GPTMessage("user", prompt));
        this.temperature = 1;
        this.maxTokens = 512;
        this.topP = 1;
        this.frequencyPenalty = 0;
        this.presencePenalty = 0;
    }
}