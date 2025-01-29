package the_t.mainproject.domain.gpt.dto.res;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import the_t.mainproject.domain.gpt.dto.req.GPTMessage;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GPTResponse {
    
    private List<Choice> choices;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Choice {
        // GPT 대화 인덱스 번호
        private int index;
        // GPT로부터 받은 메세지
        private GPTMessage message;
    }
}