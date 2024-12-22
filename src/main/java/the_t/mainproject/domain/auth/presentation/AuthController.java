package the_t.mainproject.domain.auth.presentation;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import the_t.mainproject.domain.auth.application.AuthServiceImpl;
import the_t.mainproject.domain.auth.dto.request.JoinReq;
import the_t.mainproject.domain.auth.dto.request.LoginReq;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthServiceImpl authServiceImpl;

    @PostMapping("/join")
    public ResponseEntity<?> join(@Valid @RequestBody JoinReq joinReq) {
        return authServiceImpl.join(joinReq);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginReq loginReq) {
        return authServiceImpl.login(loginReq);
    }
}
