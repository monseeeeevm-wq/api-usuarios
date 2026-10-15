package com.proyecto.usuarios.exception;

public class CorreoDuplicadoException extends RuntimeException {

    public CorreoDuplicadoException() {
        super("El correo ya se encuentra registrado. Intente con otro correo.");
    }
}