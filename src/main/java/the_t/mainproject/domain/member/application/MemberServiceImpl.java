package the_t.mainproject.domain.member.application;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import the_t.mainproject.domain.member.domain.Member;
import the_t.mainproject.domain.member.domain.repository.MemberRepository;
import the_t.mainproject.global.common.Message;
import the_t.mainproject.global.common.SuccessResponse;
import the_t.mainproject.global.security.UserDetailsImpl;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public SuccessResponse<Message> modifyProfileImage(UserDetailsImpl userDetails, String profile_image) {
        Member member = memberRepository.findById(userDetails.getMember().getId())
                .orElseThrow(() -> new BadCredentialsException("잘못된 토큰 입력입니다."));

        member.updateProfileImage(profile_image);

        Message message = Message.builder()
                .message("프로필 이미지 설정이 변경되었습니다.")
                .build();

        return SuccessResponse.of(message);
    }
}
