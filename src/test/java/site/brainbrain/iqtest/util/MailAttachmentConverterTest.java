package site.brainbrain.iqtest.util;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ByteArrayResource;

public class MailAttachmentConverterTest {

    @Test
    @DisplayName("메일 첨부용 리소스로 변환한다.")
    void toResource() throws IOException {
        // given
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write("test".getBytes());

        // when
        final ByteArrayResource resource = MailAttachmentConverter.toResource(baos);

        // then
        assertThat(resource.getByteArray()).isEqualTo(baos.toByteArray());
    }
}
