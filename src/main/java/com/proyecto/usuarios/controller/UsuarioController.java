package com.proyecto.usuarios.controller;

import com.proyecto.usuarios.dto.request.UsuarioBusquedaRequest;
import com.proyecto.usuarios.dto.request.UsuarioConsultaRequest;
import com.proyecto.usuarios.dto.request.UsuarioCreateRequest;
import com.proyecto.usuarios.dto.request.UsuarioEliminacionRequest;
import com.proyecto.usuarios.dto.request.UsuarioUpdateRequest;
import com.proyecto.usuarios.dto.response.ApiSuccessResponse;
import com.proyecto.usuarios.dto.response.UsuarioResponse;
import com.proyecto.usuarios.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/usuarios")
@RequiredArgsConstructor
@Tag(name = "Usuarios", description = "Administración de usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    @PostMapping
    @Operation(summary = "Crear usuario",
            description = "Registra un usuario nuevo. Devuelve 409 si el correo ya existe.")
    public ResponseEntity<ApiSuccessResponse<UsuarioResponse>> crear(
            @Valid @RequestBody UsuarioCreateRequest request) {
        UsuarioResponse creado = usuarioService.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiSuccessResponse<>("Usuario registrado correctamente.", creado));
    }

    @GetMapping
    @Operation(summary = "Listar usuarios",
            description = "Devuelve el listado de usuarios de forma paginada.")
    public ResponseEntity<PagedModel<UsuarioResponse>> listar(
            @ParameterObject @PageableDefault(size = 10, sort = "id") Pageable pageable) {
        return ResponseEntity.ok(new PagedModel<>(usuarioService.listar(pageable)));
    }

    @PostMapping("/consulta")
    @Operation(summary = "Consultar usuario por llave compuesta",
            description = "Busca un usuario por nombre y correo (ambos deben coincidir).")
    public ResponseEntity<UsuarioResponse> consultar(
            @Valid @RequestBody UsuarioConsultaRequest request) {
        return ResponseEntity.ok(usuarioService.consultar(request));
    }

    @PatchMapping("/actualizacion")
    @Operation(summary = "Actualizar usuario parcialmente",
            description = "Localiza al usuario por nombre y correo y modifica solo los campos enviados.")
    public ResponseEntity<UsuarioResponse> actualizar(
            @Valid @RequestBody UsuarioUpdateRequest request) {
        return ResponseEntity.ok(usuarioService.actualizar(request));
    }

    @DeleteMapping("/eliminacion")
    @Operation(summary = "Eliminar usuario",
            description = "Elimina físicamente al usuario que coincida con nombre y correo.")
    public ResponseEntity<Void> eliminar(
            @Valid @RequestBody UsuarioEliminacionRequest request) {
        usuarioService.eliminar(request);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/busqueda")
    @Operation(summary = "Búsqueda dinámica",
            description = "Combina cualquier criterio (nombre, correo, teléfono, activo). "
                    + "Los campos omitidos se excluyen de la consulta.")
    public ResponseEntity<PagedModel<UsuarioResponse>> buscar(
            @Valid @RequestBody UsuarioBusquedaRequest request) {
        return ResponseEntity.ok(new PagedModel<>(usuarioService.buscar(request)));
    }
}