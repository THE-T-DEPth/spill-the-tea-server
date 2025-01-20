package the_t.mainproject.domain.member.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class MemberUpdateReq {

    @Schema(type = "String", example = "서버핑", description = "변경할 닉네임을 입력하세요.")
    private String nickname;

    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-={}\\[\\]|:;\"'<>,.?/~`])[A-Za-z\\d!@#$%^&*()_+\\-={}\\[\\]|:;\"'<>,.?/~`]*$")
    @Schema(type = "String", example = "password1!", description = "변경할 비밀번호를 입력해주세요")
    private String newPassword;

    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-={}\\[\\]|:;\"'<>,.?/~`])[A-Za-z\\d!@#$%^&*()_+\\-={}\\[\\]|:;\"'<>,.?/~`]*$")
    @Schema(type = "String", example = "password1!", description = "비밀번호를 재입력해주세요")
    private String confirmPassword;
}
