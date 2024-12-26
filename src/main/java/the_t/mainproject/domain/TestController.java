package the_t.mainproject.domain;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping
    public String test() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return "\n name : " + authentication.getName() +
                "\n getPrincipal : " + authentication.getPrincipal() +
                "\n getAuthorities : " + authentication.getAuthorities() +
                "\n getDetails : " + authentication.getDetails() +
                "\n getCredentials : " + authentication.getCredentials() +
                "\n HELLO!!";
    }
}
