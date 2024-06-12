package pl.pjatk.bookorder.service;

import io.swagger.model.BookOrderRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.pjatk.bookorder.entity.BookToOrder;
import pl.pjatk.bookorder.repository.BookToOrderRepository;

import java.io.ByteArrayInputStream;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookOrderService {

    private final BookToOrderRepository bookToOrderRepository;
    private final PDFService pdfService;

    public void orderBooks(List<BookOrderRequest> orderRequests) {
        log.info("Ordering books based on the given requests");
        List<BookToOrder> booksToOrder = mapToBookToOrders(orderRequests);
        saveBookOrders(booksToOrder);
    }

    public ByteArrayInputStream printOrder() {
        log.info("Generating PDF for book orders");
        return pdfService.createPdf();
    }

    private List<BookToOrder> mapToBookToOrders(List<BookOrderRequest> orderRequests) {
        return orderRequests.stream()
                .map(this::findOrCreateBookToOrder)
                .toList();
    }

    private BookToOrder findOrCreateBookToOrder(BookOrderRequest orderRequest) {
        return bookToOrderRepository.findById(orderRequest.getBookId())
                .map(existingOrder -> updateBookToOrder(existingOrder, orderRequest))
                .orElseGet(() -> createBookToOrder(orderRequest));
    }

    private BookToOrder updateBookToOrder(BookToOrder existingOrder, BookOrderRequest orderRequest) {
        existingOrder.setQuantityToOrder(existingOrder.getQuantityToOrder() + calculateBooksToOrder(orderRequest));
        return existingOrder;
    }

    private BookToOrder createBookToOrder(BookOrderRequest orderRequest) {
        BookToOrder bookToOrder = new BookToOrder();
        bookToOrder.setBookId(orderRequest.getBookId());
        bookToOrder.setQuantityToOrder(calculateBooksToOrder(orderRequest));
        return bookToOrder;
    }

    private void saveBookOrders(List<BookToOrder> booksToOrder) {
        log.info("Saving {} book orders to the repository", booksToOrder.size());
        bookToOrderRepository.saveAll(booksToOrder);
    }

    private Long calculateBooksToOrder(BookOrderRequest bookOrderRequest) {
        return bookOrderRequest.getVisitorCount() / 10;
    }
}
