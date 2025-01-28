package the_t.mainproject.domain.auth.application;

import the_t.mainproject.domain.auth.dto.request.JoinReq;
import the_t.mainproject.domain.auth.dto.request.LoginReq;
import the_t.mainproject.domain.auth.dto.request.LogoutReq;
import the_t.mainproject.domain.auth.dto.request.ModifyPasswordReq;
import the_t.mainproject.domain.auth.dto.response.DuplicateCheckRes;
import the_t.mainproject.domain.auth.dto.response.JoinRes;
import the_t.mainproject.domain.auth.dto.response.LoginRes;
import the_t.mainproject.domain.auth.dto.response.ReissueRes;
import the_t.mainproject.global.common.Message;
import the_t.mainproject.global.common.SuccessResponse;
import the_t.mainproject.global.security.UserDetailsImpl;

public interface AuthService {

    SuccessResponse<JoinRes> join(JoinReq joinReq);

    SuccessResponse<LoginRes> login(LoginReq loginReq);

    SuccessResponse<Message> logout(UserDetailsImpl userDetails, LogoutReq logoutReq);

    SuccessResponse<Message> exit(UserDetailsImpl userDetails);

    SuccessResponse<ReissueRes> reissue(String refreshToken);

    SuccessResponse<DuplicateCheckRes> checkEmailDuplicate(String email);

    SuccessResponse<DuplicateCheckRes> checkNicknameDuplicate(String nickname);

    SuccessResponse<Message> modifyPassword(ModifyPasswordReq modifyPasswordReq);
}
