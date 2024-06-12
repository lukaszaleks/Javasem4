package pl.pjatk.bookshop.mapper;

import io.swagger.model.BookDTO;
import io.swagger.model.BookOrderRequest;
import io.swagger.model.BookRequest;
import io.swagger.model.NewBookRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import pl.pjatk.bookshop.entity.Author;
import pl.pjatk.bookshop.entity.Book;

@Mapper
public interface BookMapper {

    @Mapping(target = "authorId", source = "author", qualifiedByName = "getAuthorId")
    BookDTO convertToBookDTO(Book book);

    @Mapping(target = "bookId", ignore = true)
    @Mapping(target = "visitor", ignore = true)
    @Mapping(target = "author", source = "authorId", qualifiedByName = "getAuthor")
    Book createBook(NewBookRequest newBookRequest);

    @Mapping(target = "title", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "bookType", ignore = true)
    @Mapping(target = "pages", ignore = true)
    @Mapping(target = "visitor", ignore = true)
    @Mapping(target = "availableQuantity", ignore = true)
    Book update(BookRequest bookRequest, @MappingTarget Book book);

    @Mapping(target = "bookId", source = "bookId")
    @Mapping(target = "title", source = "title")
    @Mapping(target = "visitorCount", source = "visitor.visitorsCount")
    BookOrderRequest convertToBookOrderRequest(Book book);

    @Named("getAuthor")
    default Author getAuthor(Long authorId) {
        if (authorId != null) {
            Author author = new Author();
            author.setAuthorId(authorId);
            return author;
        }
        return null;
    }

    @Named("getAuthorId")
    default Long getAuthorId(Author author) {
        return (author != null) ? author.getAuthorId() : null;
    }
}
