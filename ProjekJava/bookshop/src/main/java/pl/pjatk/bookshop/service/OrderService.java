package pl.pjatk.bookshop.service;

import io.swagger.model.OrderDTO;
import io.swagger.model.OrderRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.pjatk.bookshop.config.exception.OrderNotFoundException;
import pl.pjatk.bookshop.entity.Order;
import pl.pjatk.bookshop.mapper.OrderMapper;
import pl.pjatk.bookshop.repository.OrderRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    @Transactional
    public void completeOrder(Long orderId) {
        log.info("Completing order with id: {}", orderId);
        Order existingOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Cannot find order with id: " + orderId));

        existingOrder.setPaid(true);
        orderRepository.save(existingOrder);
        log.info("Order with id: {} has been marked as paid", orderId);
    }

    public List<OrderDTO> getOrders() {
        log.info("Fetching all orders");
        return orderRepository.findAll()
                .stream()
                .map(orderMapper::convertToOrderDTO)
                .toList();
    }

    @Transactional
    public OrderDTO placeOrder(OrderRequest body) {
        log.info("Placing a new order");
        Order newOrder = orderMapper.convertFromOrderRequest(body);
        Order savedOrder = orderRepository.save(newOrder);
        log.info("New order has been placed with id: {}", savedOrder.getOrderId());
        return orderMapper.convertToOrderDTO(savedOrder);
    }

}
