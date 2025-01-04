package the_t.mainproject.domain.auth.presentation;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import the_t.mainproject.domain.auth.application.AuthServiceImpl;
import the_t.mainproject.domain.auth.dto.request.JoinReq;
import the_t.mainproject.domain.auth.dto.request.LoginReq;
import the_t.mainproject.domain.auth.dto.request.LogoutReq;
import the_t.mainproject.domain.auth.dto.request.ModifyPasswordReq;
import the_t.mainproject.domain.auth.dto.response.DuplicateCheckRes;
import the_t.mainproject.domain.auth.dto.response.LoginRes;
import the_t.mainproject.domain.auth.dto.response.ReissueRes;
import the_t.mainproject.global.common.Message;
import the_t.mainproject.global.common.SuccessResponse;
import the_t.mainproject.global.security.UserDetailsImpl;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthServiceImpl authService;

    @Operation(summary = "회원가입")
    @PostMapping("/join")
    public ResponseEntity<SuccessResponse<Message>> join(@Valid @RequestBody JoinReq joinReq) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(authService.join(joinReq));
    }

    @Operation(summary = "로그인")
    @PostMapping("/login")
    public ResponseEntity<SuccessResponse<LoginRes>> login(@Valid @RequestBody LoginReq loginReq) {
        return ResponseEntity.ok(authService.login(loginReq));
    }

    @Operation(summary = "로그아웃")
    @DeleteMapping(value = "/logout")
    public ResponseEntity<SuccessResponse<Message>> logout(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                           @RequestBody LogoutReq logoutReq) {
        return ResponseEntity.ok(authService.logout(userDetails, logoutReq));
    }

    @Operation(summary = "access token 재발급")
    @GetMapping(value = "/reissue")
    public ResponseEntity<SuccessResponse<ReissueRes>> reissue(@RequestParam(value = "refreshToken") String refreshToken) {
        return ResponseEntity.ok(authService.reissue(refreshToken));
    }

    @Operation(summary = "이메일 중복체크")
    @GetMapping("/emails")
    public ResponseEntity<SuccessResponse<DuplicateCheckRes>> checkEmailDuplicate(@RequestParam(value = "email") String email) {
        return ResponseEntity.ok(authService.checkEmailDuplicate(email));
    }

    @Operation(summary = "닉네임 중복체크")
    @GetMapping("/nicknames")
    public ResponseEntity<SuccessResponse<DuplicateCheckRes>> checkNicknameDuplicate(@RequestParam(value = "nickname") String nickname) {
        return ResponseEntity.ok(authService.checkNicknameDuplicate(nickname));
    }

    @Operation(summary = "비밀번호 변경")
    @PutMapping("/password")
    public ResponseEntity<SuccessResponse<Message>> modifyPassword(@Valid @RequestBody ModifyPasswordReq modifyPasswordReq) {
        return ResponseEntity.ok(authService.modifyPassword(modifyPasswordReq));
    }
}
