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
@ToString(exclude = {"departamentos", "pacientes"})
@Builder
public class Hospital implements Serializable {
    private final String nombre;
    private final String direccion;
    private final String telefono;
    private final List<Departamento> departamentos = new ArrayList<>();
    private final List<Paciente> pacientes = new ArrayList<>();

    private Hospital(HospitalBuilder builder) {
        this.nombre = validarString(builder.nombre, "El nombre del hospital no puede ser nulo ni vacío");
        this.direccion = validarString(builder.direccion, "La dirección no puede ser nula ni vacía");
        this.telefono = validarString(builder.telefono, "El teléfono no puede ser nulo ni vacío");
    }

    public static class HospitalBuilder {
        private String nombre;
        private String direccion;
        private String telefono;

        public HospitalBuilder nombre(String nombre) {
            this.nombre = nombre;
            return this;
        }

        public HospitalBuilder direccion(String direccion) {
            this.direccion = direccion;
            return this;
        }

        public HospitalBuilder telefono(String telefono) {
            this.telefono = telefono;
            return this;
        }

        public Hospital build() {
            return new Hospital(this);
        }
    }

    public void agregarDepartamento(Departamento departamento) {
        if (departamento != null && !departamentos.contains(departamento)) {
            departamentos.add(departamento);
            departamento.setHospital(this);
        }
    }

    public void agregarPaciente(Paciente paciente) {
        if (paciente != null && !pacientes.contains(paciente)) {
            pacientes.add(paciente);
            paciente.setHospital(this);
        }
    }

    public List<Departamento> getDepartamentos() {
        return Collections.unmodifiableList(departamentos);
    }

    public List<Paciente> getPacientes() {
        return Collections.unmodifiableList(pacientes);
    }

    List<Departamento> getInternalDepartamentos() {
        return departamentos;
    }

    List<Paciente> getInternalPacientes() {
        return pacientes;
    }


    private String validarString(String valor, String mensajeError) {
        Objects.requireNonNull(valor, mensajeError);
        if (valor.trim().isEmpty()) {
            throw new IllegalArgumentException(mensajeError);
        }
        return valor;
    }
}
