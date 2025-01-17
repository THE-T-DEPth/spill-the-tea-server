package the_t.mainproject.domain.comment.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@Builder
public class CommentListRes {

    @Schema(type = "Long", example = "1", description = "댓글 아이디를 반환합니다.")
    private Long commentId;

    @Schema(type = "Boolean", example = "true", description = "본인 댓글이면 true, 아니면 false를 반환합니다.")
    private Boolean mine;

    @Schema(type = "String", example = "image.jpg", description = "프로필 이미지를 표시합니다.")
    private String profileImage;

    @Schema(type = "String", example = "댓글 작성자 닉네임", description = "댓글 작성자의 닉네임을 표시합니다.")
    private String nickname;

    @Schema(type = "String", example = "**댓글 내용**", description = "댓글 내용을 표시합니다.")
    private String content;

    @Schema(type = "LocalTime", example = "12:25", description = "댓글 생성된 시간을 표시합니다.")
    @JsonFormat(pattern = "HH:mm", shape = JsonFormat.Shape.STRING, timezone = "Asia/Seoul")
    private LocalTime createTime;

    @Schema(type = "LocalDate", example = "2025.01.01", description = "댓글 생성된 날짜를 표시합니다.")
    @JsonFormat(pattern = "yy.MM.dd", shape = JsonFormat.Shape.STRING, timezone = "Asia/Seoul")
    private LocalDate createDate;

    @Schema(type = "Integer", example = "1", description = "댓글 공감 수를 표시합니다.")
    private Integer likedCount;

    @ArraySchema(schema = @Schema(implementation = ReplyListRes.class))
    @Schema(description = "대댓글 리스트")
    private List<ReplyListRes> replyList;
}
