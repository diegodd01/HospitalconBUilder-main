package entidades;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Getter
@ToString(exclude = {"hospital", "medicos", "salas"})
@Builder
public class Departamento implements Serializable {
    private final String nombre;
    private final EspecialidadMedica especialidad;
    private Hospital hospital;
    private final List<Medico> medicos = new ArrayList<>();
    private final List<Sala> salas = new ArrayList<>();

    private Departamento(DepartamentoBuilder builder) {
        this.nombre = validarString(builder.nombre, "El nombre del departamento no puede ser nulo ni vacío");
        this.especialidad = Objects.requireNonNull(builder.especialidad, "La especialidad no puede ser nula");
    }

    public static class DepartamentoBuilder {
        private String nombre;
        private EspecialidadMedica especialidad;

        public DepartamentoBuilder nombre(String nombre) {
            this.nombre = nombre;
            return this;
        }

        public DepartamentoBuilder especialidad(EspecialidadMedica especialidad) {
            this.especialidad = especialidad;
            return this;
        }

        public Departamento build() {
            return new Departamento(this);
        }
    }

    public void setHospital(Hospital hospital) {
        if (this.hospital != hospital) {
            if (this.hospital != null) {
                this.hospital.getInternalDepartamentos().remove(this);
            }
            this.hospital = hospital;
            if (hospital != null) {
                hospital.getInternalDepartamentos().add(this);
            }
        }
    }

    public void agregarMedico(Medico medico) {
        if (medico != null && !medicos.contains(medico)) {
            medicos.add(medico);
            medico.setDepartamento(this);
        }
    }

    public Sala crearSala(String numero, String tipo) {
        Sala sala = Sala.builder()
                .numero(numero)
                .tipo(tipo)
                .departamento(this)
                .build();
        salas.add(sala);
        return sala;
    }

    public List<Medico> getMedicos() {
        return Collections.unmodifiableList(medicos);
    }

    public List<Sala> getSalas() {
        return Collections.unmodifiableList(salas);
    }


    private String validarString(String valor, String mensajeError) {
        Objects.requireNonNull(valor, mensajeError);
        if (valor.trim().isEmpty()) {
            throw new IllegalArgumentException(mensajeError);
        }
        return valor;
    }
}
