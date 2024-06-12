package pl.pjatk.bookshop.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "books")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookId;

    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    private Author author;

    @Enumerated(EnumType.STRING)
    private BookType bookType;

    private Integer pages;

    @OneToOne(fetch = FetchType.LAZY)
    private BookVisitor visitor;

    private Double price;

    private Integer availableQuantity;
}
