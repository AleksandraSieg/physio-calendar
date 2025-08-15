package com.example.fizjoterapia.repository;

import com.example.fizjoterapia.model.Therapy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TherapyRepository extends JpaRepository<Therapy, Long> {
}
