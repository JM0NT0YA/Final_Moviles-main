package com.maga.myapplication;

import com.google.firebase.Timestamp;

/**
 * Clase que representa una Nota con título, descripción, fecha y marca de tiempo.
 */
public class Nota {
    // Campos que representan los atributos de una nota
    String titulo;
    String descripcion;
    String fecha; // Campo que almacena la fecha en formato de texto
    Timestamp timestamp; // Marca de tiempo de Firebase para la nota

    /**
     * Constructor sin argumentos requerido por Firestore.
     */
    public Nota() {
        // Constructor vacío requerido por Firestore para deserialización
    }

    /**
     * Obtiene el título de la nota.
     * @return El título de la nota.
     */
    public String getTitulo() {
        return titulo;
    }

    /**
     * Establece el título de la nota.
     * @param titulo El título de la nota.
     */
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    /**
     * Obtiene la descripción de la nota.
     * @return La descripción de la nota.
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Establece la descripción de la nota.
     * @param descripcion La descripción de la nota.
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * Obtiene la marca de tiempo de la nota.
     * @return La marca de tiempo de la nota.
     */
    public Timestamp getTimestamp() {
        return timestamp;
    }

    /**
     * Establece la marca de tiempo de la nota.
     * @param timestamp La marca de tiempo de la nota.
     */
    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * Obtiene la fecha de la nota en formato de texto.
     * @return La fecha de la nota.
     */
    public String getFecha() {
        return fecha;
    }

    /**
     * Establece la fecha de la nota en formato de texto.
     * @param fecha La fecha de la nota.
     */
    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
}