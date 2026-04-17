package com.example.ticket.infrastructure.adapter.in.web;

// Clase encargada de mapear los datos recibidos del frontend para generar un nuevo ticket
public class CrearTicketRequest {

    private String titulo;
    private String descripcion;

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}