package pl.pjatk.bookshop.controller;

import io.swagger.model.BookOrderRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "bookOrder", url = "localhost:8090")
public interface OrderBooks {

    @PostMapping("/book-report")
    ResponseEntity<Void> orderBooks(@RequestBody List<BookOrderRequest> orderRequests);
}
