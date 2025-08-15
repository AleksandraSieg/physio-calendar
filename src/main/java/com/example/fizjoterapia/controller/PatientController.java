package com.example.fizjoterapia.controller;

import com.example.fizjoterapia.model.Patient;
import com.example.fizjoterapia.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import java.util.List;
@Controller
@RequestMapping("/patients")
public class PatientController {

    // Wstrzykujemy repozytorium za pomocą adnotacji @Autowired
    @Autowired
    private PatientRepository patientRepository;

    @GetMapping("/add")
    public String showAddPatientForm(Model model) {
        model.addAttribute("patient", new Patient());
        return "add_patient"; // nazwa szablonu Thymeleaf
    }

    @PostMapping("/add")
    public String addPatient(Patient patient) {
        System.out.println("Dodawany pacjent: " + patient); // Dodaj logowanie
        patientRepository.save(patient);  // Zapisz pacjenta w bazie danych
        return "redirect:/patients/list"; // Przekierowanie do listy pacjentów po dodaniu pacjenta
    }

    // Nowa metoda do wyświetlania listy pacjentów
    @GetMapping("/list")
    public String listPatients(Model model) {
        List<Patient> patients = patientRepository.findAll();  // Pobierz listę pacjentów z bazy danych
        model.addAttribute("patients", patients);
        return "patient_list";  // Nazwa szablonu Thymeleaf do wyświetlania listy pacjentów
    }

    // Formularz edycji pacjenta
    @GetMapping("/edit/{id}")
    public String showEditPatientForm(@PathVariable Long id, Model model) {
        Optional<Patient> patient = patientRepository.findById(id);
        if (patient.isPresent()) {
            model.addAttribute("patient", patient.get());
            return "edit_patient"; // Nazwa szablonu do edycji pacjenta
        } else {
            return "redirect:/patients/list"; // Jeśli pacjent nie istnieje, przekierowanie do listy
        }
    }

    // Aktualizacja pacjenta
    @PostMapping("/edit/{id}")
    public String updatePatient(@PathVariable Long id, @ModelAttribute Patient updatedPatient) {
        Optional<Patient> patient = patientRepository.findById(id);
        if (patient.isPresent()) {
            Patient existingPatient = patient.get();
            existingPatient.setFirstName(updatedPatient.getFirstName());
            existingPatient.setLastName(updatedPatient.getLastName());
            existingPatient.setBirthDate(updatedPatient.getBirthDate());
            existingPatient.setProblemDescription(updatedPatient.getProblemDescription());
            patientRepository.save(existingPatient);
            return "redirect:/patients/list"; // Przekierowanie do listy pacjentów po aktualizacji
        } else {
            return "redirect:/patients/list"; // Przekierowanie, jeśli pacjent nie został znaleziony
        }
    }


    // Usuwanie pacjenta
    @GetMapping("/delete/{id}")
    public String deletePatient(@PathVariable Long id) {
        patientRepository.deleteById(id);
        return "redirect:/patients/list"; // Przekierowanie po usunięciu pacjenta
    }
}
