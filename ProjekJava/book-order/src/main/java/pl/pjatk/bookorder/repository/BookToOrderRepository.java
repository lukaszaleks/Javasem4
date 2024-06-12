package pl.pjatk.bookorder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.pjatk.bookorder.entity.BookToOrder;

@Repository
public interface BookToOrderRepository extends JpaRepository<BookToOrder, Long> {
}
