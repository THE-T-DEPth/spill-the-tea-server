package the_t.mainproject.infrastructure.mail;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@RequiredArgsConstructor
@Component
public class MailUtil {

    private final String SUBJECT = "SpillTheTea 인증 링크입니다.";
    private final String SENDER_NAME = "SpillTheTea";

    @Value("${spring.mail.username}")
    private String fromEmail;

    private final JavaMailSender mailSender;

    public MimeMessage createMessage(String code, String email) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
        helper.setTo(email);
        helper.setSubject(SUBJECT);
        helper.setFrom(fromEmail);
        helper.setText(code, true);

        return mimeMessage;
    }


    // Description : 코드 생성 함수 (00000000 ~ zzzzzzzz) (8자리)
    public String generateCode() {
        SecureRandom random = new SecureRandom();
        // nextLong(long bound) : 0(포함)부터 입력된 bound(미포함) 사이의 랜덤 정수를 반환
        long randomNumber = random.nextLong(2821109907455L + 1);
        String code = Long.toString(randomNumber, 36);
        code = String.format("%8s", code).replace(' ', '0');

        return code;
    }
}
