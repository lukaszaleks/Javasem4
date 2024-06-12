package pl.pjatk.bookshop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.pjatk.bookshop.service.BookService;

@RestController
@RequiredArgsConstructor
public class BookReportController {

    private final BookService bookService;

    @GetMapping(value = "/order-report ")
    public ResponseEntity<Void> orderBooks() {
        bookService.orderBooks();
        return ResponseEntity.ok().build();
    }
}
