package site.brainbrain.iqtest.domain.result;

import java.io.ByteArrayOutputStream;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import site.brainbrain.iqtest.domain.PurchaseOption;
import site.brainbrain.iqtest.domain.dto.BasicEmailDto;
import site.brainbrain.iqtest.domain.dto.ResultStrategyDto;
import site.brainbrain.iqtest.service.CertificateService;
import site.brainbrain.iqtest.service.EmailService;
import site.brainbrain.iqtest.service.ScoreService;

@Component
@RequiredArgsConstructor
public class PremiumResultStrategy implements ResultStrategy {

    private final ScoreService scoreService;
    private final EmailService emailService;
    private final CertificateService certificateService;

    @Override
    public PurchaseOption getPurchaseOption() {
        return PurchaseOption.PREMIUM;
    }

    @Override
    public void createResult(final ResultStrategyDto request) {
        final ByteArrayOutputStream certificate = certificateService.generate(request.name(), request.scoreResult());
        final BasicEmailDto basicEmailDto = new BasicEmailDto(getPurchaseOption(), request.userId(), request.email(), request.name());
        emailService.sendCertificate(basicEmailDto, certificate);
        //todo: 인증서 배송 로직 추가
    }
}
