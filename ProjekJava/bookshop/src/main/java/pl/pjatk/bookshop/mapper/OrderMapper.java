package pl.pjatk.bookshop.mapper;

import io.swagger.model.OrderDTO;
import io.swagger.model.OrderRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import pl.pjatk.bookshop.entity.Book;
import pl.pjatk.bookshop.entity.Order;

@Mapper
public interface OrderMapper {

    @Mapping(target = "bookId", source = "book", qualifiedByName = "getBookId")
    OrderDTO convertToOrderDTO(Order order);

    @Mapping(target = "book", source = "bookId", qualifiedByName = "getBook")
    @Mapping(target = "orderId", ignore = true)
    @Mapping(target = "totalPrice", ignore = true)
    @Mapping(target = "bookPrice", ignore = true)
    @Mapping(target = "paid", ignore = true)
    Order convertFromOrderRequest(OrderRequest orderRequest);

    @Named("getBook")
    default Book getBook(Long bookId) {
        if (bookId != null) {
            Book book = new Book();
            book.setBookId(bookId);
            return book;
        }
        return null;
    }

    @Named("getBookId")
    default Long getBookId(Book book) {
        return (book != null) ? book.getBookId() : null;
    }
}
