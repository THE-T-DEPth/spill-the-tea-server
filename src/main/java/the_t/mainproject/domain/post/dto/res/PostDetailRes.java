package the_t.mainproject.domain.post.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class PostDetailRes {

    @Schema(type = "Long", example = "1", description = "게시글 ID")
    public Long postId;

    @Schema(type = "String", example = "1", description = "게시글 제목")
    public String title;

    @Schema(type = "String", example = "1", description = "게시글 내용")
    public String content;

    @Schema(type = "String", example = "1", description = "게시글 썸네일 링크")
    public String thumb;

    @Schema(type = "Integer", example = "1", description = "게시글 좋아요 개수")
    public Integer likedCount;

    @Schema(type = "Integer", example = "1", description = "게시글 댓글 개수")
    public Integer commentCount;

    @Schema(type = "String", example = "1", description = "게시글 음성 유형")
    public String voiceType;
}
