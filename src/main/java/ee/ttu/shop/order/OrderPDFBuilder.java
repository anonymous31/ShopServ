package ee.ttu.shop.order;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfWriter;

public class OrderPDFBuilder extends AbstractITextPdfView{
	
	@Override
    protected void renderMergedOutputModel(Map<String, Object> model,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        ByteArrayOutputStream baos = createTemporaryOutputStream();
       
        Document document = newDocument();
        PdfWriter writer = newWriter(document, baos);
        prepareWriter(model, writer, request);
        buildPdfMetadata(model, document, request);
 

        document.open();
        buildPdfDocument(model, document, writer, request, response);
        document.close();

        writeToResponse(response, baos);
    }
	
	

}
