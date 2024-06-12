package pl.pjatk.bookorder.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.pjatk.bookorder.config.BookToOrderNotFoundException;
import pl.pjatk.bookorder.entity.BookToOrder;
import pl.pjatk.bookorder.repository.BookToOrderRepository;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PDFService {

    private final BookToOrderRepository bookToOrderRepository;

    public ByteArrayInputStream createPdf() {
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            addTitle(document);
            addHeader(document);
            addTable(document);

            document.close();

        } catch (DocumentException e) {
            log.error("Error occurred while creating PDF: {}", e.getMessage(), e);
        }

        return new ByteArrayInputStream(out.toByteArray());
    }

    private void addTitle(Document document) throws DocumentException {
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
        Paragraph title = new Paragraph("Books to Order", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);
    }

    private void addHeader(Document document) throws DocumentException {
        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
        Paragraph header = new Paragraph("Below is the list to order:", headerFont);
        header.setSpacingBefore(20);
        header.setSpacingAfter(10);
        document.add(header);
    }

    private void addTable(Document document) throws DocumentException {
        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
        PdfPTable table = createTable(headerFont);

        List<BookToOrder> booksToOrder = bookToOrderRepository.findAll();
        if (booksToOrder.isEmpty()) {
            throw new BookToOrderNotFoundException("Cannot find books to order");
        }
        populateTableWithData(table, booksToOrder);

        document.add(table);
    }

    private PdfPTable createTable(Font headerFont) {
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10);
        table.setSpacingAfter(10);

        addTableHeader(table, headerFont);

        return table;
    }

    private void addTableHeader(PdfPTable table, Font headerFont) {
        PdfPCell cell;
        cell = new PdfPCell(new Phrase("Book ID", headerFont));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(5);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Quantity to Order", headerFont));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(5);
        table.addCell(cell);
    }

    private void populateTableWithData(PdfPTable table, List<BookToOrder> booksToOrder) {
        booksToOrder.forEach(bookToOrder -> {
            table.addCell(String.valueOf(bookToOrder.getBookId()));
            table.addCell(String.valueOf(bookToOrder.getQuantityToOrder()));
        });
    }

}
