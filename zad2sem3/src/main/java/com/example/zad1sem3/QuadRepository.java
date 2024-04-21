package com.example.zad1sem3;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuadRepository extends JpaRepository<Quad, Long> {
}