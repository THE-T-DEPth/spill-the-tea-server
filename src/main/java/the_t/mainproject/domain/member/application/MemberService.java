package the_t.mainproject.domain.member.application;

import org.springframework.web.multipart.MultipartFile;
import the_t.mainproject.domain.member.dto.MemberUpdateReq;
import the_t.mainproject.global.common.Message;
import the_t.mainproject.global.common.SuccessResponse;
import the_t.mainproject.global.security.UserDetailsImpl;

public interface MemberService {

    SuccessResponse<Message> modifyProfileImage(UserDetailsImpl userDetails, MultipartFile profile_image);

    SuccessResponse<Message> deleteProfileImage(UserDetailsImpl userDetails);

    SuccessResponse<Message> updateNicknamePassword(UserDetailsImpl userDetails, MemberUpdateReq memberUpdateReq);
}
