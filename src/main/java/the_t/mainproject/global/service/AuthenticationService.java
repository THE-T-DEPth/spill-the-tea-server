package the_t.mainproject.global.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import the_t.mainproject.global.security.UserDetailsImpl;

@Service
public class AuthenticationService {

    public Long getMemberIdFromAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetailsImpl) {
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            return userDetails.getMember().getId();
        }
        return null; // 인증되지 않은 경우 null 반환
    }
}
