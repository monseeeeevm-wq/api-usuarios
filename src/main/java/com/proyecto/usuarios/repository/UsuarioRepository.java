package com.proyecto.usuarios.repository;

import com.proyecto.usuarios.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface UsuarioRepository
        extends JpaRepository<Usuario, Long>, JpaSpecificationExecutor<Usuario> {

    // Llave compuesta: nombre + correo (ambos deben coincidir)
    Optional<Usuario> findByNombreAndCorreo(String nombre, String correo);

    // Para validar unicidad del correo
    boolean existsByCorreo(String correo);
}