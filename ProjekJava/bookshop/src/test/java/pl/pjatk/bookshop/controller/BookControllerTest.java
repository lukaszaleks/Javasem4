package pl.pjatk.bookshop.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.model.BookDTO;
import io.swagger.model.BookRequest;
import io.swagger.model.NewBookRequest;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import pl.pjatk.bookshop.service.BookService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@WebMvcTest(BookController.class)
@ExtendWith(MockitoExtension.class)
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @Test
    void getBooks_shouldReturnListOfBooks() throws Exception {
        // Given
        List<BookDTO> books = new ArrayList<>();
        when(bookService.getAllBooks()).thenReturn(books);

        // When
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/book")
                        .with(SecurityMockMvcRequestPostProcessors.user("user").password("user").authorities(AuthorityUtils.createAuthorityList("USER")))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(books.size())));

        // Then
        verify(bookService, times(1)).getAllBooks();
    }

    @Test
    void addBook_shouldReturnAddedBook() throws Exception {
        // Given
        NewBookRequest newBookRequest = new NewBookRequest();
        BookDTO addedBook = new BookDTO();
        when(bookService.addBook(newBookRequest)).thenReturn(addedBook);

        // When
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/book")
                        .with(SecurityMockMvcRequestPostProcessors.user("admin").password("admin").authorities(AuthorityUtils.createAuthorityList("ADMINISTRATOR")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(newBookRequest))
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.bookId").value(addedBook.getBookId()));

        // Then
        verify(bookService, times(1)).addBook(newBookRequest);
    }

    @Test
    void addBook_shouldReturnForbidden() throws Exception {
        // Given
        NewBookRequest newBookRequest = new NewBookRequest();
        BookDTO addedBook = new BookDTO();
        when(bookService.addBook(newBookRequest)).thenReturn(addedBook);

        // When
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/book")
                        .with(SecurityMockMvcRequestPostProcessors.user("user").password("user").authorities(AuthorityUtils.createAuthorityList("USER")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(newBookRequest)))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    void getBookById_shouldReturnBookById() throws Exception {
        // Given
        Long bookId = 1L;
        BookDTO bookDTO = new BookDTO();
        when(bookService.getBookById(bookId)).thenReturn(bookDTO);

        // When
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/book/{bookId}", bookId)
                        .with(SecurityMockMvcRequestPostProcessors.user("user").password("user").authorities(AuthorityUtils.createAuthorityList("USER")))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.bookId").value(bookDTO.getBookId()));

        // Then
        verify(bookService, times(1)).getBookById(bookId);
    }

    @Test
    void updateBook_shouldReturnUpdatedBook() throws Exception {
        // Given
        Long bookId = 1L;
        BookRequest bookRequest = new BookRequest();
        BookDTO updatedBook = new BookDTO();
        when(bookService.updateBook(bookId, bookRequest)).thenReturn(updatedBook);

        // When
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/book/{bookId}", bookId)
                        .with(SecurityMockMvcRequestPostProcessors.user("admin").password("admin").authorities(AuthorityUtils.createAuthorityList("ADMINISTRATOR")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(bookRequest))
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.bookId").value(updatedBook.getBookId()));

        // Then
        verify(bookService, times(1)).updateBook(bookId, bookRequest);
    }

    @Test
    void updateBook_shouldReturnForbidden() throws Exception {
        // Given
        Long bookId = 1L;
        BookRequest bookRequest = new BookRequest();
        BookDTO updatedBook = new BookDTO();
        when(bookService.updateBook(bookId, bookRequest)).thenReturn(updatedBook);

        // When
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/book/{bookId}", bookId)
                        .with(SecurityMockMvcRequestPostProcessors.user("user").password("user").authorities(AuthorityUtils.createAuthorityList("USER")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(bookRequest)))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    void deleteBook_shouldReturnOk() throws Exception {
        // Given
        Long bookId = 1L;

        // When
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/book/{bookId}", bookId)
                        .with(SecurityMockMvcRequestPostProcessors.user("admin").password("admin").authorities(AuthorityUtils.createAuthorityList("ADMINISTRATOR")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk());

        // Then
        verify(bookService, times(1)).deleteBook(bookId);
    }

    @Test
    void deleteBook_shouldReturnForbidden() throws Exception {
        // Given
        Long bookId = 1L;

        // When
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/book/{bookId}", bookId)
                        .with(SecurityMockMvcRequestPostProcessors.user("user").password("user").authorities(AuthorityUtils.createAuthorityList("USER")))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    void searchBookBySearchParams_shouldReturnListOfBooks() throws Exception {
        // Given
        String title = "Title";
        String bookType = "Type";
        List<BookDTO> books = new ArrayList<>();
        when(bookService.searchBook(title, bookType)).thenReturn(books);

        // When
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/book/search")
                        .with(SecurityMockMvcRequestPostProcessors.user("user").password("user").authorities(AuthorityUtils.createAuthorityList("USER")))
                        .param("title", title)
                        .param("bookType", bookType)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(books.size())));

        // Then
        verify(bookService, times(1)).searchBook(title, bookType);
    }
}