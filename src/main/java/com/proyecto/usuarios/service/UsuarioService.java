package com.proyecto.usuarios.service;

import com.proyecto.usuarios.dto.request.UsuarioBusquedaRequest;
import com.proyecto.usuarios.dto.request.UsuarioConsultaRequest;
import com.proyecto.usuarios.dto.request.UsuarioCreateRequest;
import com.proyecto.usuarios.dto.request.UsuarioEliminacionRequest;
import com.proyecto.usuarios.dto.request.UsuarioUpdateRequest;
import com.proyecto.usuarios.dto.response.UsuarioResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UsuarioService {

    UsuarioResponse crear(UsuarioCreateRequest request);

    Page<UsuarioResponse> listar(Pageable pageable);

    UsuarioResponse consultar(UsuarioConsultaRequest request);

    UsuarioResponse actualizar(UsuarioUpdateRequest request);

    void eliminar(UsuarioEliminacionRequest request);

    Page<UsuarioResponse> buscar(UsuarioBusquedaRequest request);
}