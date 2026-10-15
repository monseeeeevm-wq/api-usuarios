package com.proyecto.usuarios.exception;

import com.proyecto.usuarios.dto.response.ApiErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 400 - Errores de Bean Validation
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> manejarValidacion(MethodArgumentNotValidException ex) {
        Map<String, String> errores = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(error -> errores.putIfAbsent(error.getField(), error.getDefaultMessage()));

        ApiErrorResponse respuesta = ApiErrorResponse.builder()
                .codigo("DATOS_INVALIDOS")
                .mensaje("Verifique la información capturada e intente nuevamente.")
                .errores(errores)
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(respuesta);
    }

    // 400 - JSON mal formado o body ausente
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiErrorResponse> manejarJsonInvalido(HttpMessageNotReadableException ex) {
        ApiErrorResponse respuesta = ApiErrorResponse.builder()
                .codigo("SOLICITUD_INVALIDA")
                .mensaje("El cuerpo de la solicitud es inválido o está vacío.")
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(respuesta);
    }

    // 404 - Usuario no encontrado
    @ExceptionHandler(UsuarioNoEncontradoException.class)
    public ResponseEntity<ApiErrorResponse> manejarUsuarioNoEncontrado(UsuarioNoEncontradoException ex) {
        ApiErrorResponse respuesta = ApiErrorResponse.builder()
                .codigo("USUARIO_NO_ENCONTRADO")
                .mensaje(ex.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(respuesta);
    }

    // 409 - Correo duplicado
    @ExceptionHandler(CorreoDuplicadoException.class)
    public ResponseEntity<ApiErrorResponse> manejarCorreoDuplicado(CorreoDuplicadoException ex) {
        ApiErrorResponse respuesta = ApiErrorResponse.builder()
                .codigo("CORREO_DUPLICADO")
                .mensaje(ex.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.CONFLICT).body(respuesta);
    }

    // 409 - Violación de integridad en BD (sin exponer SQL ni nombres internos)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiErrorResponse> manejarIntegridad(DataIntegrityViolationException ex) {
        log.warn("Violación de integridad de datos: {}", ex.getMostSpecificCause().getMessage());
        ApiErrorResponse respuesta = ApiErrorResponse.builder()
                .codigo("ERROR_INTEGRIDAD_DATOS")
                .mensaje("La operación no se pudo completar porque viola una restricción de los datos.")
                .build();
        return ResponseEntity.status(HttpStatus.CONFLICT).body(respuesta);
    }

    // 405 - Método HTTP no permitido
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiErrorResponse> manejarMetodoNoPermitido(HttpRequestMethodNotSupportedException ex) {
        ApiErrorResponse respuesta = ApiErrorResponse.builder()
                .codigo("METODO_NO_PERMITIDO")
                .mensaje("El método HTTP utilizado no está permitido para este recurso.")
                .build();
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(respuesta);
    }

    // 404 - Ruta inexistente
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ApiErrorResponse> manejarRutaInexistente(NoResourceFoundException ex) {
        ApiErrorResponse respuesta = ApiErrorResponse.builder()
                .codigo("RECURSO_NO_ENCONTRADO")
                .mensaje("El recurso solicitado no existe.")
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(respuesta);
    }

    // 500 - Cualquier error inesperado
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> manejarErrorInesperado(Exception ex) {
        log.error("Error inesperado", ex);
        ApiErrorResponse respuesta = ApiErrorResponse.builder()
                .codigo("ERROR_INTERNO")
                .mensaje("Ocurrió un error inesperado. Intente nuevamente más tarde.")
                .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respuesta);
    }
}