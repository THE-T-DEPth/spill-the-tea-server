package the_t.mainproject.infrastructure.mail.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MailCodeRes {

    @Schema(type = "string", example = "abcd1234", description = "인증 코드를 출력합니다. 이메일 인증 시 사용자에게 발급되는 임시 키 값입니다.")
    private String code;
}
