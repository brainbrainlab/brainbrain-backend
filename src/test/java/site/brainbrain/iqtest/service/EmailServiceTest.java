package site.brainbrain.iqtest.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import com.icegreen.greenmail.junit5.GreenMailExtension;
import com.icegreen.greenmail.store.FolderException;
import com.icegreen.greenmail.util.GreenMailUtil;
import com.icegreen.greenmail.util.ServerSetupTest;

import site.brainbrain.iqtest.exception.BrainBrainMailException;

class EmailServiceTest {

    @RegisterExtension
    static GreenMailExtension greenMail = new GreenMailExtension(ServerSetupTest.SMTP).withPerMethodLifecycle(false);
    private EmailService emailService;

    @BeforeEach
    void setUp() throws FolderException {
        greenMail.purgeEmailFromAllMailboxes();
        final JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setPort(greenMail.getSmtp().getPort());
        emailService = new EmailService(mailSender);
    }

    @Test
    @DisplayName("메일 전송에 성공한다.")
    void send_email_success() throws MessagingException, IOException {
        // given
        final String email = "test@example.com";
        final String name = "tester name";
        final ByteArrayOutputStream pdf = new ByteArrayOutputStream();
        pdf.write("test certificate".getBytes(StandardCharsets.UTF_8));

        // when
        emailService.send(email, name, pdf);

        // then
        assertThat(greenMail.waitForIncomingEmail(5000, 1)).isTrue();

        final MimeMessage message = greenMail.getReceivedMessages()[0];
        assertThat(message.getSubject()).isEqualTo("BrainBrain IQ 테스트 결과");
        assertThat(message.getAllRecipients()[0].toString()).isEqualTo(email);

        final String body = GreenMailUtil.getBody(message);
        assertThat(body).contains(String.format("%s_certificate.pdf", name));
    }

    @Test
    @DisplayName("메일 전송에 실패하면 예외가 발생한다.")
    void send_email_fail() {
        // given
        final JavaMailSender failMailSender = mock(JavaMailSender.class);
        final EmailService failEmailService = new EmailService(failMailSender);
        when(failMailSender.createMimeMessage()).thenThrow(new RuntimeException());

        final ByteArrayOutputStream pdf = new ByteArrayOutputStream();

        // when & then
        assertThatThrownBy(() -> failEmailService.send("test@example.com", "tester", pdf))
                .isInstanceOf(BrainBrainMailException.class)
                .hasMessageContaining("이메일 전송에 실패했습니다.");
    }
}
