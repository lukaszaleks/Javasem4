package pl.pjatk.bookshop.service;

import io.swagger.model.BookDTO;
import io.swagger.model.BookOrderRequest;
import io.swagger.model.BookRequest;
import io.swagger.model.NewBookRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.pjatk.bookshop.config.exception.BookNotFoundException;
import pl.pjatk.bookshop.config.exception.SearchParamsException;
import pl.pjatk.bookshop.controller.OrderBooks;
import pl.pjatk.bookshop.entity.Book;
import pl.pjatk.bookshop.entity.BookVisitor;
import pl.pjatk.bookshop.mapper.BookMapper;
import pl.pjatk.bookshop.repository.BookRepository;
import pl.pjatk.bookshop.repository.BookVisitorRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookVisitorRepository bookVisitorRepository;

    @Mock
    private BookMapper bookMapper;

    @Mock
    private OrderBooks orderBooks;

    @InjectMocks
    private BookService bookService;

    @Test
    void getAllBooks_booksExist_booksReturned() {
        // Given
        Book book1 = new Book();
        Book book2 = new Book();
        when(bookRepository.findAll()).thenReturn(Arrays.asList(book1, book2));

        // When
        List<BookDTO> bookDTOs = bookService.getAllBooks();

        // Then
        assertEquals(2, bookDTOs.size());
        verify(bookMapper, times(2)).convertToBookDTO(any());
    }

    @Test
    void addBook_bookSuccessfullyAdded_bookDTOReturned() {
        // Given
        NewBookRequest newBookRequest = new NewBookRequest();
        Book newBook = new Book();
        newBook.setBookId(1L);
        BookVisitor bookVisitor = new BookVisitor();
        bookVisitor.setBook(newBook);
        BookDTO expectedBookDTO = new BookDTO();
        expectedBookDTO.setBookId(1L);

        when(bookRepository.save(any())).thenReturn(newBook);
        when(bookMapper.createBook(any())).thenReturn(newBook);
        when(bookMapper.convertToBookDTO(any())).thenReturn(expectedBookDTO);

        // When
        BookDTO result = bookService.addBook(newBookRequest);

        // Then
        assertEquals(expectedBookDTO, result);
        verify(bookRepository, times(1)).save(newBook);
        verify(bookMapper, times(1)).convertToBookDTO(newBook);
    }

    @Test
    void getBookById_bookExists_bookDTOReturned() {
        // Given
        Long bookId = 1L;
        Book foundBook = new Book();
        BookDTO expectedBookDTO = new BookDTO();

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(foundBook));
        when(bookMapper.convertToBookDTO(foundBook)).thenReturn(expectedBookDTO);

        // When
        BookDTO result = bookService.getBookById(bookId);

        // Then
        assertEquals(expectedBookDTO, result);
        verify(bookMapper, times(1)).convertToBookDTO(foundBook);
    }

    @Test
    void updateBook_bookSuccessfullyUpdated_bookDTOReturned() {
        // Given
        Long bookId = 1L;
        BookRequest bookRequest = new BookRequest();
        Book existingBook = new Book();
        Book updatedBook = new Book();
        BookDTO expectedBookDTO = new BookDTO();

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(existingBook));
        when(bookMapper.update(bookRequest, existingBook)).thenReturn(updatedBook);
        when(bookRepository.save(updatedBook)).thenReturn(updatedBook);
        when(bookMapper.convertToBookDTO(updatedBook)).thenReturn(expectedBookDTO);

        // When
        BookDTO result = bookService.updateBook(bookId, bookRequest);

        // Then
        assertEquals(expectedBookDTO, result);
        verify(bookMapper, times(1)).update(bookRequest, existingBook);
        verify(bookRepository, times(1)).save(updatedBook);
        verify(bookMapper, times(1)).convertToBookDTO(updatedBook);
    }

    @Test
    void orderBooks_booksOrderedSuccessfully() {
        // When
        bookService.orderBooks();

        // Then
        verify(orderBooks, times(1)).orderBooks(List.of());
    }

    @Test
    void searchBook_validParameters_booksReturned() {
        // Given
        String title = "Java";
        String bookType = "Programming";
        Book book1 = new Book();
        Book book2 = new Book();
        when(bookRepository.findAllByParams(title, bookType)).thenReturn(Arrays.asList(book1, book2));

        // When
        List<BookDTO> result = bookService.searchBook(title, bookType);

        // Then
        assertEquals(2, result.size());
        verify(bookMapper, times(2)).convertToBookDTO(any());
    }

    @Test
    void searchBook_invalidParameters_exceptionThrown() {
        // Given
        String title = "";
        String bookType = "";

        // When / Then
        assertThrows(SearchParamsException.class, () -> bookService.searchBook(title, bookType));
        verify(bookRepository, never()).findAllByParams(any(), any());
        verify(bookMapper, never()).convertToBookDTO(any());
    }

    @Test
    void deleteBook_bookExists_bookDeletedSuccessfully() {
        // Given
        Long bookId = 1L;
        Book existingBook = new Book();

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(existingBook));

        // When
        bookService.deleteBook(bookId);

        // Then
        verify(bookVisitorRepository, times(1)).deleteAllByBook_BookId(bookId);
        verify(bookRepository, times(1)).delete(existingBook);
    }

    @Test
    void deleteBook_bookDoesNotExist_exceptionThrown() {
        // Given
        Long bookId = 1L;
        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(BookNotFoundException.class, () -> bookService.deleteBook(bookId));
        verify(bookVisitorRepository, never()).deleteAllByBook_BookId(any());
        verify(bookRepository, never()).delete(any());
    }
}