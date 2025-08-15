package com.example.fizjoterapia.controller;

import com.example.fizjoterapia.model.Appointment;
import com.example.fizjoterapia.model.Therapy;
import com.example.fizjoterapia.repository.AppointmentRepository;
import com.example.fizjoterapia.repository.TherapyRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/therapies")
public class TherapyController {

    @Autowired
    private TherapyRepository therapyRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @GetMapping("/add")
    public String showAddTherapyForm(Model model) {
        model.addAttribute("therapy", new Therapy());
        return "add_therapy"; // szablon Thymeleaf dla dodawania terapii
    }

    @PostMapping("/add")
    public String addTherapy(Therapy therapy) {
        therapyRepository.save(therapy); // zapis do bazy danych
        return "redirect:/therapies"; // przekierowanie po dodaniu terapii
    }

    @GetMapping
    public String showTherapiesList(Model model) {
        model.addAttribute("therapies", therapyRepository.findAll());
        return "therapies_list"; // szablon do wyświetlenia listy terapii
    }

    // Endpoint do wyświetlania formularza edycji terapii
    @GetMapping("/edit/{id}")
    public String showEditTherapyForm(@PathVariable Long id, Model model) {
        Therapy therapy = therapyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Nieprawidłowy identyfikator terapii: " + id));
        model.addAttribute("therapy", therapy);
        return "edit_therapy"; // widok do edycji terapii
    }

    // Endpoint do aktualizacji terapii
    @PostMapping("/update/{id}")
    public String updateTherapy(@PathVariable Long id, Therapy therapy) {
        therapy.setId(id); // ustawienie ID dla aktualizacji istniejącego obiektu
        therapyRepository.save(therapy); // aktualizacja terapii
        return "redirect:/therapies";
    }

    @GetMapping("/delete/{id}")
    public String deleteTherapyById(@PathVariable Long id) {
        deleteTherapy(id); // Wywołanie istniejącej metody
        return "redirect:/therapies"; // Po usunięciu przekierowanie na listę terapii
    }

    @Transactional
    public void deleteTherapy(Long therapyId) {
        if (!therapyRepository.existsById(therapyId)) {
            throw new IllegalArgumentException("Terapia o podanym ID nie istnieje: " + therapyId);
        }

        List<Appointment> appointments = appointmentRepository.findByTherapyId(therapyId);
        for (Appointment appointment : appointments) {
            appointment.setTherapy(null); // Zerowanie terapii
            appointment.setDeletedTherapyName("Terapia usunięta");
            appointmentRepository.save(appointment);
        }
        therapyRepository.deleteById(therapyId);
    }


}

