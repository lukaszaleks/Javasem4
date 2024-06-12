package pl.pjatk.bookshop.controller;

import io.swagger.api.BookApi;
import io.swagger.model.BookDTO;
import io.swagger.model.BookRequest;
import io.swagger.model.NewBookRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.pjatk.bookshop.service.BookService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BookController implements BookApi {
    private final BookService bookService;

    @Override
    public ResponseEntity<List<BookDTO>> getBooks() {
        return ResponseEntity.ok(bookService.getAllBooks());
    }

    @Override
    public ResponseEntity<BookDTO> addBook(NewBookRequest body) {
        return ResponseEntity.ok(bookService.addBook(body));
    }

    @Override
    public ResponseEntity<BookDTO> getBookById(Long bookId) {
        return ResponseEntity.ok(bookService.getBookById(bookId));
    }

    @Override
    public ResponseEntity<BookDTO> updateBook(Long bookId, BookRequest body) {
        return ResponseEntity.ok(bookService.updateBook(bookId, body));
    }

    @Override
    public ResponseEntity<Void> deleteBook(@PathVariable Long bookId) {
        bookService.deleteBook(bookId);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<List<BookDTO>> searchBookBySearchParams(@RequestParam(required = false) String title,
                                                                  @RequestParam(required = false) String bookType) {
        return ResponseEntity.ok(bookService.searchBook(title, bookType));
    }
}
