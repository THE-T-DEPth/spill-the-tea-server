package the_t.mainproject.infrastructure.mail.application;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import the_t.mainproject.domain.member.domain.Member;
import the_t.mainproject.domain.member.domain.repository.MemberRepository;
import the_t.mainproject.global.common.Message;
import the_t.mainproject.global.common.SuccessResponse;
import the_t.mainproject.infrastructure.mail.MailUtil;
import the_t.mainproject.infrastructure.mail.dto.MailCodeRes;
import the_t.mainproject.infrastructure.mail.dto.TempPasswordRes;
import the_t.mainproject.infrastructure.redis.RedisUtil;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MailService {

    private final String VERIFY_SUFFIX = "_verify";
    private final String PASSWORD_SUFFIX = "_password";

    private final JavaMailSender mailSender;
    private final RedisUtil redisUtil;
    private final MailUtil mailUtil;
    private final PasswordEncoder passwordEncoder;

    private final MemberRepository memberRepository;

    // 이메일 인증을 위한 인증번호 메일 발송
    public SuccessResponse<MailCodeRes> sendVerifyMail(String email) throws Exception {
        if(memberRepository.existsByEmail(email))
            throw new IllegalArgumentException("이미 가입된 이메일입니다.");

        MailCodeRes mailCodeRes = sendCode(email);

        return SuccessResponse.of(mailCodeRes);
    }

    // 이메일 인증번호 검증
    public SuccessResponse<Message> verifyEmailCode(String code) {
        String data = redisUtil.getData(code);
        if (data == null)
            throw new IllegalArgumentException("인증번호가 동일하지 않습니다.");

        redisUtil.deleteData(code);
        redisUtil.setDataExpire(data + VERIFY_SUFFIX, String.valueOf(true), 60 * 5L);

        Message message = Message.builder()
                .message("이메일 인증이 완료되었습니다.")
                .build();

        return SuccessResponse.of(message);
    }

    // 비밀번호 찾기를 위한 인증번호 메일 발송
    public SuccessResponse<MailCodeRes> sendPasswordMail(String email) throws Exception {
        if(!memberRepository.existsByEmail(email))
            throw new IllegalArgumentException("가입되지 않은 이메일입니다.");

        MailCodeRes mailCodeRes = sendCode(email);

        return SuccessResponse.of(mailCodeRes);
    }

    // 비밀번호 변경 인증번호 검증
    @Transactional
    public SuccessResponse<TempPasswordRes> passwordEmailCode(String code) {
        String email = redisUtil.getData(code);
        if (email == null)
            throw new IllegalArgumentException("인증번호가 동일하지 않습니다.");

        redisUtil.deleteData(code);
        redisUtil.setDataExpire(email + PASSWORD_SUFFIX, String.valueOf(true), 60 * 5L);

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("해당 이메일의 유저를 찾을 수 없습니다."));

        String tempPassword = mailUtil.generatePassword();
        member.updatePassword(passwordEncoder.encode(tempPassword));

        TempPasswordRes tempPasswordRes = TempPasswordRes.builder()
                .temp_password(tempPassword)
                .build();

        return SuccessResponse.of(tempPasswordRes);
    }

    // 인증번호 전송 공통 메서드
    private MailCodeRes sendCode(String email) throws Exception {
        String code = mailUtil.generateCode();
        redisUtil.setDataExpire(code, email, 60 * 3L);

        MimeMessage mimeMessage = mailUtil.createMessage(code, email);
        mailSender.send(mimeMessage);

        return MailCodeRes.builder()
                .code(code)
                .build();
    }
}
