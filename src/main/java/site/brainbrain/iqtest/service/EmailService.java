package site.brainbrain.iqtest.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.io.ByteArrayOutputStream;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import site.brainbrain.iqtest.domain.PurchaseOption;
import site.brainbrain.iqtest.domain.ScoreResult;
import site.brainbrain.iqtest.domain.dto.BasicEmailDto;
import site.brainbrain.iqtest.exception.BrainBrainMailException;
import site.brainbrain.iqtest.util.MailAttachmentConverter;

@RequiredArgsConstructor
@Service
public class EmailService {

    private static final String MAIL_TITLE = "BrainBrain IQ 테스트 결과";
    private static final String MAIL_BODY = "첨부된 인증서 및 보고서를 확인해주세요.";
    private static final String CERTIFICATE_FILENAME = "%s_certificate.pdf";
    private static final String EXTRA_PAYMENT_MESSAGE = "아래의 링크에서 더 많은 분석 결과를 얻을 수 있습니다."
            + "<ul><li><a href=\"%s\">이곳을 클릭해 추가 분석 보기</a></li></ul>";
    private static final String EXTRA_PAYMENT_URL = "https://brainbrain.site/payment?user_id=%d&purchase_option=%s";

    private final JavaMailSender mailSender;

    public void sendOnlyScore(final BasicEmailDto basicInfo, final ScoreResult scoreResult) {
        try {
            final MimeMessage message = mailSender.createMimeMessage();
            final MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(basicInfo.email());
            helper.setSubject(MAIL_TITLE);
            helper.setText(basicInfo.name() + "님의 IQ 점수는 : " + scoreResult.cattell()); //todo: 점수만 전송할 때 텍스트만? 혹은 이미지로?
            setExtraPayment(basicInfo, helper);

            mailSender.send(message);
        } catch (final Exception e) {
            throw new BrainBrainMailException("이메일 전송에 실패했습니다.");
        }
    }

    public void sendCertificate(final BasicEmailDto basicInfo, final ByteArrayOutputStream certificate) {
        try {
            final MimeMessage message = mailSender.createMimeMessage();
            final MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(basicInfo.email());
            helper.setSubject(MAIL_TITLE);
            helper.setText(MAIL_BODY);
            setExtraPayment(basicInfo, helper);

            final String fileName = String.format(CERTIFICATE_FILENAME, basicInfo.name());
            final ByteArrayResource byteArrayResource = MailAttachmentConverter.toResource(certificate);
            helper.addAttachment(fileName, byteArrayResource, MediaType.APPLICATION_PDF_VALUE);

            mailSender.send(message);
        } catch (final Exception e) {
            throw new BrainBrainMailException("이메일 전송에 실패했습니다.");
        }
    }

    private void setExtraPayment(final BasicEmailDto basicInfo, final MimeMessageHelper helper) throws MessagingException {
        if (basicInfo.purchaseOption() != PurchaseOption.PREMIUM) {
            final String extraPaymentUrl = String.format(EXTRA_PAYMENT_URL, basicInfo.userId(), basicInfo.purchaseOption());
            helper.setText(String.format(EXTRA_PAYMENT_MESSAGE, extraPaymentUrl), true);
        }
    }
}
