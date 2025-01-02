package the_t.mainproject.infrastructure.mail.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TempPasswordRes {

    @Schema(type = "string", example = "v1Tx#3kx", description = "임시 비밀번호입니다.")
    private String temp_password;
}
