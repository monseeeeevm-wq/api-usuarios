package com.proyecto.usuarios.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsuarioBusquedaRequest {

    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres.")
    private String nombre;

    @Size(max = 150, message = "El correo no puede exceder 150 caracteres.")
    private String correo;

    @Size(max = 10, message = "El teléfono no puede exceder 10 dígitos.")
    private String telefono;

    private Boolean activo;

    @Min(value = 0, message = "La página no puede ser negativa.")
    private Integer pagina = 0;

    @Min(value = 1, message = "El tamaño mínimo es 1.")
    @Max(value = 50, message = "El tamaño máximo es 50.")
    private Integer tamano = 10;
}