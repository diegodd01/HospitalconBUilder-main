package entidades;

import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@Getter
@ToString
@SuperBuilder
public abstract class Persona implements Serializable {
    protected final String nombre;
    protected final String apellido;
    protected final String dni;
    protected final LocalDate fechaNacimiento;
    protected final TipoSangre tipoSangre;

    protected Persona(PersonaBuilder<?, ?> builder) {
        this.nombre = validarString(builder.nombre, "El nombre no puede ser nulo ni vacío");
        this.apellido = validarString(builder.apellido, "El apellido no puede ser nulo ni vacío");
        this.dni = validarDni(builder.dni);
        this.fechaNacimiento = Objects.requireNonNull(builder.fechaNacimiento, "La fecha de nacimiento no puede ser nula");
        this.tipoSangre = Objects.requireNonNull(builder.tipoSangre, "El tipo de sangre no puede ser nulo");
    }

    public String getNombreCompleto() {
        return nombre + " " + apellido;
    }

    public int getEdad() {
        return LocalDate.now().getYear() - fechaNacimiento.getYear();
    }

    private String validarString(String valor, String mensajeError) {
        Objects.requireNonNull(valor, mensajeError);
        if (valor.trim().isEmpty()) {
            throw new IllegalArgumentException(mensajeError);
        }
        return valor;
    }

    private String validarDni(String dni) {
        Objects.requireNonNull(dni, "El DNI no puede ser nulo");
        if (!dni.matches("\\d{7,8}")) {
            throw new IllegalArgumentException("El DNI debe tener 7 u 8 dígitos");
        }
        return dni;
    }

}
