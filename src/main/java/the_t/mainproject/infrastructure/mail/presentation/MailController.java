package the_t.mainproject.infrastructure.mail.presentation;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import the_t.mainproject.global.common.Message;
import the_t.mainproject.global.common.SuccessResponse;
import the_t.mainproject.infrastructure.mail.application.MailService;
import the_t.mainproject.infrastructure.mail.dto.MailCodeRes;
import the_t.mainproject.infrastructure.mail.dto.TempPasswordRes;

@RequiredArgsConstructor
@RestController
@RequestMapping("/mails")
public class MailController {

    private final MailService mailService;

    @Operation(summary = "회원가입을 위한 인증 코드 이메일 발송")
    @GetMapping(value = "/join")
    public ResponseEntity<SuccessResponse<MailCodeRes>> sendVerifyMail(@RequestParam String email) throws Exception {
        return ResponseEntity.ok(mailService.sendVerifyMail(email));
    }

    @Operation(summary = "회원가입을 위한 인증 코드 검증")
    @GetMapping(value = "/join/verify")
    public ResponseEntity<SuccessResponse<Message>> verifyEmailCode(@RequestParam String code) {
        return ResponseEntity.ok(mailService.verifyEmailCode(code));
    }

    @Operation(summary = "비밀번호 재설정 인증 코드 이메일 발송")
    @GetMapping(value = "/password")
    public ResponseEntity<SuccessResponse<MailCodeRes>> sendPasswordMail(@RequestParam String email) throws Exception {
        return ResponseEntity.ok(mailService.sendPasswordMail(email));
    }

    @Operation(summary = "비밀번호 인증코드 검증 및 임시 비밀번호 발급")
    @PostMapping(value = "/password/verify")
    public ResponseEntity<SuccessResponse<TempPasswordRes>> verifyPasswordCode(@RequestParam String code) {
        return ResponseEntity.ok(mailService.passwordEmailCode(code));
    }
}
