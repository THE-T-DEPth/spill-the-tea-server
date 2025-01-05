package the_t.mainproject.domain.comment.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateCommentReq {

    @Schema(type = "Long", example = "1", description = "부모 댓글 아이디입니다. 대댓글 작성 시 사용됩니다.")
    private Long parentCommentId;

    @Schema(type = "Long", example = "1", description = "게시글 아이디입니다.")
    private Long postId;

    @NotBlank(message = "내용을 입력해주세요.")
    @Schema(type = "String", example = "**댓글 내용**", description = "댓글 내용입니다.")
    private String content;
}
