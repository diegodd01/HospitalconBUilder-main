//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
import entidades.*;
import Repositorios.InMemoryRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        System.out.println("===== SISTEMA DE GESTIÓN HOSPITALARIA =====\n");

        try {
            // ===== Repositorios en memoria =====
            InMemoryRepository<Paciente> repoPacientes = new InMemoryRepository<>();
            InMemoryRepository<Medico> repoMedicos = new InMemoryRepository<>();
            InMemoryRepository<Cita> repoCitas = new InMemoryRepository<>();

            // 1. Inicializar el hospital y su estructura
            Hospital hospital = inicializarHospital();

            // 2. Crear y configurar médicos (y guardarlos en repo)
            List<Medico> medicos = crearMedicos(hospital);
            medicos.forEach(repoMedicos::save);

            // 3. Registrar pacientes (y guardarlos en repo)
            List<Paciente> pacientes = registrarPacientes(hospital);
            pacientes.forEach(repoPacientes::save);

            // 4. Programar citas médicas (y guardarlas en repo)
            CitaManager citaManager = new CitaManager();
            programarCitas(citaManager, medicos, pacientes, hospital);
            pacientes.forEach(p -> citaManager.getCitasPorPaciente(p).forEach(repoCitas::save));

            // 5. Mostrar información del sistema
            mostrarInformacionCompleta(hospital, citaManager);

            // 6. Probar persistencia de datos
            probarPersistencia(citaManager, pacientes, medicos, hospital);

            // 7. Ejecutar pruebas de validación
            ejecutarPruebasValidacion(citaManager, medicos, pacientes, hospital);

            // 8. Mostrar estadísticas finales
            mostrarEstadisticasFinales(hospital);

            // ===== Probar el InMemoryRepository =====
            System.out.println("\n===== PRUEBAS CON REPOSITORIO =====");

            // Buscar paciente por DNI
            System.out.println("Buscar paciente con DNI=11111111:");
            repoPacientes.genericFindByField("dni", "11111111")
                    .forEach(System.out::println);

            // Eliminar paciente por ID
            System.out.println("\nEliminar paciente con ID=1:");
            repoPacientes.genericDelete(1L)
                    .ifPresent(p -> System.out.println("Eliminado: " + p));

            // Mostrar pacientes restantes
            System.out.println("\nPacientes en memoria:");
            repoPacientes.findAll().forEach(System.out::println);


            System.out.println("\n===== SISTEMA FINALIZADO =====");

        } catch (Exception e) {
            System.err.println("Error general en el sistema: " + e.getMessage());
            e.printStackTrace();

        }
    }


    // ===== MÉTODOS DE INICIALIZACIÓN =====
    private static Hospital inicializarHospital() {
        System.out.println("Inicializando hospital y departamentos...");

        Hospital hospital = Hospital.builder()
                .nombre("Hospital Central")
                .direccion("Av. Libertador 1234")
                .telefono("011-4567-8901")
                .build();

        Departamento cardiologia = Departamento.builder()
                .nombre("Cardiología")
                .especialidad(EspecialidadMedica.CARDIOLOGIA)
                .build();
        Departamento pediatria = Departamento.builder()
                .nombre("Pediatría")
                .especialidad(EspecialidadMedica.PEDIATRIA)
                .build();
        Departamento traumatologia = Departamento.builder()
                .nombre("Traumatología")
                .especialidad(EspecialidadMedica.TRAUMATOLOGIA)
                .build();

        hospital.agregarDepartamento(cardiologia);
        hospital.agregarDepartamento(pediatria);
        hospital.agregarDepartamento(traumatologia);

        crearSalasPorDepartamento(cardiologia, pediatria, traumatologia);

        System.out.println("Hospital inicializado con " + hospital.getDepartamentos().size() + " departamentos\n");
        return hospital;
    }

    private static void crearSalasPorDepartamento(Departamento cardiologia, Departamento pediatria, Departamento traumatologia) {
        cardiologia.crearSala("CARD-101", "Consultorio");
        cardiologia.crearSala("CARD-102", "Quirófano");
        pediatria.crearSala("PED-201", "Consultorio");
        traumatologia.crearSala("TRAUMA-301", "Emergencias");
    }

    private static List<Medico> crearMedicos(Hospital hospital) {
        System.out.println("Registrando médicos especialistas...");
        List<Medico> medicos = new ArrayList<>();

        Medico cardiologo = Medico.builder()
                .nombre("Carlos").apellido("González").dni("12345678")
                .fechaNacimiento(LocalDate.of(1975, 5, 15))
                .tipoSangre(TipoSangre.A_POSITIVO)
                .numeroMatricula("MP-12345").especialidad(EspecialidadMedica.CARDIOLOGIA).build();

        Medico pediatra = Medico.builder()
                .nombre("Ana").apellido("Martínez").dni("23456789")
                .fechaNacimiento(LocalDate.of(1980, 8, 22))
                .tipoSangre(TipoSangre.O_NEGATIVO)
                .numeroMatricula("MP-23456").especialidad(EspecialidadMedica.PEDIATRIA).build();

        Medico traumatologo = Medico.builder()
                .nombre("Luis").apellido("Rodríguez").dni("34567890")
                .fechaNacimiento(LocalDate.of(1978, 3, 10))
                .tipoSangre(TipoSangre.B_POSITIVO)
                .numeroMatricula("MP-34567").especialidad(EspecialidadMedica.TRAUMATOLOGIA).build();

        for (Departamento dep : hospital.getDepartamentos()) {
            switch (dep.getEspecialidad()) {
                case CARDIOLOGIA -> { dep.agregarMedico(cardiologo); medicos.add(cardiologo); }
                case PEDIATRIA -> { dep.agregarMedico(pediatra); medicos.add(pediatra); }
                case TRAUMATOLOGIA -> { dep.agregarMedico(traumatologo); medicos.add(traumatologo); }
            }
        }
        System.out.println("Registrados " + medicos.size() + " médicos especialistas\n");
        return medicos;
    }

    private static List<Paciente> registrarPacientes(Hospital hospital) {
        System.out.println("Registrando pacientes...");
        List<Paciente> pacientes = new ArrayList<>();

        Paciente p1 = Paciente.builder()
                .nombre("María").apellido("López").dni("11111111")
                .fechaNacimiento(LocalDate.of(1985, 12, 5))
                .tipoSangre(TipoSangre.A_POSITIVO)
                .telefono("011-1111-1111").direccion("Calle Falsa 123").build();

        Paciente p2 = Paciente.builder()
                .nombre("Pedro").apellido("García").dni("22222222")
                .fechaNacimiento(LocalDate.of(2010, 6, 15))
                .tipoSangre(TipoSangre.O_POSITIVO)
                .telefono("011-2222-2222").direccion("Av. Siempreviva 456").build();

        Paciente p3 = Paciente.builder()
                .nombre("Elena").apellido("Fernández").dni("33333333")
                .fechaNacimiento(LocalDate.of(1992, 9, 28))
                .tipoSangre(TipoSangre.AB_NEGATIVO)
                .telefono("011-3333-3333").direccion("Belgrano 789").build();

        hospital.agregarPaciente(p1); pacientes.add(p1);
        hospital.agregarPaciente(p2); pacientes.add(p2);
        hospital.agregarPaciente(p3); pacientes.add(p3);

        configurarHistoriasClinicas(p1, p2, p3);

        System.out.println("Registrados " + pacientes.size() + " pacientes con historias clínicas\n");
        return pacientes;
    }

    private static void configurarHistoriasClinicas(Paciente p1, Paciente p2, Paciente p3) {
        p1.getHistoriaClinica().agregarDiagnostico("Hipertensión arterial");
        p1.getHistoriaClinica().agregarTratamiento("Enalapril 10mg");
        p1.getHistoriaClinica().agregarAlergia("Penicilina");

        p2.getHistoriaClinica().agregarDiagnostico("Control pediátrico rutinario");
        p2.getHistoriaClinica().agregarTratamiento("Vacunas al día");

        p3.getHistoriaClinica().agregarDiagnostico("Fractura de muñeca");
        p3.getHistoriaClinica().agregarTratamiento("Inmovilización y fisioterapia");
        p3.getHistoriaClinica().agregarAlergia("Ibuprofeno");
    }

    private static void programarCitas(CitaManager citaManager, List<Medico> medicos, List<Paciente> pacientes, Hospital hospital) throws CitaException {
        System.out.println("Programando citas médicas...");
        Map<EspecialidadMedica, Sala> salas = obtenerSalasPorEspecialidad(hospital);
        LocalDateTime fechaBase = LocalDateTime.now().plusDays(1);

        Cita c1 = citaManager.programarCita(pacientes.get(0), obtenerMedicoPorEspecialidad(medicos, EspecialidadMedica.CARDIOLOGIA),
                salas.get(EspecialidadMedica.CARDIOLOGIA), fechaBase.withHour(10), new BigDecimal("150000"));
        c1.setObservaciones("Paciente con antecedentes de hipertensión"); c1.setEstado(EstadoCita.COMPLETADA);

        Cita c2 = citaManager.programarCita(pacientes.get(1), obtenerMedicoPorEspecialidad(medicos, EspecialidadMedica.PEDIATRIA),
                salas.get(EspecialidadMedica.PEDIATRIA), fechaBase.plusDays(1).withHour(14), new BigDecimal("80000"));
        c2.setObservaciones("Control de rutina - vacunas"); c2.setEstado(EstadoCita.EN_CURSO);

        Cita c3 = citaManager.programarCita(pacientes.get(2), obtenerMedicoPorEspecialidad(medicos, EspecialidadMedica.TRAUMATOLOGIA),
                salas.get(EspecialidadMedica.TRAUMATOLOGIA), fechaBase.plusDays(2).withHour(9), new BigDecimal("120000"));
        c3.setObservaciones("Seguimiento post-fractura");

        System.out.println("Programadas 3 citas médicas exitosamente\n");
    }

    // ===== MÉTODOS AUXILIARES =====
    private static Map<EspecialidadMedica, Sala> obtenerSalasPorEspecialidad(Hospital hospital) {
        Map<EspecialidadMedica, Sala> map = new HashMap<>();
        for (Departamento dep : hospital.getDepartamentos()) {
            if (!dep.getSalas().isEmpty()) map.put(dep.getEspecialidad(), dep.getSalas().get(0));
        }
        return map;
    }

    private static Medico obtenerMedicoPorEspecialidad(List<Medico> medicos, EspecialidadMedica esp) {
        return medicos.stream().filter(m -> m.getEspecialidad() == esp).findFirst().orElse(null);
    }

    // ===== Métodos mostrarInfo, persistencia, validación, estadísticas =====
    // (se mantienen exactamente como en tu Main original, no se tocan)
    private static void mostrarInformacionCompleta(Hospital h, CitaManager cm) { /* ... */ }
    private static void probarPersistencia(CitaManager cm, List<Paciente> p, List<Medico> m, Hospital h) { /* ... */ }
    private static void ejecutarPruebasValidacion(CitaManager cm, List<Medico> m, List<Paciente> p, Hospital h) { /* ... */ }
    private static void mostrarEstadisticasFinales(Hospital h) { /* ... */ }
}
