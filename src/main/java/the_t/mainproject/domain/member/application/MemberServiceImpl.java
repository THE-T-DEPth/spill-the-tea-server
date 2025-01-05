package the_t.mainproject.domain.member.application;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import the_t.mainproject.domain.member.domain.Member;
import the_t.mainproject.domain.member.domain.repository.MemberRepository;
import the_t.mainproject.domain.member.dto.MemberUpdateReq;
import the_t.mainproject.global.common.Message;
import the_t.mainproject.global.common.SuccessResponse;
import the_t.mainproject.global.security.UserDetailsImpl;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

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

    @Override
    @Transactional
    public SuccessResponse<Message> deleteProfileImage(UserDetailsImpl userDetails) {
        Member member = memberRepository.findById(userDetails.getMember().getId())
                .orElseThrow(() -> new BadCredentialsException("잘못된 토큰 입력입니다."));

        member.updateProfileImage(null);

        Message message = Message.builder()
                .message("프로필 이미지 삭제가 완료되었습니다.")
                .build();

        return SuccessResponse.of(message);
    }

    @Override
    @Transactional
    public SuccessResponse<Message> updateNicknamePassword(UserDetailsImpl userDetails, MemberUpdateReq memberUpdateReq) {
        Member member = memberRepository.findById(userDetails.getMember().getId())
                .orElseThrow(() -> new BadCredentialsException("잘못된 토큰 입력입니다."));

        // 닉네임 수정
        if(memberUpdateReq.getNickname() != null && !memberUpdateReq.getNickname().trim().isEmpty()) {
            member.updateNickname(memberUpdateReq.getNickname());
        }

        // 비밀번호 수정
        if(memberUpdateReq.getNewPassword() != null && !memberUpdateReq.getNewPassword().trim().isEmpty()) {
            if(!memberUpdateReq.getNewPassword().equals(memberUpdateReq.getConfirmPassword())) {
                throw new IllegalArgumentException("비밀번호가 일치하지 않습니다");
            }

            member.updatePassword(passwordEncoder.encode(memberUpdateReq.getNewPassword()));
        }

        Message message = Message.builder()
                .message("회원 정보가 변경되었습니다.")
                .build();

        return SuccessResponse.of(message);
    }
}
