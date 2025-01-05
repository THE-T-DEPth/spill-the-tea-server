package the_t.mainproject.domain.auth.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class LogoutReq {

    @Schema(type = "string",
            example = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJBY2Nlc3NUb2tlbiIsInJvbGUiOiJjdnZ6M0BuYXZlci5jb20iLCJpZCI6ImN2dnozQG5hdmVyLmNvbSIsImV4cCI6MTcyMjE4NTU5OSwiZW1haWwiOiJjdnZ6M0BuYXZlci5jb20ifQ.6b3gEhqolM3PcdeDpaT1ExTuNV0_PSQQhGDFEk1IvDnPePbjtV2ZX3ds48_nWx77ci4nSEbS1XsajcV9yW_vBQ",
            description="access token입니다."
    )
    private String accessToken;

    @Schema(
            type = "string",
            example = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJSZWZyZXNoVG9rZW4iLCJleHAiOjE3MjIxODU2MDl9.hQckv2VUkHeKE2GOIe08xUbvKVwPPV6XoKo0xM5ZgppcIrIHeCCXUSOqhgZtsenMyryYNAgxCFeYDLA30-SfgQ",
            description="refresh token입니다."
    )
    private String refreshToken;
}
