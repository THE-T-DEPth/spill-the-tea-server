package the_t.mainproject.infrastructure.mail;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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

    public String generatePassword() {
        SecureRandom random = new SecureRandom();
        // 비밀번호 구성 요소
        String upperCaseLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowerCaseLetters = "abcdefghijklmnopqrstuvwxyz";
        String digits = "0123456789";
        String specialCharacters = "!@#$%^&*()_+-={}[]|:;\"'<>,.?/~`";
        // 하나씩 반드시 포함
        char upper = upperCaseLetters.charAt(random.nextInt(upperCaseLetters.length()));
        char lower = lowerCaseLetters.charAt(random.nextInt(lowerCaseLetters.length()));
        char digit = digits.charAt(random.nextInt(digits.length()));
        char special = specialCharacters.charAt(random.nextInt(specialCharacters.length()));
        // 나머지는 랜덤 조합
        String allCharacters = upperCaseLetters + lowerCaseLetters + digits + specialCharacters;
        StringBuilder tempPassword = new StringBuilder();
        tempPassword.append(upper).append(lower).append(digit).append(special);
        for (int i = 4; i < 12; i++) { // 최소 8자리 이상, 최대 12자리
            tempPassword.append(allCharacters.charAt(random.nextInt(allCharacters.length())));
        }
        // 비밀번호를 랜덤하게 섞음
        List<Character> passwordChars = tempPassword.chars()
                .mapToObj(c -> (char) c)
                .collect(Collectors.toList());
        Collections.shuffle(passwordChars);

        return passwordChars.stream()
                .map(String::valueOf)
                .collect(Collectors.joining());
    }
}
