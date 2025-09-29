package entidades;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum EstadoCita {
    PROGRAMADA("Programada"),
    EN_CURSO("En Curso"),
    COMPLETADA("Completada"),
    CANCELADA("Cancelada"),
    NO_ASISTIO("No Asistió");

    private final String descripcion;
}
