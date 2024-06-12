package pl.pjatk.bookshop.service;

import io.swagger.model.BookDTO;
import io.swagger.model.BookOrderRequest;
import io.swagger.model.BookRequest;
import io.swagger.model.NewBookRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.pjatk.bookshop.config.exception.BookNotFoundException;
import pl.pjatk.bookshop.config.exception.SearchParamsException;
import pl.pjatk.bookshop.controller.OrderBooks;
import pl.pjatk.bookshop.entity.Book;
import pl.pjatk.bookshop.entity.BookVisitor;
import pl.pjatk.bookshop.mapper.BookMapper;
import pl.pjatk.bookshop.repository.BookRepository;
import pl.pjatk.bookshop.repository.BookVisitorRepository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookService {
    private final BookRepository bookRepository;
    private final BookVisitorRepository bookVisitorRepository;
    private final BookMapper bookMapper;
    private final OrderBooks orderBooks;

    public List<BookDTO> getAllBooks() {
        log.info("Fetching all books");
        return bookRepository.findAll().stream()
                .map(bookMapper::convertToBookDTO)
                .toList();
    }

    @Transactional
    public BookDTO addBook(NewBookRequest body) {
        log.info("Adding a new book");
        Book book = bookMapper.createBook(body);
        book.setVisitor(createNewBookVisitor(book));
        return bookMapper.convertToBookDTO(bookRepository.save(book));
    }

    public BookDTO getBookById(Long bookId) {
        log.info("Fetching book with id: {}", bookId);
        Book foundBook = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException("Cannot find book with id: " + bookId));
        increaseVisitors(foundBook);
        return bookMapper.convertToBookDTO(foundBook);
    }

    @Transactional
    public BookDTO updateBook(Long bookId, BookRequest body) {
        log.info("Updating book with id: {}", bookId);
        Book existingBook = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException("Cannot find book with id: " + bookId));

        Book updatedBook = bookMapper.update(body, existingBook);

        return bookMapper.convertToBookDTO(bookRepository.save(updatedBook));
    }

    public void orderBooks() {
        log.info("Ordering books");
        List<BookOrderRequest> bookOrderRequests = createBookOrderRequests();
        sendBookOrderRequests(bookOrderRequests);
        clearVisitors(bookOrderRequests);
    }

    private List<BookOrderRequest> createBookOrderRequests() {
        List<Book> allBooksWithVisitors = bookRepository.findAllBooksWithVisitors();
        return allBooksWithVisitors.stream()
                .map(this::mapToBookOrderRequest)
                .toList();
    }

    private BookOrderRequest mapToBookOrderRequest(Book book) {
        return bookMapper.convertToBookOrderRequest(book);
    }

    private void sendBookOrderRequests(List<BookOrderRequest> bookOrderRequests) {
        orderBooks.orderBooks(bookOrderRequests);
    }

    private void clearVisitors(List<BookOrderRequest> bookOrderRequests) {
        Set<Long> bookIdsToClear = bookOrderRequests.stream()
                .map(BookOrderRequest::getBookId)
                .collect(Collectors.toSet());
        bookVisitorRepository.clearVisitors(bookIdsToClear);
    }

    private void increaseVisitors(Book book) {
        BookVisitor bookVisitor = bookVisitorRepository.findByBook_BookId(book.getBookId()).orElse(createNewBookVisitor(book));
        bookVisitor.setVisitorsCount(bookVisitor.getVisitorsCount() + 1);
        bookVisitorRepository.save(bookVisitor);
    }

    private BookVisitor createNewBookVisitor(Book book) {
        BookVisitor bookVisitor = new BookVisitor();
        bookVisitor.setBook(book);
        bookVisitor.setVisitorsCount(0L);
        return bookVisitor;
    }

    public List<BookDTO> searchBook(String title, String bookType) {
        if (!isParamsValidated(title, bookType)) {
            throw new SearchParamsException("Both parameters cannot be empty!");
        }
        List<Book> foundBooks = bookRepository.findAllByParams(title, bookType);
        return foundBooks.stream()
                .map(book -> {
                    increaseVisitors(book);
                    return bookMapper.convertToBookDTO(book);
                }).toList();
    }

    private boolean isParamsValidated(String title, String bookType) {
        return !(title.isEmpty() && bookType.isEmpty());
    }

    @Transactional
    public void deleteBook(Long bookId) {
        log.info("Deleting book with id: {}", bookId);
        Book existingBook = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException("Cannot find book with id: " + bookId));

        bookVisitorRepository.deleteAllByBook_BookId(bookId);
        bookRepository.delete(existingBook);
    }
}
