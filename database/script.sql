-- =====================================================
-- Script de creación de base de datos
-- Proyecto: API REST de administración de usuarios
-- Motor: PostgreSQL 14 o superior
-- =====================================================

-- PASO 1: ejecutar SOLO esta línea conectado a la base "postgres"
-- CREATE DATABASE usuarios_db;

-- PASO 2: conectarse a "usuarios_db" y ejecutar lo siguiente

CREATE TABLE IF NOT EXISTS usuarios (
    id             BIGSERIAL    PRIMARY KEY,
    nombre         VARCHAR(100) NOT NULL,
    correo         VARCHAR(150) NOT NULL,
    telefono       VARCHAR(10)  NOT NULL,
    activo         BOOLEAN      NOT NULL DEFAULT TRUE,
    fecha_creacion TIMESTAMP    NOT NULL DEFAULT NOW(),
    CONSTRAINT uk_usuarios_correo UNIQUE (correo)
);

-- Índice para acelerar la consulta por llave compuesta (nombre + correo)
CREATE INDEX IF NOT EXISTS idx_usuarios_nombre_correo ON usuarios (nombre, correo);