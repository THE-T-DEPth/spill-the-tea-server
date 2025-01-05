package the_t.mainproject.domain.auth.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReissueRes {

    @Schema(
            type = "String",
            example = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJBY2Nlc3NUb2tlbiIsInJvbGUiOiJpNjkwMzY3QGdtYWlsLmNvbSIsImlkIjoiaTY5MDM2N0BnbW",
            description = "새로 발급된 access token을 출력합니다."
    )
    private String accessToken;
}
