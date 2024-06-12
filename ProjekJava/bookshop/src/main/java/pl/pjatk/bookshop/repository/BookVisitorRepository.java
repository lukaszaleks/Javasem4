package pl.pjatk.bookshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.pjatk.bookshop.entity.BookVisitor;

import java.util.Optional;
import java.util.Set;

@Repository
public interface BookVisitorRepository extends JpaRepository<BookVisitor, Long> {
    @Modifying
    @Query("""
           UPDATE BookVisitor book_visitor
           SET book_visitor.visitorsCount = 0
           WHERE book_visitor.book.bookId IN (:bookIdsToClear)
           """)
    void clearVisitors(Set<Long> bookIdsToClear);

    Optional<BookVisitor> findByBook_BookId(Long bookId);

    @Modifying
    void deleteAllByBook_BookId(Long bookId);
}
