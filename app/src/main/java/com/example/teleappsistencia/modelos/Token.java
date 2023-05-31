package com.example.teleappsistencia.modelos;

import java.io.Serializable;

/**
 * Clase POJO "Token" utilizada para parsear la respuesta JSON del servidor.
 */
public class Token implements Serializable {

    /**
     * Atributos de la clase POJO con sus anotaciones GSON correspondientes,
     * que se utilizan para mapear las JSON keys hacia campos Java.
     */

    private String refresh;
    private String access;

    // Getters y Setters
    
    public String getRefresh() {
        return refresh;
    }

    public void setRefresh(String refresh) {
        this.refresh = refresh;
    }

    public String getAccess() {
        return access;
    }

    public void setAccess(String access) {
        this.access = access;
    }

    /**
     * Método toString
     * @return
     */
    @Override
    public String toString() {
        return "Token{" +
                "refresh='" + refresh + '\'' +
                ", access='" + access + '\'' +
                '}';
    }
}
