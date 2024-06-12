package pl.pjatk.bookorder.controller;

import io.swagger.model.BookOrderRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.pjatk.bookorder.service.BookOrderService;

import java.io.ByteArrayInputStream;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class BookOrderController {

    private final BookOrderService bookOrderService;

    @PostMapping(value = "/order-report ")
    public ResponseEntity<Void> bookOrder(@RequestBody List<BookOrderRequest> orderRequests) {
        bookOrderService.orderBooks(orderRequests);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/print")
    public ResponseEntity<InputStreamResource> getPdf() {
        ByteArrayInputStream bis = bookOrderService.printOrder();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=sample.pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }
}
