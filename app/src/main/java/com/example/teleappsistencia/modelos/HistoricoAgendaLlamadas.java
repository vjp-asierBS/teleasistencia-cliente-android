package com.example.teleappsistencia.modelos;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class HistoricoAgendaLlamadas implements Serializable {

    @SerializedName("id")
    private int id;
    @SerializedName("id_agenda")
    private Object idAgenda;
    @SerializedName("id_teleoperador")
    private Object idTeleoperador;
    @SerializedName("observaciones")
    private String observaciones;

    public HistoricoAgendaLlamadas() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Object getIdAgenda() {
        return idAgenda;
    }

    public void setIdAgenda(Object idAgenda) {
        this.idAgenda = idAgenda;
    }

    public Object getIdTeleoperador() {
        return idTeleoperador;
    }

    public void setIdTeleoperador(Object idTeleoperador) {
        this.idTeleoperador = idTeleoperador;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    @Override
    public String toString() {
        return "HistoricoAgendaLlamadas{" +
                "id=" + id +
                ", idAgenda=" + idAgenda +
                ", idTeleoperador=" + idTeleoperador +
                ", observaciones=" + observaciones +
                '}';
    }
}
