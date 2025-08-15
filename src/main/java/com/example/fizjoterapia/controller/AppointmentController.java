package com.example.fizjoterapia.controller;

import com.example.fizjoterapia.model.Appointment;
import com.example.fizjoterapia.model.Patient;
import com.example.fizjoterapia.model.Therapy;
import com.example.fizjoterapia.repository.AppointmentRepository;
import com.example.fizjoterapia.repository.PatientRepository;
import com.example.fizjoterapia.repository.TherapyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/appointments")
public class AppointmentController {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private TherapyRepository therapyRepository;

    @GetMapping("/add")
    public String showAddAppointmentForm(Model model) {
        model.addAttribute("appointment", new Appointment());
        model.addAttribute("patients", patientRepository.findAll());
        model.addAttribute("therapies", therapyRepository.findAll());
        return "add_appointment";
    }

    @PostMapping("/add")
    public String addAppointment(
            @RequestParam Long patientId,
            @RequestParam Long therapyId,
            @RequestParam String appointmentDate) {

        // Pobranie pacjenta i terapii na podstawie ID
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new IllegalArgumentException("Nieprawidłowy identyfikator pacjenta: " + patientId));
        Therapy therapy = therapyRepository.findById(therapyId)
                .orElseThrow(() -> new IllegalArgumentException("Nieprawidłowy identyfikator terapii: " + therapyId));

        // Ustawienie daty wizyty
        LocalDate date = LocalDate.parse(appointmentDate);

        // Obliczenie numeru wizyty
        List<Appointment> appointmentsForDate = appointmentRepository.findByDateOrderByAppointmentNumber(date);
        int nextAppointmentNumber = appointmentsForDate.size() + 1;

        // Utworzenie i zapisanie wizyty
        Appointment appointment = new Appointment();
        appointment.setPatient(patient);
        appointment.setTherapy(therapy);
        appointment.setDate(date);
        appointment.setAppointmentNumber(nextAppointmentNumber);
        appointmentRepository.save(appointment);

        return "redirect:/appointments?date=" + date;
    }

    @GetMapping
    public String showAppointmentsForDate(@RequestParam(required = false) LocalDate date, Model model) {
        if (date == null) {
            date = LocalDate.now();
        }
        List<Appointment> appointments = appointmentRepository.findByDateOrderByAppointmentNumber(date);
        model.addAttribute("appointments", appointments);
        model.addAttribute("date", date);
        return "appointments_list";
    }

    // Wyświetla kalendarz do wyboru daty
    @GetMapping("/calendar")
    public String showCalendarPage(Model model) {
        model.addAttribute("selectedDate", LocalDate.now());
        return "calendar";  // Nazwa pliku HTML dla widoku kalendarza
    }

    // Wyświetla listę pacjentów z terapiami dla wybranej daty
    @GetMapping("/appointments/appointments")
    public String showAppointmentsForSelectedDate(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date, Model model) {
        List<Appointment> appointments = appointmentRepository.findByDateOrderByAppointmentNumber(date);
        model.addAttribute("appointments", appointments);
        model.addAttribute("date", date);
        return "appointments_list";  // Nazwa pliku HTML do wyświetlania listy pacjentów z terapiami
    }

    // Metoda do wyświetlenia formularza edycji wizyty
    @GetMapping("/edit/{id}")
    public String showEditAppointmentForm(@PathVariable Long id, Model model) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Nieprawidłowy identyfikator wizyty: " + id));
        model.addAttribute("appointment", appointment);
        model.addAttribute("patients", patientRepository.findAll());
        model.addAttribute("therapies", therapyRepository.findAll());
        return "edit_appointment"; // Widok do edycji wizyty
    }

    // Metoda do aktualizacji wizyty
    @PostMapping("/update/{id}")
    public String updateAppointment(
            @PathVariable Long id,
            @RequestParam Long patientId,
            @RequestParam Long therapyId,
            @RequestParam String appointmentDate) {

        // Znajdź pacjenta i terapię
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new IllegalArgumentException("Nieprawidłowy identyfikator pacjenta: " + patientId));
        Therapy therapy = therapyRepository.findById(therapyId)
                .orElseThrow(() -> new IllegalArgumentException("Nieprawidłowy identyfikator terapii: " + therapyId));

        // Znajdź istniejącą wizytę
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Nieprawidłowy identyfikator wizyty: " + id));

        // Zaktualizuj dane wizyty
        appointment.setPatient(patient);
        appointment.setTherapy(therapy);
        appointment.setDate(LocalDate.parse(appointmentDate));
        appointmentRepository.save(appointment); // Zapisz zaktualizowaną wizytę

        return "redirect:/appointments?date=" + appointmentDate;
    }

    // Metoda do usuwania wizyty
    @GetMapping("/delete/{id}")
    public String deleteAppointment(@PathVariable Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Nieprawidłowy identyfikator wizyty: " + id));

        LocalDate appointmentDate = appointment.getDate();
        appointmentRepository.delete(appointment);

        return "redirect:/appointments?date=" + appointmentDate;
    }
}
