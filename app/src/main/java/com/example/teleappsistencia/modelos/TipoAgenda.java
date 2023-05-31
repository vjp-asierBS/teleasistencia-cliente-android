package com.example.teleappsistencia.modelos;

import com.example.teleappsistencia.utilidades.Constantes;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Clase encargada del modelo de un TipoAgenda.
 */
public class TipoAgenda implements Serializable {

    @SerializedName("id")
    private int id;
    @SerializedName("nombre")
    private String nombre;
    @SerializedName("codigo")
    private int codigo;
    @SerializedName("importancia")
    private String importancia;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getImportancia() {
        return importancia;
    }

    public void setImportancia(String importancia) {
        this.importancia = importancia;
    }

    @Override
    public String toString() {
        return getCodigo()+ Constantes.ESPACIO_GUION_ESPACIO +getNombre();
    }
}
