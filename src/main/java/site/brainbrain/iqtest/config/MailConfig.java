//package site.brainbrain.iqtest.config;
//
//import java.util.Properties;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.mail.javamail.JavaMailSenderImpl;
//
//@Configuration
//public class MailConfig {
//
//    private static final String SMTP_HOST = "smtp.gmail.com";
//    private static final int SMTP_PORT = 587;
//
//    private static final String MAIL_SMTP_AUTH = "mail.smtp.auth";
//    private static final String MAIL_SMTP_STARTTLS_ENABLE = "mail.smtp.starttls.enable";
//    private static final String MAIL_SMTP_DEBUG = "mail.smtp.debug";
//
//    private static final String MAIL_SMTP_CONNECTION_TIMEOUT = "mail.smtp.connectiontimeout";
//    private static final String MAIL_SMTP_TIMEOUT = "mail.smtp.timeout";
//    private static final String MAIL_SMTP_WRITE_TIMEOUT = "mail.smtp.writetimeout";
//    private static final String TIMEOUT_DURATION = "10000";
//
//
//    @Value("${spring.mail.username}")
//    private String username;
//
//    @Value("${spring.mail.password}")
//    private String password;
//
//    @Bean
//    public JavaMailSender javaMailService() {
//        final JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
//        javaMailSender.setHost(SMTP_HOST);
//        javaMailSender.setPort(SMTP_PORT);
//        javaMailSender.setUsername(username);
//        javaMailSender.setPassword(password);
//        javaMailSender.setDefaultEncoding("UTF-8");
//
//        final Properties properties = javaMailSender.getJavaMailProperties();
//        properties.put(MAIL_SMTP_AUTH, true);
//        properties.put(MAIL_SMTP_STARTTLS_ENABLE, true);
//        properties.put(MAIL_SMTP_DEBUG, true);
//        properties.put(MAIL_SMTP_CONNECTION_TIMEOUT, TIMEOUT_DURATION);
//        properties.put(MAIL_SMTP_TIMEOUT, TIMEOUT_DURATION);
//        properties.put(MAIL_SMTP_WRITE_TIMEOUT, TIMEOUT_DURATION);
//
//        javaMailSender.setJavaMailProperties(properties);
//        return javaMailSender;
//    }
//}
