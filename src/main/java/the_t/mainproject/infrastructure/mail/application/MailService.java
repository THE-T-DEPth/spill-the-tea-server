package the_t.mainproject.infrastructure.mail.application;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import the_t.mainproject.global.common.Message;
import the_t.mainproject.global.common.SuccessResponse;
import the_t.mainproject.infrastructure.mail.dto.MailCodeRes;
import the_t.mainproject.infrastructure.redis.RedisUtil;

import java.security.SecureRandom;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MailService {

    private final String SUBJECT = "SpillTheTea 인증 링크입니다.";
    private final String SENDER_NAME = "SpillTheTea";

    @Value("${spring.mail.username}")
    private String fromEmail;

    private final JavaMailSender mailSender;
    private final RedisUtil redisUtil;

    public SuccessResponse<MailCodeRes> sendMail(String email) throws Exception {
        String code = generateCode();
        redisUtil.setDataExpire(code, String.valueOf(false), 60*3L);
        MimeMessage mimeMessage = createMessage(code, email);
        mailSender.send(mimeMessage);

        MailCodeRes mailCodeRes = MailCodeRes.builder()
                .code(code)
                .build();

        return SuccessResponse.of(mailCodeRes);
    }

    public SuccessResponse<Message> verifyCode(String code) {
        String data = redisUtil.getData(code);
        if (data == null)
            throw new IllegalArgumentException("유효하지 않은 코드입니다.");

        redisUtil.setDataExpire(code, String.valueOf(true), 60 * 60L);

        Message message = Message.builder()
                .message("이메일 인증이 완료되었습니다.")
                .build();

        return SuccessResponse.of(message);
    }

    private MimeMessage createMessage(String code, String email) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
        helper.setTo(email);
        helper.setSubject(SUBJECT);
        helper.setFrom(fromEmail);
        helper.setText(code, true);

        return mimeMessage;
    }


    // Description : 코드 생성 함수 (00000000 ~ zzzzzzzz) (8자리)
    private String generateCode() {
        SecureRandom random = new SecureRandom();
        // nextLong(long bound) : 0(포함)부터 입력된 bound(미포함) 사이의 랜덤 정수를 반환
        long randomNumber = random.nextLong(2821109907455L + 1);
        String code = Long.toString(randomNumber, 36);
        code = String.format("%8s", code).replace(' ', '0');

        return code;
    }
}
