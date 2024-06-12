package pl.pjatk.bookorder.service;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.pjatk.bookorder.entity.BookToOrder;
import pl.pjatk.bookorder.repository.BookToOrderRepository;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PDFServiceTest {

    @Mock
    private BookToOrderRepository bookToOrderRepository;

    @InjectMocks
    private PDFService pdfService;

    @Test
    void testCreatePdf() throws Exception {
        BookToOrder book1 = new BookToOrder();
        book1.setBookId(1L);
        book1.setQuantityToOrder(10L);

        BookToOrder book2 = new BookToOrder();
        book2.setBookId(2L);
        book2.setQuantityToOrder(5L);

        List<BookToOrder> booksToOrder = Arrays.asList(book1, book2);
        when(bookToOrderRepository.findAll()).thenReturn(booksToOrder);

        ByteArrayInputStream pdfStream = pdfService.createPdf();

        assertNotNull(pdfStream);

        PdfReader reader = new PdfReader(pdfStream);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        pdfStream.transferTo(out);

        String pdfContent = "";
        for (int i = 1; i <= reader.getNumberOfPages(); i++) {
            pdfContent += PdfTextExtractor.getTextFromPage(reader, i);
        }
        reader.close();

        assertTrue(pdfContent.contains("Books to Order"));
        assertTrue(pdfContent.contains("Book ID"));
        assertTrue(pdfContent.contains("Quantity to Order"));
        assertTrue(pdfContent.contains("1"));
        assertTrue(pdfContent.contains("10"));
        assertTrue(pdfContent.contains("2"));
        assertTrue(pdfContent.contains("5"));
    }
}