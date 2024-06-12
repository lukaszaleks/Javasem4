package pl.pjatk.bookshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.pjatk.bookshop.entity.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
}
