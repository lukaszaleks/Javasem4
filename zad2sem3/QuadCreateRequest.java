package com.example.zad1sem3;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class QuadCreateRequest {
    private String brand;
    private String model;
    private int year;
}
