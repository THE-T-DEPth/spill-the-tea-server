package the_t.mainproject.domain.auth.presentation;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import the_t.mainproject.domain.auth.application.AuthServiceImpl;
import the_t.mainproject.domain.auth.dto.request.JoinReq;
import the_t.mainproject.domain.auth.dto.request.LoginReq;
import the_t.mainproject.domain.auth.dto.response.EmailDuplicateCheckRes;
import the_t.mainproject.domain.auth.dto.response.LoginRes;
import the_t.mainproject.global.common.Message;
import the_t.mainproject.global.common.SuccessResponse;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthServiceImpl authService;

    @PostMapping("/join")
    public ResponseEntity<SuccessResponse<Message>> join(@Valid @RequestBody JoinReq joinReq) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(authService.join(joinReq));
    }

    @PostMapping("/login")
    public ResponseEntity<SuccessResponse<LoginRes>> login(@Valid @RequestBody LoginReq loginReq) {
        return ResponseEntity.ok(authService.login(loginReq));
    }

    @GetMapping("/email")
    public ResponseEntity<SuccessResponse<EmailDuplicateCheckRes>> checkEmailDuplicate(@RequestParam(value = "email") String email) {
        return ResponseEntity.ok(authService.checkEmailDuplicate(email));
    }
}
