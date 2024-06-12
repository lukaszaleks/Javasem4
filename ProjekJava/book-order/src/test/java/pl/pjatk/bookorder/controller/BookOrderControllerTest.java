package pl.pjatk.bookorder.controller;

import io.swagger.model.BookOrderRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import pl.pjatk.bookorder.service.BookOrderService;

import java.io.ByteArrayInputStream;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(BookOrderController.class)
class BookOrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookOrderService bookOrderService;

    @Test
    void testBookOrder() throws Exception {
        BookOrderRequest orderRequest = new BookOrderRequest();
        orderRequest.setBookId(1L);
        orderRequest.setVisitorCount(20L);

        String jsonRequest = "[{\"bookId\":1,\"visitorCount\":20}]";

        mockMvc.perform(post("/order-report ")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk());

        verify(bookOrderService, times(1)).orderBooks(anyList());
    }

    @Test
    void testGetPdf() throws Exception {
        ByteArrayInputStream bis = new ByteArrayInputStream("Test PDF Content".getBytes());
        when(bookOrderService.printOrder()).thenReturn(bis);

        mockMvc.perform(get("/print"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Disposition", "inline; filename=sample.pdf"))
                .andExpect(content().contentType(MediaType.APPLICATION_PDF));

        verify(bookOrderService, times(1)).printOrder();
    }
}