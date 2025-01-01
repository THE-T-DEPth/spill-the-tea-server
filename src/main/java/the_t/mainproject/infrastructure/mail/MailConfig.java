package the_t.mainproject.infrastructure.mail;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailConfig {

    @Value("${spring.mail.host}")
    private String mailServerHost;

    @Value("${spring.mail.port}")
    private int mailServerPort;

    @Value("${spring.mail.username}")
    private String mailServerUsername;

    @Value("${spring.mail.password}")
    private String mailServerPassword;

    @Value("${spring.mail.properties.mail.debug}")
    private boolean mailDebug;

    @Value("${spring.mail.properties.mail.smtp.auth}")
    private boolean auth;

    @Value("${spring.mail.properties.mail.smtp.starttls.enable}")
    private boolean starttlsEnable;

    @Value("${spring.mail.properties.mail.smtp.starttls.required}")
    private boolean starttlsRequired;

    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(mailServerHost);
        mailSender.setPort(mailServerPort);
        mailSender.setUsername(mailServerUsername);
        mailSender.setPassword(mailServerPassword);
        mailSender.setJavaMailProperties(getMailProperties());

        return mailSender;
    }

    private Properties getMailProperties() {
        Properties properties = new Properties();
        properties.put("mail.transport.protocol", "smtp");
        properties.put("mail.debug", mailDebug);
        properties.put("mail.smtp.auth", auth);
        properties.put("mail.smtp.starttls.enable", starttlsEnable);
        properties.put("mail.smtp.starttls.required", starttlsRequired);

        return properties;
    }
}
