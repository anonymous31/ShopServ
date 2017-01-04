package ee.ttu.shop.order;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.AbstractView;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;

@Service
public abstract class AbstractITextPdfView extends AbstractView {

	public AbstractITextPdfView() {
		setContentType("application/pdf");
	}

	@Override
	protected boolean generatesDownloadContent() {
		return true;
	}

	protected Document newDocument() {
		return new Document(PageSize.A4);
	}

	protected PdfWriter newWriter(Document document, OutputStream os) throws DocumentException {
		return PdfWriter.getInstance(document, os);
	}

	protected void prepareWriter(Map<String, Object> model, PdfWriter writer, HttpServletRequest request)
			throws DocumentException {

		writer.setViewerPreferences(getViewerPreferences());
	}

	protected int getViewerPreferences() {
		return PdfWriter.ALLOW_PRINTING | PdfWriter.PageLayoutSinglePage;
	}

	protected void buildPdfMetadata(Map<String, Object> model, Document document, HttpServletRequest request) {
	}

	protected Font font10;
	protected Font font10b;
	protected Font font12;
	protected Font font12b;
	protected Font font14;
	protected Font font14b;

	public String convertDate(Timestamp ts, String newFormat) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(newFormat);
		return sdf.format(ts);
	}

	public PdfPCell getClientInfo(String who, String name, String phone) {
		PdfPCell cell = new PdfPCell();
		cell.setBorder(PdfPCell.NO_BORDER);
		cell.addElement(new Paragraph(who, font12b));
		cell.addElement(new Paragraph(name, font12));
		cell.addElement(new Paragraph(phone, font12));
		return cell;
	}

	protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		Order order = (Order) model.get("order");

		BaseFont bf = BaseFont.createFont(BaseFont.TIMES_ITALIC, BaseFont.WINANSI, BaseFont.EMBEDDED);
		BaseFont bfb = BaseFont.createFont(BaseFont.TIMES_BOLDITALIC, BaseFont.WINANSI, BaseFont.EMBEDDED);
		font10 = new Font(bf, 10);
		font10b = new Font(bfb, 10);
		font12 = new Font(bf, 12);
		font12b = new Font(bfb, 12);
		font14 = new Font(bf, 14);
		font14b = new Font(bfb, 14);

		{
			Paragraph p;
			p = new Paragraph("Invoice nr " + order.getId(), font14b);
			p.setAlignment(Element.ALIGN_RIGHT);
			document.add(p);

			PdfPTable table = new PdfPTable(2);
			table.setWidthPercentage(100);
			table.setWidths(new int[] { 7, 2 });
			table.setSpacingBefore(10);
			PdfPCell client = getClientInfo("Client:", order.getFirst_name() + " " + order.getLast_name(),
					order.getPhone() + " " + order.getAddress());
			table.addCell(client);
			PdfPCell cell = new PdfPCell();
			cell.setBorder(PdfPCell.NO_BORDER);
			cell.addElement(new Paragraph("Date:", font12b));
			cell.addElement(new Paragraph(convertDate(order.getCreationDate(), "MM/dd/yyyy"), font12));
			table.addCell(cell);
			document.add(table);
		}

		{
			PdfPTable table = new PdfPTable(4);
			table.setWidthPercentage(100.0f);
			table.setWidths(new float[] { 3.0f, 2.0f, 2.0f, 2.0f });
			table.setSpacingBefore(10);

			Font font = FontFactory.getFont(FontFactory.HELVETICA);
			font.setColor(BaseColor.BLACK);

			PdfPCell cell = new PdfPCell();
			cell.setBackgroundColor(BaseColor.WHITE);
			cell.setPadding(5);

			cell.setPhrase(new Phrase("Item name", font));
			table.addCell(cell);

			cell.setPhrase(new Phrase("Quantity", font));
			table.addCell(cell);

			cell.setPhrase(new Phrase("Price", font));
			table.addCell(cell);

			cell.setPhrase(new Phrase("Cost", font));
			table.addCell(cell);

			for (OrderItem oi : order.getOrderItems()) {
				table.addCell(oi.getProduct().getName());
				table.addCell(Integer.toString(oi.getQty()));
				table.addCell(oi.getPrice().toString());

				table.addCell(oi.getPrice().multiply(new BigDecimal(oi.getQty())).toString());
			}

			document.add(table);
		}

		LineSeparator ls = new LineSeparator();
		document.add(new Chunk(ls));

		{
			PdfPTable table = new PdfPTable(3);
			table.setWidthPercentage(100);
			table.setWidths(new int[] { 5, 1, 1 });
			table.setSpacingBefore(10);

			PdfPCell cell2 = new PdfPCell();
			cell2.setBorder(PdfPCell.NO_BORDER);
			table.addCell(cell2);

			PdfPCell cell = new PdfPCell();
			cell.setBorder(PdfPCell.NO_BORDER);
			cell.addElement(new Paragraph("Total:", font12));
			table.addCell(cell);

			PdfPCell cell1 = new PdfPCell();
			cell1.setBorder(PdfPCell.NO_BORDER);
			BigDecimal subtotal = new BigDecimal("0");
			for (OrderItem oi : order.getOrderItems()) {
				subtotal = subtotal.add(oi.getPrice().multiply(new BigDecimal(oi.getQty())));
			}
			cell1.addElement(new Paragraph(subtotal.toString() + " EUR", font12b));
			table.addCell(cell1);

			document.add(table);
		}

		document.add(Chunk.NEWLINE);
		document.add(Chunk.NEWLINE);
		document.add(Chunk.NEWLINE);

		document.add(new Chunk(ls));

		{
			PdfPTable table = new PdfPTable(3);
			table.setWidthPercentage(100);
			table.setWidths(new int[] { 1, 1, 1 });
			table.setSpacingBefore(10);
			PdfPCell cell1 = new PdfPCell();
			cell1.setBorder(PdfPCell.NO_BORDER);
			cell1.addElement(new Paragraph("Computer parts OÜ", font12));
			cell1.addElement(new Paragraph("10345 Vana-Tallinn 11", font12));
			cell1.addElement(new Paragraph("Tallinn,Estonia", font12));
			cell1.addElement(new Paragraph("Reg nr. :12345678 KMKR: EE123456789", font12));
			table.addCell(cell1);
			PdfPCell cell = new PdfPCell();
			cell.setBorder(PdfPCell.NO_BORDER);
			cell.addElement(new Paragraph("Tel. (+372) 1234567", font12));
			cell.addElement(new Paragraph("E-post: noreply@junk.net", font12));
			table.addCell(cell);

			PdfPCell cell2 = new PdfPCell();
			cell2.setBorder(PdfPCell.NO_BORDER);
			cell2.addElement(new Paragraph("a/a FR7618206000103056", font12));
			cell2.addElement(new Paragraph("SWEDBANK", font12));
			table.addCell(cell2);

			document.add(table);
		}

	}
}
