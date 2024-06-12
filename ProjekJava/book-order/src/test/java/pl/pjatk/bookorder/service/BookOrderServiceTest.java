package pl.pjatk.bookorder.service;

import io.swagger.model.BookOrderRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import pl.pjatk.bookorder.repository.BookToOrderRepository;

import java.io.ByteArrayInputStream;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookOrderServiceTest {

    @Mock
    private BookToOrderRepository bookToOrderRepository;

    @Mock
    private PDFService pdfService;

    @InjectMocks
    private BookOrderService bookOrderService;

    @Test
    void testOrderBooks() {
        BookOrderRequest request1 = new BookOrderRequest();
        request1.setBookId(1L);
        request1.setVisitorCount(50L);
        BookOrderRequest request2 = new BookOrderRequest();
        request2.setBookId(2L);
        request2.setVisitorCount(30L);
        List<BookOrderRequest> orderRequests = Arrays.asList(request1, request2);

        when(bookToOrderRepository.findById(1L)).thenReturn(java.util.Optional.empty());
        when(bookToOrderRepository.findById(2L)).thenReturn(java.util.Optional.empty());

        bookOrderService.orderBooks(orderRequests);

        verify(bookToOrderRepository, times(1)).saveAll(anyList());
        verify(bookToOrderRepository, times(2)).findById(anyLong());
    }

    @Test
    void testPrintOrder() {
        ByteArrayInputStream expectedInputStream = new ByteArrayInputStream(new byte[]{});
        when(pdfService.createPdf()).thenReturn(expectedInputStream);

        ByteArrayInputStream result = bookOrderService.printOrder();

        verify(pdfService, times(1)).createPdf();
        assertEquals(expectedInputStream, result);
    }
}