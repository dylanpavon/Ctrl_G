package com.ctrlplus.controlplus.enums;

public enum CategoriaIngreso {
    SUELDO("Sueldo"),
    PRESTAMO("Prestamo"),
    VENTAS("Ventas"),
    OTROS("Otros");
    
    private final String nombre;

    private CategoriaIngreso(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }
}
