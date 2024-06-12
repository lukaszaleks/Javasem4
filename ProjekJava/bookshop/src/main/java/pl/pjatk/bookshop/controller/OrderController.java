package pl.pjatk.bookshop.controller;

import io.swagger.api.OrderApi;
import io.swagger.model.OrderDTO;
import io.swagger.model.OrderRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import pl.pjatk.bookshop.service.OrderService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class OrderController implements OrderApi {

    private final OrderService orderService;

    @Override
    public ResponseEntity<Void> completeOrder(Long orderId) {
        orderService.completeOrder(orderId);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<List<OrderDTO>> getOrders() {
        return ResponseEntity.ok(orderService.getOrders());
    }

    @Override
    public ResponseEntity<OrderDTO> placeOrder(OrderRequest body) {
        return ResponseEntity.ok(orderService.placeOrder(body));
    }

}
