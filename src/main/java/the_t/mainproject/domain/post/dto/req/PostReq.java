package the_t.mainproject.domain.post.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import the_t.mainproject.domain.post.domain.VoiceType;

import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@Getter
public class PostReq {

    @NotBlank(message = "제목을 입력해주세요")
    @Size(min = 1, max = 12, message = "제목은 최소 1자부터 최대 12자까지 가능합니다")
    @Schema(type = "String", example = "제목 예시", description = "제목")
    private String title;

    @NotBlank(message = "내용을 입력해주세요")
    @Size(min = 2, max = 1500, message = "내용은 최소 2자부터 최대 1500자까지 가능합니다")
    @Schema(type = "String", example = "내용 예시", description = "내용. 소설화 적용 시 변환된 내용으로, 미적용 시는 원본 내용으로 작성해주세요")
    private String content;

    @NotEmpty(message = "키워드 3개를 입력해주세요")
    @Size(min = 3, max = 3, message = "키워드는 3개만 입력해주세요")
    @Schema(type = "List<String>", example = "[\"우정\", \"실수\", \"고민\"]", description = "키워드 3개를 리스트로 전달해주세요.")
    private List<String> keyword;

    @NotBlank(message = "음성 유형을 입력해주세요")
    @Schema(type = "String", example = "ko_KR_Standard_A", description = """
            음성 유형. 아래 유형 중 하나를 작성해주세요. 음성 변환 기능을 이용하지 않을 경우 none으로 입력하면 됩니다.
            none, ko_KR_Standard_A, ko_KR_Standard_C
            """)
    private String voice_type;

}
