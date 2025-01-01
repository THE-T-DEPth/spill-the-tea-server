package the_t.mainproject.infrastructure.mail.presentation;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import the_t.mainproject.global.common.SuccessResponse;
import the_t.mainproject.infrastructure.mail.application.MailService;
import the_t.mainproject.infrastructure.mail.dto.MailCodeRes;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/mails")
public class MailController {

    private final MailService mailService;

    @GetMapping
    public ResponseEntity<SuccessResponse<MailCodeRes>> sendMail(@RequestParam String email) throws Exception {
        return ResponseEntity.ok(mailService.sendMail(email));
    }

    @GetMapping(value = "/verify")
    public void verify(@RequestParam String code, HttpServletResponse response) throws IOException {
        mailService.verifyCode(code);
        String redirect_url = "http://www.google.com";
        response.sendRedirect(redirect_url);
    }
}
