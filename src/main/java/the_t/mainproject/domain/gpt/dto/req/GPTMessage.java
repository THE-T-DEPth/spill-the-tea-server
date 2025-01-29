package the_t.mainproject.domain.gpt.dto.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * GPT API 요청/응답 메시지를 구성하는 클래스
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GPTMessage {

    private String role;
    private String content;

}