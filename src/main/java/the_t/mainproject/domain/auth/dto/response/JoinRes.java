package the_t.mainproject.domain.auth.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JoinRes {

    @Schema(type = "string", example = "서버핑", description="회원가입 때 입력한 닉네임을 반환합니다."
    )
    private String nickname;
}
