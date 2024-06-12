package pl.pjatk.bookshop.service;

import io.swagger.model.OrderDTO;
import io.swagger.model.OrderRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.pjatk.bookshop.config.exception.OrderNotFoundException;
import pl.pjatk.bookshop.entity.Order;
import pl.pjatk.bookshop.mapper.OrderMapper;
import pl.pjatk.bookshop.repository.OrderRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderMapper orderMapper;

    @InjectMocks
    private OrderService orderService;

    @Test
    void completeOrder_orderExists_orderMarkedAsPaid() {
        // Given
        Long orderId = 1L;
        Order existingOrder = new Order();
        existingOrder.setOrderId(orderId);
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(existingOrder));

        // When
        orderService.completeOrder(orderId);

        // Then
        assertTrue(existingOrder.isPaid());
        verify(orderRepository, times(1)).save(existingOrder);
    }

    @Test
    void completeOrder_orderNotFound_exceptionThrown() {
        // Given
        Long orderId = 1L;
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(OrderNotFoundException.class, () -> orderService.completeOrder(orderId));
        verify(orderRepository, never()).save(any());
    }

    @Test
    void getOrders_ordersExist_ordersReturned() {
        // Given
        Order order1 = new Order();
        Order order2 = new Order();
        when(orderRepository.findAll()).thenReturn(Arrays.asList(order1, order2));

        // When
        List<OrderDTO> orderDTOs = orderService.getOrders();

        // Then
        assertEquals(2, orderDTOs.size());
        verify(orderMapper, times(2)).convertToOrderDTO(any());
    }

    @Test
    void placeOrder_orderSuccessfullyPlaced_orderDTOReturned() {
        // Given
        OrderRequest orderRequest = new OrderRequest();
        Order newOrder = new Order();
        Order savedOrder = new Order();
        OrderDTO expectedOrderDTO = new OrderDTO();

        when(orderMapper.convertFromOrderRequest(orderRequest)).thenReturn(newOrder);
        when(orderRepository.save(newOrder)).thenReturn(savedOrder);
        when(orderMapper.convertToOrderDTO(savedOrder)).thenReturn(expectedOrderDTO);

        // When
        OrderDTO result = orderService.placeOrder(orderRequest);

        // Then
        assertEquals(expectedOrderDTO, result);
        verify(orderMapper, times(1)).convertFromOrderRequest(orderRequest);
        verify(orderRepository, times(1)).save(newOrder);
        verify(orderMapper, times(1)).convertToOrderDTO(savedOrder);
    }
}