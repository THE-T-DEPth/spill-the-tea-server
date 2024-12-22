package the_t.mainproject.domain.auth.application;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import the_t.mainproject.domain.auth.dto.request.JoinReq;
import the_t.mainproject.domain.member.domain.Member;
import the_t.mainproject.domain.member.domain.repository.MemberRepository;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class AuthServiceImpl implements AuthService {

    private final MemberRepository memberRepository;

    @Transactional
    public ResponseEntity<?> join(JoinReq joinReq) {
        Member member = Member.builder()
                .email(joinReq.getEmail())
                .name(joinReq.getName())
                .password(joinReq.getPassword())
                .nickname(joinReq.getNickname())
                .build();

        memberRepository.save(member);

        return ResponseEntity.ok("회원가입 성공");
    }
}
