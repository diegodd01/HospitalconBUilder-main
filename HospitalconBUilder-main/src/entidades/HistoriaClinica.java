package entidades;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Getter
@ToString(exclude = {"paciente"})
@Builder
public class HistoriaClinica implements Serializable {
    private final String numeroHistoria;
    private final Paciente paciente;
    private final LocalDateTime fechaCreacion;
    private final List<String> diagnosticos = new ArrayList<>();
    private final List<String> tratamientos = new ArrayList<>();
    private final List<String> alergias = new ArrayList<>();

    private HistoriaClinica(HistoriaClinicaBuilder builder) {
        this.paciente = Objects.requireNonNull(builder.paciente, "El paciente no puede ser nulo");
        this.fechaCreacion = builder.fechaCreacion != null ? builder.fechaCreacion : LocalDateTime.now();
        this.numeroHistoria = generarNumeroHistoria();
    }

    public static class HistoriaClinicaBuilder {
        private Paciente paciente;
        private LocalDateTime fechaCreacion;

        public HistoriaClinicaBuilder paciente(Paciente paciente) {
            this.paciente = paciente;
            return this;
        }

        public HistoriaClinicaBuilder fechaCreacion(LocalDateTime fechaCreacion) {
            this.fechaCreacion = fechaCreacion;
            return this;
        }

        public HistoriaClinica build() {
            return new HistoriaClinica(this);
        }
    }

    private String generarNumeroHistoria() {
        return "HC-" + paciente.getDni() + "-" + fechaCreacion.getYear();
    }

    public void agregarDiagnostico(String diagnostico) {
        if (diagnostico != null && !diagnostico.trim().isEmpty()) {
            diagnosticos.add(diagnostico);
        }
    }

    public void agregarTratamiento(String tratamiento) {
        if (tratamiento != null && !tratamiento.trim().isEmpty()) {
            tratamientos.add(tratamiento);
        }
    }

    public void agregarAlergia(String alergia) {
        if (alergia != null && !alergia.trim().isEmpty()) {
            alergias.add(alergia);
        }
    }

    public List<String> getDiagnosticos() {
        return Collections.unmodifiableList(diagnosticos);
    }

    public List<String> getTratamientos() {
        return Collections.unmodifiableList(tratamientos);
    }

    public List<String> getAlergias() {
        return Collections.unmodifiableList(alergias);
    }

}
