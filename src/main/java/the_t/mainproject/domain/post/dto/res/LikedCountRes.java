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
public class LikedCountRes {
    @Schema(type = "Long", example = "1", description = "게시글 ID")
    public Long postId;

    @Schema(type = "Integer", example = "1", description = "게시글 좋아요 개수")
    public Integer likedCount;
}
