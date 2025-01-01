package the_t.mainproject.domain.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginReq {

    @NotBlank(message = "이메일을 입력해주세요")
    @Email
    @Schema(type = "String", example = "spillthetea@gmail.com", description = "로그인할 이메일을 입력해주세요")
    private String email;

    @NotBlank(message = "비밀번호를 입력해주세요")
    @Schema(type = "String", example = "password1!", description = "비밀번호를 입력해주세요")
    private String password;
}
