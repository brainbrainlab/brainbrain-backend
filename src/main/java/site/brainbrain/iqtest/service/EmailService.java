package site.brainbrain.iqtest.service;

import java.io.ByteArrayOutputStream;

import jakarta.mail.internet.MimeMessage;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import site.brainbrain.iqtest.domain.ScoreResult;
import site.brainbrain.iqtest.exception.BrainBrainMailException;
import site.brainbrain.iqtest.util.MailAttachmentConverter;

@Slf4j
@RequiredArgsConstructor
@Service
public class EmailService {

    private static final String MAIL_TITLE = "BrainBrain IQ 테스트 결과";
    private static final String MAIL_BODY = "첨부된 인증서 및 보고서를 확인해주세요.";
    private static final String CERTIFICATE_FILENAME = "%s_certificate.pdf";

    private final JavaMailSender mailSender;

    public void sendOnlyScore(final String email, final String name, final ScoreResult scoreResult) {
        try {
            final MimeMessage message = mailSender.createMimeMessage();
            final MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(email);
            helper.setSubject(MAIL_TITLE);
            helper.setText(name + "님의 IQ 점수는 : " + scoreResult.cattell()); //todo: 점수만 전송할 때 텍스트만? 혹은 이미지로?

            mailSender.send(message);
        } catch (final Exception e) {
            log.error("이메일 점수 전송 중 예외 발생 : {}", e.getMessage());
            throw new BrainBrainMailException("이메일 전송에 실패했습니다.");
        }
    }

    public void sendCertificate(final String email, final String name, final ByteArrayOutputStream certificate) {
        try {
            final MimeMessage message = mailSender.createMimeMessage();
            final MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(email);
            helper.setSubject(MAIL_TITLE);
            helper.setText(MAIL_BODY);

            final String fileName = String.format(CERTIFICATE_FILENAME, name);
            final ByteArrayResource byteArrayResource = MailAttachmentConverter.toResource(certificate);
            helper.addAttachment(fileName, byteArrayResource, MediaType.APPLICATION_PDF_VALUE);

            mailSender.send(message);
        } catch (final Exception e) {
            log.error("이메일 인증서 전송 중 예외 발생 : {}", e.getMessage());
            throw new BrainBrainMailException("이메일 전송에 실패했습니다.");
        }
    }
}
