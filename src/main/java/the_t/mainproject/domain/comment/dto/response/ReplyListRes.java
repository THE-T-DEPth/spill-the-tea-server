package the_t.mainproject.domain.comment.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
public class ReplyListRes {

    @Schema(type = "Long", example = "2", description = "대댓글 아이디를 반환합니다.")
    private Long commentId;

    @Schema(type = "Long", example = "1", description = "부모 댓글의 아이디를 반환합니다.")
    private Long parentCommentId;

    @Schema(type = "String", example = "image.jpg", description = "프로필 이미지를 표시합니다.")
    private String profileImage;

    @Schema(type = "String", example = "댓글 작성자 닉네임", description = "대댓글 작성자의 닉네임을 표시합니다.")
    private String nickname;

    @Schema(type = "String", example = "**댓글 내용**", description = "대댓글 내용을 표시합니다.")
    private String content;

    @Schema(type = "LocalTime", example = "12:25", description = "대댓글 생성된 시간을 표시합니다.")
    @JsonFormat(pattern = "HH:mm", shape = JsonFormat.Shape.STRING, timezone = "Asia/Seoul")
    private LocalTime createTime;

    @Schema(type = "LocalDate", example = "2025.01.01", description = "대댓글 생성된 날짜를 표시합니다.")
    @JsonFormat(pattern = "yy.MM.dd", shape = JsonFormat.Shape.STRING, timezone = "Asia/Seoul")
    private LocalDate createDate;

    @Schema(type = "Integer", example = "1", description = "대댓글 공감 수를 표시합니다.")
    private Integer likedCount;
}
