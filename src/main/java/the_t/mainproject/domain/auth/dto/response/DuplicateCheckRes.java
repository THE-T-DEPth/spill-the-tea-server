package the_t.mainproject.domain.auth.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DuplicateCheckRes {

    @Schema(type = "boolean", example = "true", description = "**사용 가능 여부**입니다. 값이 true면 중복되지 않아 사용 가능하다는 의미입니다.")
    private boolean availability;
}
