package the_t.mainproject.domain.auth.application;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import the_t.mainproject.domain.auth.domain.RefreshToken;
import the_t.mainproject.domain.auth.domain.repository.RefreshTokenRepository;
import the_t.mainproject.domain.auth.dto.request.JoinReq;
import the_t.mainproject.domain.auth.dto.request.LoginReq;
import the_t.mainproject.domain.auth.dto.response.LoginRes;
import the_t.mainproject.domain.member.domain.Member;
import the_t.mainproject.domain.member.domain.repository.MemberRepository;
import the_t.mainproject.global.common.Message;
import the_t.mainproject.global.common.SuccessResponse;
import the_t.mainproject.global.security.jwt.JwtTokenProvider;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class AuthServiceImpl implements AuthService {

    private final MemberRepository memberRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public SuccessResponse<Message> join(JoinReq joinReq) {
        Member member = Member.builder()
                .email(joinReq.getEmail())
                .name(joinReq.getName())
                .password(passwordEncoder.encode(joinReq.getPassword()))
                .nickname(joinReq.getNickname())
                .build();

        memberRepository.save(member);

        Message message = Message.builder()
                .message("회원가입이 완료됨")
                .build();

        return SuccessResponse.of(message);
    }

    @Override
    @Transactional
    public SuccessResponse<LoginRes> login(LoginReq loginReq) {
        String email = loginReq.getEmail();
        String password = loginReq.getPassword();

        // 인증
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Token 생성
        String accessToken = jwtTokenProvider.createAccessToken(email);
        String refreshToken = jwtTokenProvider.createRefreshToken();

        // 로그인 시 회원 체크
        memberRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("해당 이메일의 유저를 찾을 수 없습니다: " + email));

        // refreshToken 발급
        refreshTokenRepository.findByEmail(email)
                .ifPresentOrElse(
                        findRefreshToken -> findRefreshToken.updateRefreshToken(refreshToken),
                        () -> refreshTokenRepository.save(
                                RefreshToken.builder()
                                        .email(email)
                                        .refreshToken(refreshToken)
                                        .build()
                        )
                );

        refreshTokenRepository.save(RefreshToken.builder()
                .email(loginReq.getEmail())
                .refreshToken(refreshToken)
                .build()
        );

        LoginRes loginRes = LoginRes.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();

        return SuccessResponse.of(loginRes);
    }
}
