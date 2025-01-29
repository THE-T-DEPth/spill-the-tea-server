package the_t.mainproject.domain.gpt.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class NovelizeRes {

    @Schema(type = "String", example = "소설화된 내용 예시", description = "소설화된 게시글 내용")
    public String novel;
}
