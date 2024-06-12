package pl.pjatk.bookshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.pjatk.bookshop.entity.Book;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    @Query("""
        SELECT book FROM Book book
        LEFT JOIN FETCH book.visitor
        """)
    List<Book> findAllBooksWithVisitors();

    @Query("""
           SELECT book FROM Book book
           WHERE book.title LIKE %:title%
           OR book.bookType = :bookType
           """)
    List<Book> findAllByParams(String title, String bookType);
}
