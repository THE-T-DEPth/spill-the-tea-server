package the_t.mainproject.domain.auth.application;

import the_t.mainproject.domain.auth.dto.request.JoinReq;
import the_t.mainproject.domain.auth.dto.request.LoginReq;
import the_t.mainproject.domain.auth.dto.request.ModifyPasswordReq;
import the_t.mainproject.domain.auth.dto.response.DuplicateCheckRes;
import the_t.mainproject.domain.auth.dto.response.LoginRes;
import the_t.mainproject.global.common.Message;
import the_t.mainproject.global.common.SuccessResponse;

public interface AuthService {

    SuccessResponse<Message> join(JoinReq joinReq);

    SuccessResponse<LoginRes> login(LoginReq loginReq);

    SuccessResponse<DuplicateCheckRes> checkEmailDuplicate(String email);

    SuccessResponse<DuplicateCheckRes> checkNicknameDuplicate(String nickname);

    SuccessResponse<Message> modifyPassword(ModifyPasswordReq modifyPasswordReq);
}
