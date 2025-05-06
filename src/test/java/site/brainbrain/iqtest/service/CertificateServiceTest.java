package site.brainbrain.iqtest.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.io.ByteArrayOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class CertificateServiceTest {

    @Autowired
    private CertificateService certificateService;

    @Test
    @DisplayName("인증서 생성 테스트")
    void createCert() {

        // 파일 잘 나오는지는 두눈으로 직접 확인하기,,ㅋㅋ
        assertDoesNotThrow(() -> {
            ByteArrayOutputStream pdf = certificateService.generate("Monkey D. Luffy", 111);
            Files.createDirectories(Path.of("src/test/resources/static"));
            Files.write(Path.of("src/test/resources/static/output.pdf"), pdf.toByteArray());
        });
    }

}
