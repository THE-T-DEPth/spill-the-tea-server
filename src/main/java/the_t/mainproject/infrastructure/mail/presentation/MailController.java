package the_t.mainproject.infrastructure.mail.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import the_t.mainproject.global.common.Message;
import the_t.mainproject.global.common.SuccessResponse;
import the_t.mainproject.infrastructure.mail.application.MailService;
import the_t.mainproject.infrastructure.mail.dto.MailCodeRes;

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
    public ResponseEntity<SuccessResponse<Message>> verify(@RequestParam String code) {
        return ResponseEntity.ok(SuccessResponse.of(Message.builder().message("verify 호출").build()));
    }
}
