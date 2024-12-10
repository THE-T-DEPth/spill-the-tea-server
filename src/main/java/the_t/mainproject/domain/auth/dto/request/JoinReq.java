package the_t.mainproject.domain.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class JoinReq {

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Size(min = 8, max = 20)
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-={}\\[\\]|:;\"'<>,.?/~`])[A-Za-z\\d!@#$%^&*()_+\\-={}\\[\\]|:;\"'<>,.?/~`]*$")
    private String password;

    @NotBlank
    @Size(min = 2, max = 8)
    private String name;

    @NotBlank
    @Size(min = 2, max = 8)
    @Pattern(regexp = "^[a-zA-Z가-힣\\d]+$")
    private String nickname;
}
