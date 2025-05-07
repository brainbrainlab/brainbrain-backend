package site.brainbrain.iqtest.service;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDPageContentStream.AppendMode;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

@Service
public class CertificateService {

    private static final Color GRAY = new Color(0x666666);

    public ByteArrayOutputStream generate(String name, Integer score) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            String path = new ClassPathResource("/static/certification.pdf").getFile().getAbsolutePath();
            PDDocument doc = PDDocument.load(new File(path));
            writeDateAndCertificateNo(doc);
            writeName(doc, name);
            writeScore(doc, score);

            doc.save(outputStream);
            doc.close();
            return outputStream;
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private PDPageContentStream generateStream(PDDocument doc) throws IOException {
        PDPage page = doc.getPage(0);
        return new PDPageContentStream(doc, page, AppendMode.APPEND, true);
    }

    private void writeDateAndCertificateNo(PDDocument doc) throws IOException {
        PDPageContentStream contentStream = generateStream(doc);
        contentStream.beginText();
        InputStream gilda = getClass().getResourceAsStream("/static/fonts/GildaDisplay-Regular.ttf");
        PDType0Font dateAndCertFont = PDType0Font.load(doc, gilda);

        contentStream.setFont(dateAndCertFont, 13);
        contentStream.setStrokingColor(GRAY);
        contentStream.newLineAtOffset(134, 142);
        // FIXME 이거 유저의 날짜 정보 가져와야 하나.
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
        contentStream.showText(date);

        // TODO 이 인증번호 저장해야 하나. 만약 저장해야 한다면 이 클래스에서 만들게 아니라 상위에서 만들고 전달해줘야 하나?
        contentStream.newLineAtOffset(0, -38);
        String certNo = UUID.randomUUID().toString().substring(14);
        contentStream.showText(certNo);

        contentStream.endText();
        contentStream.close();
    }

    private void writeName(PDDocument doc, String name) throws IOException {
        PDPageContentStream contentStream = generateStream(doc);

        contentStream.beginText();
        InputStream exmouthFont = getClass().getResourceAsStream("/static/fonts/exmouth_.ttf");
        PDType0Font nameAndScoreFont = PDType0Font.load(doc, exmouthFont);

        contentStream.setFont(nameAndScoreFont, 70);
        //FIXME: 색상 미정
        contentStream.setNonStrokingColor(Color.BLACK);

        // 페이지의 크기 가져오기
        PDPage page = doc.getPage(0);
        float pageWidth = page.getMediaBox().getWidth();

        // 이름의 너비 계산
        float nameWidth = nameAndScoreFont.getStringWidth(name) / 1000 * 70;

        // 이름을 정 중앙에 배치
        float xPosition = (pageWidth - nameWidth) / 2;

        contentStream.newLineAtOffset(xPosition, 310);
        contentStream.showText(name);

        contentStream.endText();
        contentStream.close();
    }

    private void writeScore(PDDocument doc, Integer score) throws IOException {
        PDPageContentStream contentStream = generateStream(doc);

        contentStream.beginText();
        InputStream exmouthFont = getClass().getResourceAsStream("/static/fonts/exmouth_.ttf");
        PDType0Font nameAndScoreFont = PDType0Font.load(doc, exmouthFont);

        contentStream.setFont(nameAndScoreFont, 80);
        // FIXME 색상 미정
        contentStream.setNonStrokingColor(Color.BLACK);

        contentStream.newLineAtOffset(390, 202);
        contentStream.showText(String.valueOf(score));

        contentStream.endText();
        contentStream.close();
    }
}
