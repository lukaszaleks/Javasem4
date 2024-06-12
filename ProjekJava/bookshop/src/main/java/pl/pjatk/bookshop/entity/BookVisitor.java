package pl.pjatk.bookshop.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "book_visitor_counters")
@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class BookVisitor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookVisitorId;

    @OneToOne(fetch = FetchType.LAZY)
    private Book book;

    private Long visitorsCount;
}
