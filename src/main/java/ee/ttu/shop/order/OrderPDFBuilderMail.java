package ee.ttu.shop.order;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfWriter;

@Service
public class OrderPDFBuilderMail extends AbstractITextPdfView {

	@Autowired
	private JavaMailSender mailSender;

	@Override
	protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		ByteArrayOutputStream baos = createTemporaryOutputStream();
		baos.toString("UTF-8");

		Order order1 = (Order) model.get("order");

		Document document = newDocument();
		PdfWriter writer = newWriter(document, baos);
		prepareWriter(model, writer, request);
		buildPdfMetadata(model, document, request);

		document.open();
		buildPdfDocument(model, document, writer, request, response);
		document.close();
		FileOutputStream fos = null;
		File temp = File.createTempFile("temp-file-name", ".tmp");
		;
		try {

			fos = new FileOutputStream(temp);

			baos.writeTo(fos);
		} catch (IOException ioe) {

			ioe.printStackTrace();
		} finally {
			fos.close();
		}
		Order order = (Order) model.get("order");

		mailSender.send(new MimeMessagePreparator() {

			@Override
			public void prepare(MimeMessage mimeMessage) throws Exception {
				MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8");
				message.setTo(order.getEmail());
				message.setSubject("Order nr " + order.getId());
				message.setText("<b>Pay time</b>", true);
				message.addAttachment("Order" + order.getId() + ".pdf", temp);
			}
		});

	}
}
