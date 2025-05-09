package site.brainbrain.iqtest.util;

import java.io.ByteArrayOutputStream;

import org.springframework.core.io.ByteArrayResource;

public class MailAttachmentConverter {

    public static ByteArrayResource toResource(final ByteArrayOutputStream certificate) {
        return new ByteArrayResource(certificate.toByteArray());
    }
}
