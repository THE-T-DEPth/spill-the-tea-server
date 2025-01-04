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
import the_t.mainproject.domain.member.domain.Member;
import the_t.mainproject.domain.member.domain.repository.MemberRepository;
import the_t.mainproject.global.security.UserDetailsImpl;
import the_t.mainproject.global.security.jwt.JwtTokenProvider;
import the_t.mainproject.infrastructure.redis.RedisUtil;

import java.io.IOException;

// AccessToken을 이용한 인증 필터
@RequiredArgsConstructor
public class JwtAuthenticationProcessingFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;
    private final RedisUtil redisUtil;

    private GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        checkAccessTokenAndAuthentication(request, response, filterChain);
    }

    private void checkAccessTokenAndAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        jwtTokenProvider.extractAccessToken(request)
                .filter(jwtTokenProvider::isTokenValid)
                .filter(accessToken -> !jwtTokenProvider.isTokenInBlackList(accessToken))
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
}
