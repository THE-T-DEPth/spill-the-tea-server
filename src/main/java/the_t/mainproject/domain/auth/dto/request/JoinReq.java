package the_t.mainproject.domain.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class JoinReq {

    @NotBlank(message = "이메일을 입력해주세요")
    @Email
    @Schema(type = "String", example = "spillthetea@gmail.com", description = "이메일")
    private String email;

    @NotBlank(message = "비밀번호를 입력해주세요")
    @Size(min = 8, max = 20, message = "최소 8자 최대 20자까지 가능합니다")
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-={}\\[\\]|:;\"'<>,.?/~`])[A-Za-z\\d!@#$%^&*()_+\\-={}\\[\\]|:;\"'<>,.?/~`]*$",
            message = "비밀번호는 8~20자 이내여야 하며, 대소문자, 숫자, 특수문자를 각각 최소 1개씩 포함해야 합니다."
    )
    @Schema(type = "String", example = "Password1!", description = "비밀번호")
    private String password;

    @NotBlank(message = "이름을 입력해주세요")
    @Size(min = 2, max = 8, message = "이름은 최소 2자 최대 8자까지 가능합니다")
    @Schema(type = "String", example = "김더티", description = "이름")
    private String name;

    @NotBlank(message = "닉네임을 입력해주세요")
    @Size(min = 2, max = 8, message = "닉네임은 최소 2자 최대 8자까지 가능합니다.")
    @Pattern(regexp = "^[a-zA-Z가-힣\\d]+$")
    @Schema(type = "String", example = "서버핑", description = "닉네임")
    private String nickname;
}
