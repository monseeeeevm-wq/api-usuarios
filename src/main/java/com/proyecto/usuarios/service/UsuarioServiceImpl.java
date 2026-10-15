package com.proyecto.usuarios.service;

import com.proyecto.usuarios.dto.request.UsuarioBusquedaRequest;
import com.proyecto.usuarios.dto.request.UsuarioConsultaRequest;
import com.proyecto.usuarios.dto.request.UsuarioCreateRequest;
import com.proyecto.usuarios.dto.request.UsuarioEliminacionRequest;
import com.proyecto.usuarios.dto.request.UsuarioUpdateRequest;
import com.proyecto.usuarios.dto.response.UsuarioResponse;
import com.proyecto.usuarios.entity.Usuario;
import com.proyecto.usuarios.exception.CorreoDuplicadoException;
import com.proyecto.usuarios.exception.UsuarioNoEncontradoException;
import com.proyecto.usuarios.repository.UsuarioRepository;
import com.proyecto.usuarios.specification.UsuarioSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;

    @Override
    @Transactional
    public UsuarioResponse crear(UsuarioCreateRequest request) {
        String correo = request.getCorreo().trim();

        if (usuarioRepository.existsByCorreo(correo)) {
            throw new CorreoDuplicadoException();
        }

        Usuario usuario = Usuario.builder()
                .nombre(request.getNombre().trim())
                .correo(correo)
                .telefono(request.getTelefono())
                .activo(true)
                .build();

        return aResponse(usuarioRepository.save(usuario));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UsuarioResponse> listar(Pageable pageable) {
        return usuarioRepository.findAll(pageable).map(this::aResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public UsuarioResponse consultar(UsuarioConsultaRequest request) {
        return aResponse(buscarPorLlave(request.getNombre(), request.getCorreo()));
    }

    @Override
    @Transactional
    public UsuarioResponse actualizar(UsuarioUpdateRequest request) {
        Usuario usuario = buscarPorLlave(request.getNombre(), request.getCorreo());

        // Regla: campo omitido o null = no se modifica (evita sobrescribir con nulos)
        if (request.getNuevoNombre() != null) {
            usuario.setNombre(request.getNuevoNombre().trim());
        }

        if (request.getNuevoCorreo() != null) {
            String nuevoCorreo = request.getNuevoCorreo().trim();
            if (!nuevoCorreo.equals(usuario.getCorreo())
                    && usuarioRepository.existsByCorreo(nuevoCorreo)) {
                throw new CorreoDuplicadoException();
            }
            usuario.setCorreo(nuevoCorreo);
        }

        if (request.getTelefono() != null) {
            usuario.setTelefono(request.getTelefono());
        }

        if (request.getActivo() != null) {
            usuario.setActivo(request.getActivo());
        }

        return aResponse(usuarioRepository.save(usuario));
    }

    @Override
    @Transactional
    public void eliminar(UsuarioEliminacionRequest request) {
        Usuario usuario = buscarPorLlave(request.getNombre(), request.getCorreo());
        usuarioRepository.delete(usuario); // eliminación física
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UsuarioResponse> buscar(UsuarioBusquedaRequest request) {
        int pagina = request.getPagina() != null ? request.getPagina() : 0;
        int tamano = request.getTamano() != null ? request.getTamano() : 10;

        Pageable pageable = PageRequest.of(pagina, tamano, Sort.by("id").ascending());

        return usuarioRepository
                .findAll(UsuarioSpecification.conFiltros(request), pageable)
                .map(this::aResponse);
    }

    // ---------- Métodos privados ----------

    private Usuario buscarPorLlave(String nombre, String correo) {
        return usuarioRepository
                .findByNombreAndCorreo(nombre.trim(), correo.trim())
                .orElseThrow(UsuarioNoEncontradoException::new);
    }

    private UsuarioResponse aResponse(Usuario usuario) {
        return UsuarioResponse.builder()
                .id(usuario.getId())
                .nombre(usuario.getNombre())
                .correo(usuario.getCorreo())
                .telefono(usuario.getTelefono())
                .activo(usuario.getActivo())
                .fechaCreacion(usuario.getFechaCreacion())
                .build();
    }
}