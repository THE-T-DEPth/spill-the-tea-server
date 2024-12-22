package the_t.mainproject.global.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import the_t.mainproject.domain.auth.domain.repository.RefreshTokenRepository;
import the_t.mainproject.domain.member.domain.Member;
import the_t.mainproject.domain.member.domain.repository.MemberRepository;
import the_t.mainproject.global.security.UserDetailsImpl;
import the_t.mainproject.global.security.jwt.JwtTokenProvider;

import java.io.IOException;

// AccessToken을 이용한 인증 필터
@RequiredArgsConstructor
public class JwtAuthenticationProcessingFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    private GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();

    /**
     * 1. 리프레시 토큰이 오는 경우 -> 유효하면 AccessToken 재발급 후, 필터 진행 X, 바로 튕기기
     * 2. 리프레시 토큰은 없고 AccessToken만 있는 경우 -> 유저정보 저장 후 필터 계속 진행
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String refreshToken = jwtTokenProvider
                .extractRefreshToken(request)
                .filter(jwtTokenProvider::isTokenValid)
                .orElse(null);  // 2번 경우

        // 1번 경우
        if(refreshToken != null) {
            checkRefreshTokenAndReIssueAccessToken(response, refreshToken);
            return;
        }
        checkRefreshTokenAndReIssueAccessToken(request, response, filterChain);
    }

    // 2번 경우 메서드
    private void checkRefreshTokenAndReIssueAccessToken(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        jwtTokenProvider.extractAccessToken(request).filter(jwtTokenProvider::isTokenValid)
                .ifPresent(accessToken -> jwtTokenProvider.extractEmail(accessToken)
                        .ifPresent(email -> memberRepository.findByEmail(email)
                                .ifPresent(member -> saveAuthentication(member)
                                )
                        )
                );
        filterChain.doFilter(request, response);
    }

    private void saveAuthentication(Member member) {
        UserDetailsImpl userDetails = new UserDetailsImpl(member);

        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, authoritiesMapper.mapAuthorities(userDetails.getAuthorities()));
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }

    // 1번 경우 메서드
    private void checkRefreshTokenAndReIssueAccessToken(HttpServletResponse response, String refreshToken) {
        refreshTokenRepository.findByRefreshToken(refreshToken)
                .ifPresent(findRefreshToken ->
                        jwtTokenProvider.sendAccessToken(response, jwtTokenProvider.createAccessToken(findRefreshToken.getEmail()))
                );
    }
}
