package com.ctrlplus.controlplus.enums;

public enum Categoria {
    ALQUILER("Alquiler"),
    ALIMENTOS("Alimentos"),
    MOVILIDAD("Movilidad"),
    SALUD("Salud"),
    COMPRAS("Compras"),
    SERVICIOS("Servicios"),
    IMPUESTOS("Impuestos"),
    AHORROS("Ahorros"),
    DEPORTES("Deportes"),
    TARJETAS_DE_CREDITOS("Tarjetas de crédito"),
    DONACIONES("Donar a Maratea"),
    PRESTAMOS("Préstamos"),
    OTROS("Otros");
    
    private final String nombre;

    private Categoria(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }
}
