package com.example.fizjoterapia.repository;

import com.example.fizjoterapia.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<Patient, Long> {
}
