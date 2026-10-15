package com.proyecto.usuarios.entity;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
public abstract class EntidadBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    // La fecha la controla el servidor, nunca el cliente
    @PrePersist
    protected void alCrear() {
        this.fechaCreacion = LocalDateTime.now();
    }

    // Método abstracto: cada entidad lo implementa a su manera (polimorfismo)
    public abstract String descripcion();
}