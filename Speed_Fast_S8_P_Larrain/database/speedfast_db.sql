-- =====================================================
-- SpeedFast
-- Script de creación de base de datos
-- =====================================================

CREATE DATABASE IF NOT EXISTS speedfast_db;

USE speedfast_db;

-- =====================================================
-- TABLA REPARTIDOR
-- =====================================================

CREATE TABLE IF NOT EXISTS repartidor (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL
);

-- =====================================================
-- TABLA PEDIDO
-- =====================================================

CREATE TABLE IF NOT EXISTS pedido (
    id INT AUTO_INCREMENT PRIMARY KEY,
    direccion VARCHAR(150) NOT NULL,
    tipo ENUM(
        'COMIDA',
        'ENCOMIENDA',
        'EXPRESS'
    ) NOT NULL,
    estado ENUM(
        'PENDIENTE',
        'EN_REPARTO',
        'ENTREGADO'
    ) NOT NULL DEFAULT 'PENDIENTE'
);

-- =====================================================
-- TABLA ENTREGA
-- =====================================================

CREATE TABLE IF NOT EXISTS entrega (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_pedido INT NOT NULL,
    id_repartidor INT NOT NULL,
    fecha DATE NOT NULL,
    hora TIME NOT NULL,

    CONSTRAINT fk_entrega_pedido
        FOREIGN KEY (id_pedido)
        REFERENCES pedido(id),

    CONSTRAINT fk_entrega_repartidor
        FOREIGN KEY (id_repartidor)
        REFERENCES repartidor(id)
);

USE speedfast_db;

SHOW TABLES;

SHOW CREATE TABLE entrega;