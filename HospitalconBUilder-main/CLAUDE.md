# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Commands

### Building and Running
- **Compile**: Use IntelliJ IDEA compiler or `javac` from command line (no Gradle/Maven configuration present)
- **Run**: Execute `java Main` from the compiled output directory
- **IDE**: Project configured for IntelliJ IDEA with module file `Hospi.iml`

### Dependencies
- **Lombok**: Version 1.18.38 (already configured in module file)
- **Java Version**: Compatible with standard Java (no specific version constraints found)

## Architecture and Structure

### Core Domain Model
This is a hospital management system built around these main entities:

#### Hospital Management Hierarchy
- **Hospital**: Central entity containing departments and patients
- **Departamento**: Specialized medical departments (Cardiología, Pediatría, Traumatología)
- **Sala**: Treatment rooms/offices within departments
- **Medico**: Doctors with specializations and professional licenses (Matricula)
- **Paciente**: Patients with medical histories and contact information
- **Persona**: Base class for Medico and Paciente

#### Appointment System
- **CitaManager**: Main service implementing CitaService interface for appointment management
- **Cita**: Appointment entities linking patients, doctors, rooms, and schedules
- **EstadoCita**: Appointment states (PROGRAMADA, EN_CURSO, COMPLETADA, CANCELADA)

#### Medical Records
- **HistoriaClinica**: Medical history tracking diagnoses, treatments, and allergies
- **EspecialidadMedica**: Medical specializations enum
- **TipoSangre**: Blood type classifications

### Key Design Patterns
- **Manager Pattern**: CitaManager centralizes appointment logic with validation and persistence
- **Service Layer**: CitaService interface defines contract for appointment operations
- **Domain Model**: Rich entities with business logic and validation
- **Data Transfer**: CSV serialization/deserialization for persistence

### Validation Rules
- **Appointment Scheduling**: Cannot schedule in the past, requires positive cost
- **Doctor Availability**: 2-hour minimum gap between appointments
- **Room Availability**: 2-hour minimum gap between room usage
- **Specialty Matching**: Doctor specialty must match department of assigned room

### Data Management
- **In-Memory Storage**: Uses concurrent maps for fast lookups by patient, doctor, and room
- **CSV Persistence**: Appointments can be saved/loaded from CSV files
- **Immutable Core**: Final fields for essential entity data with controlled mutability

### Entry Point
- **Main.java**: Comprehensive demonstration including:
  - Hospital and department setup
  - Doctor and patient registration
  - Appointment scheduling with different specialties
  - Data persistence testing
  - Validation rule demonstrations
  - System statistics reporting

## Development Notes

### Entity Relationships
- Bidirectional associations managed carefully to prevent memory leaks
- Hospital owns departments and patients
- Departments contain doctors and rooms
- Appointments create cross-references between all entities

### Concurrency Support
- ConcurrentHashMap used in CitaManager for thread-safe appointment indexing
- Collections returned as unmodifiable views to prevent external modification

### Error Handling
- Custom CitaException for appointment-related business rule violations
- Comprehensive validation at entity creation and state transitions
- Null checks and defensive programming throughout