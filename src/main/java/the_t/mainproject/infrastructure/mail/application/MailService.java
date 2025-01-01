package the_t.mainproject.infrastructure.mail.application;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import the_t.mainproject.domain.member.domain.repository.MemberRepository;
import the_t.mainproject.global.common.SuccessResponse;
import the_t.mainproject.infrastructure.mail.MailUtil;
import the_t.mainproject.infrastructure.mail.dto.MailCodeRes;
import the_t.mainproject.infrastructure.redis.RedisUtil;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MailService {

    private final String VERIFY_SUFFIX = "_verify";

    private final JavaMailSender mailSender;
    private final RedisUtil redisUtil;
    private final MailUtil mailUtil;

    private final MemberRepository memberRepository;

    public SuccessResponse<MailCodeRes> sendMail(String email) throws Exception {
        if(memberRepository.existsByEmail(email))
            throw new IllegalArgumentException("이미 가입된 이메일입니다.");
        String code = mailUtil.generateCode();
        redisUtil.setDataExpire(code, email, 60 * 3L);

        MimeMessage mimeMessage = mailUtil.createMessage(code, email);
        mailSender.send(mimeMessage);

        MailCodeRes mailCodeRes = MailCodeRes.builder()
                .code(code)
                .build();

        return SuccessResponse.of(mailCodeRes);
    }

    public void verifyCode(String code) {
        String data = redisUtil.getData(code);
        if (data == null)
            throw new IllegalArgumentException("유효하지 않은 코드입니다.");

        redisUtil.deleteData(code);
        redisUtil.setDataExpire(data + VERIFY_SUFFIX, String.valueOf(true), 60 * 60L);
//
//        Message message = Message.builder()
//                .message("이메일 인증이 완료되었습니다.")
//                .build();
//
//        return SuccessResponse.of(message);
    }

}
