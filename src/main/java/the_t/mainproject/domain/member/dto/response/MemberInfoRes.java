package the_t.mainproject.domain.member.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MemberInfoRes {

    @Schema(type = "String", example = "image.jpg", description = "프로필 이미지를 반환합니다.")
    private String profileImage;

    @Schema(type = "String", example = "서버핑", description = "회원의 닉네임을 반환합니다.")
    private String nickname;
}
