package com.example.fizjoterapia.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne(optional = true)
    @JoinColumn(name = "therapy_id", nullable = true)
    private Therapy therapy;

    private LocalDate date; // data wizyty
    private Integer appointmentNumber; // numer na liście danego dnia

    // Istniejące pola
    private String deletedTherapyName; // Nowe pole

    // Gettery i Settery

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Therapy getTherapy() {
        return therapy;
    }

    public void setTherapy(Therapy therapy) {
        this.therapy = therapy;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Integer getAppointmentNumber() {
        return appointmentNumber;
    }

    public void setAppointmentNumber(Integer appointmentNumber) {
        this.appointmentNumber = appointmentNumber;
    }

    public String getDeletedTherapyName() {
        return deletedTherapyName;
    }

    public void setDeletedTherapyName(String deletedTherapyName) {
        this.deletedTherapyName = deletedTherapyName;
    }
}
