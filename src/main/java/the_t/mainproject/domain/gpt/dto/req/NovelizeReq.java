package the_t.mainproject.domain.gpt.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class NovelizeReq {

    @NotBlank(message = "내용을 입력해주세요")
    @Size(min = 2, max = 1500, message = "내용은 최소 2자부터 최대 1500자까지 가능합니다")
    @Schema(type = "String", example = "내용 예시", description = "내용")
    private String content;

}
