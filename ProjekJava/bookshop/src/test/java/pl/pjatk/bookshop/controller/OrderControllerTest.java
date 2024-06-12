package pl.pjatk.bookshop.controller;

import io.swagger.model.OrderDTO;
import io.swagger.model.OrderRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import pl.pjatk.bookshop.service.OrderService;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@WebMvcTest(OrderController.class)
@ExtendWith(MockitoExtension.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @Test
    void completeOrder_shouldReturnOk() throws Exception {
        // Given
        Long orderId = 1L;

        // When
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/order/complete/{orderId}", orderId)
                        .with(SecurityMockMvcRequestPostProcessors.user("admin").password("admin").authorities(AuthorityUtils.createAuthorityList("ADMINISTRATOR")))
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk());

        // Then
        verify(orderService, times(1)).completeOrder(orderId);
    }

    @Test
    void completeOrder_shouldReturnForbidden() throws Exception {
        // Given
        Long orderId = 1L;

        // When
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/order/complete/{orderId}", orderId)
                        .with(SecurityMockMvcRequestPostProcessors.user("user").password("user").authorities(AuthorityUtils.createAuthorityList("USER")))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    void getOrders_shouldReturnListOfOrders() throws Exception {
        // Given
        List<OrderDTO> orders = new ArrayList<>();
        when(orderService.getOrders()).thenReturn(orders);

        // When
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/order")
                        .with(SecurityMockMvcRequestPostProcessors.user("admin").password("admin").authorities(AuthorityUtils.createAuthorityList("ADMINISTRATOR")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void placeOrder_shouldReturnPlacedOrder() throws Exception {
        // Given
        OrderRequest orderRequest = new OrderRequest();
        OrderDTO placedOrder = new OrderDTO();
        when(orderService.placeOrder(orderRequest)).thenReturn(placedOrder);

        // When
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/order")
                        .with(SecurityMockMvcRequestPostProcessors.user("user").password("user").authorities(AuthorityUtils.createAuthorityList("USER")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
                        .content("{}"))
                .andExpect(MockMvcResultMatchers.status().isOk());

        // Then
        verify(orderService, times(1)).placeOrder(orderRequest);
    }
}