package com.example.zad1sem3;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class QuadResponse {
    private Long quadId;
    private String brand;
    private String model;
    private int year;
}