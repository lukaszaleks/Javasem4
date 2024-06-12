package pl.pjatk.bookshop.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import pl.pjatk.bookshop.service.BookService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@WebMvcTest(BookReportController.class)
@ExtendWith(MockitoExtension.class)
class BookReportControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @Test
    void orderBooks_shouldReturnOk() throws Exception {
        // When
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/order-report ")
                        .with(SecurityMockMvcRequestPostProcessors.user("admin").password("admin").authorities(AuthorityUtils.createAuthorityList("ADMINISTRATOR")))
                        .contentType("application/json")
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk());

        // Then
        verify(bookService, times(1)).orderBooks();
    }

    @Test
    void orderBooks_shouldReturnForbiddenForUser() throws Exception {
        // When
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/order-report ")
                        .with(SecurityMockMvcRequestPostProcessors.user("user").password("user").authorities(AuthorityUtils.createAuthorityList("USER")))
                        .contentType("application/json"))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }
}