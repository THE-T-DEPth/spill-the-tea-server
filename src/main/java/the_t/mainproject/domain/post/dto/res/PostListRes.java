package the_t.mainproject.domain.post.dto.res;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class PostListRes {

    @Schema(type = "Long", example = "1", description = "게시글 ID")
    public Long postId;

    @Schema(type = "String", example = "제목 예시", description = "게시글 제목")
    public String title;

    @Schema(type = "String", example = "https://this-is-thumb.jpg", description = "게시글 썸네일 URL")
    public String thumbUrl;

    @Schema(type = "Integer", example = "1", description = "게시글 좋아요 개수")
    public Integer likedCount;

    @Schema(type = "Integer", example = "1", description = "게시글 댓글 개수")
    public Integer commentCount;

    @Schema(type = "String", example = "[\"덕질\", \"실수\", \"고민\"]", description = "키워드 3개")
    public String keywordList;

    @Schema(type = "LocalDate", example = "24.12.31", description = "게시글 작성일")
    @JsonFormat(pattern = "yy.MM.dd", shape = JsonFormat.Shape.STRING)
    private LocalDate createDate;

    @Schema(type = "LocalTime", example = "02:21", description = "게시글 작성시간")
    @JsonFormat(pattern = "HH:mm", shape = JsonFormat.Shape.STRING)
    private LocalTime createTime;

    @Schema(type = "boolean", example = "true", description = "사용자가 공감을 눌렀는지 여부 (이미 눌렀으면 true, 아니면 false)")
    private boolean isLiked;
}
