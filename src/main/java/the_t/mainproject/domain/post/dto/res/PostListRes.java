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
public class PostListRes {

    @Schema(type = "Long", example = "1", description = "게시글 ID")
    public Long postId;

    @Schema(type = "String", example = "제목 예시", description = "게시글 제목")
    public String title;

    @Schema(type = "String", example = "https://spill-the-tea-bucket.s3.ap-northeast-2.amazonaws.com/Shaun%21-431ca7d7-70ed-42c9-b5bd-af8c0db33a1d.jpg", description = "게시글 썸네일 링크")
    public String thumbUrl;

    @Schema(type = "Integer", example = "1", description = "게시글 좋아요 개수")
    public Integer likedCount;

    @Schema(type = "Integer", example = "1", description = "게시글 댓글 개수")
    public Integer commentCount;

    @Schema(type = "String", example = "[\"덕질\", \"실수\", \"고민\"]", description = "키워드 3개")
    public String keywordList;

    @Schema(type = "String", example = "24.12.31", description = "작성일")
    public String createdDate;

    @Schema(type = "String", example = "02:21", description = "작성시간")
    public String createdTime;
}
