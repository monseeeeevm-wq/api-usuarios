package com.proyecto.usuarios.specification;

import com.proyecto.usuarios.dto.request.UsuarioBusquedaRequest;
import com.proyecto.usuarios.entity.Usuario;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public final class UsuarioSpecification {

    private UsuarioSpecification() {
    }

    public static Specification<Usuario> conFiltros(UsuarioBusquedaRequest filtros) {
        return (root, query, cb) -> {
            List<Predicate> predicados = new ArrayList<>();

            // Nombre: coincidencia PARCIAL, sin importar mayúsculas
            if (tieneTexto(filtros.getNombre())) {
                predicados.add(cb.like(
                        cb.lower(root.get("nombre")),
                        "%" + escapar(filtros.getNombre().trim().toLowerCase()) + "%",
                        '\\'));
            }

            // Correo: coincidencia PARCIAL, sin importar mayúsculas
            if (tieneTexto(filtros.getCorreo())) {
                predicados.add(cb.like(
                        cb.lower(root.get("correo")),
                        "%" + escapar(filtros.getCorreo().trim().toLowerCase()) + "%",
                        '\\'));
            }

            // Teléfono: coincidencia EXACTA
            if (tieneTexto(filtros.getTelefono())) {
                predicados.add(cb.equal(root.get("telefono"), filtros.getTelefono().trim()));
            }

            // Activo: coincidencia EXACTA
            if (filtros.getActivo() != null) {
                predicados.add(cb.equal(root.get("activo"), filtros.getActivo()));
            }

            // Todos los filtros enviados se combinan con AND
            return cb.and(predicados.toArray(new Predicate[0]));
        };
    }

    private static boolean tieneTexto(String valor) {
        return valor != null && !valor.isBlank();
    }

    // Evita que el usuario meta comodines (% o _) en el texto de búsqueda
    private static String escapar(String valor) {
        return valor.replace("\\", "\\\\").replace("%", "\\%").replace("_", "\\_");
    }
}