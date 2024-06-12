package pl.pjatk.bookorder.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "books_to_order")
public class BookToOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookId;
    private Long quantityToOrder;
}
