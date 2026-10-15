package com.proyecto.usuarios.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsuarioUpdateRequest {

    // ---- Llave para localizar al usuario (obligatorios) ----
    @NotBlank(message = "El nombre es obligatorio y no puede contener solo espacios.")
    private String nombre;

    @NotBlank(message = "El correo electrónico es obligatorio.")
    @Email(message = "El correo electrónico no tiene un formato válido.")
    private String correo;

    // ---- Campos a modificar (opcionales: si no vienen, no se tocan) ----
    @Pattern(regexp = ".*\\S.*", message = "El nuevo nombre no puede contener solo espacios.")
    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres.")
    private String nuevoNombre;

    @Email(message = "El nuevo correo electrónico no tiene un formato válido.")
    @Size(max = 150, message = "El correo no puede exceder 150 caracteres.")
    private String nuevoCorreo;

    @Pattern(regexp = "^[0-9]{10}$", message = "El teléfono debe contener exactamente 10 dígitos.")
    private String telefono;

    private Boolean activo;
}