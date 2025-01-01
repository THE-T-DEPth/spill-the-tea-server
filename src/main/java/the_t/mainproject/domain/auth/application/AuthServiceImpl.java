package the_t.mainproject.domain.auth.application;

import lombok.RequiredArgsConstructor;
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
import the_t.mainproject.domain.auth.dto.response.DuplicateCheckRes;
import the_t.mainproject.domain.auth.dto.response.LoginRes;
import the_t.mainproject.domain.member.domain.Member;
import the_t.mainproject.domain.member.domain.repository.MemberRepository;
import the_t.mainproject.global.common.Message;
import the_t.mainproject.global.common.SuccessResponse;
import the_t.mainproject.global.security.jwt.JwtTokenProvider;
import the_t.mainproject.infrastructure.redis.RedisUtil;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class AuthServiceImpl implements AuthService {

    private final MemberRepository memberRepository;
    private final AuthenticationManager authenticationManager;
    private final RedisUtil redisUtil;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public SuccessResponse<Message> join(JoinReq joinReq) {
        String email = joinReq.getEmail();
        String nickname = joinReq.getNickname();

        if(memberRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("이미 가입된 이메일입니다.");
        }
        if(memberRepository.existsByNickname(nickname)) {
            throw new IllegalArgumentException("이미 사용중인 닉네임입니다.");
        }

        String data = redisUtil.getData(email + "_verify");
        if(data == null) {
            throw new IllegalArgumentException("인증이 필요한 이메일입니다.");
        }
        redisUtil.deleteData(email + "_verify");

        Member member = Member.builder()
                .email(email)
                .name(joinReq.getName())
                .password(passwordEncoder.encode(joinReq.getPassword()))
                .nickname(nickname)
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

    @Override
    public SuccessResponse<DuplicateCheckRes> checkEmailDuplicate(String email) {
        DuplicateCheckRes emailDuplicateCheckRes = DuplicateCheckRes.builder()
                .availability(!memberRepository.existsByEmail(email))
                .build();

        return SuccessResponse.of(emailDuplicateCheckRes);
    }

    @Override
    public SuccessResponse<DuplicateCheckRes> checkNicknameDuplicate(String nickname) {
        DuplicateCheckRes nicknameDuplicateCheckRes = DuplicateCheckRes.builder()
                .availability(!memberRepository.existsByNickname(nickname))
                .build();

        return SuccessResponse.of(nicknameDuplicateCheckRes);
    }
}
