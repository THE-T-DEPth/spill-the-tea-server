package the_t.mainproject.domain.image.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class ImageRes {

    @Schema(type = "Long", example = "1", description = "이미지 id")
    public Long id;

    @Schema(type = "String", example = "https://spill-the-tea-bucket.s3.ap-northeast-2.amazonaws" +
            ".com/Shaun%21-431ca7d7-70ed-42c9-b5bd-af8c0db33a1d.jpg", description = "이미지 링크")
    public String url;

}
