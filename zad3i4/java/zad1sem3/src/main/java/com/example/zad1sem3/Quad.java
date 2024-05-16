package com.example.zad1sem3;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "quads")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Quad {
    @Id
    @Column(unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long quadId;
    private String brand;
    private String model;
    @Column(name = "manufacture_year")
    private int year;
}
