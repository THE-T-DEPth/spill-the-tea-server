package the_t.mainproject.domain.auth.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginRes {

    private String accessToken;

    private String refreshToken;
}