# Physio Calendar

A simple calendar application for physiotherapists to manage patients, therapies, and appointments. Built with Spring Boot.


## Features

Manage patients, therapies, and appointments (CRUD operations)

Simple data model with relationships between patients, therapies, and appointments

In-memory H2 database for development

Thymeleaf templates (basic UI support)

Ready for extension (validation, PostgreSQL, reminders, etc.)


## Technologies

Java 17+

Spring Boot 3 (Web, Thymeleaf, Data JPA)

H2 Database (runtime, dev mode)

Spring Boot DevTools

Spring Boot Starter Test (planned – tests not yet implemented)

Maven (build tool)


## Data Model

Patient: firstName, lastName, birthDate, problemDescription

Therapy: name, description

Appointment: patient, therapy, date

Relationships (planned, not fully working yet):

One Patient can have many Appointments

Appointment links a Patient with a Therapy


## API (in progress)

Each resource has a basic CRUD controller (still under development)

Running the project

## Requirements

Java 17+

Maven 3.8+

## Start application (dev mode with H2)
```bash
./mvnw spring-boot:run
```

## Then open in browser:

http://localhost:8080/appointments

