package com.proyecto.usuarios.exception;

public class UsuarioNoEncontradoException extends RuntimeException {

    public UsuarioNoEncontradoException() {
        super("No existe un usuario con el nombre y correo electrónico proporcionados.");
    }
}