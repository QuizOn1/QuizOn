package com.example.informaticapp;

import java.io.Serializable;

public class AsesoriaItem implements Serializable {
    private String nombre;
    private String correo;
    private String fecha;
    private String materia;
    private String tema;

    public AsesoriaItem(String nombre, String correo, String fecha, String materia, String tema) {
        this.nombre = nombre;
        this.correo = correo;
        this.fecha = fecha;
        this.materia = materia;
        this.tema = tema;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public String getFecha() {
        return fecha;
    }

    public String getMateria() {
        return materia;
    }
    public String getTema() {
        return tema;
    }
}
