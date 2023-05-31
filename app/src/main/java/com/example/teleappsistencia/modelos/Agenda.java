package com.example.teleappsistencia.modelos;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Agenda implements Serializable {
    @SerializedName("id")
    private int id;
    @SerializedName("id_paciente")
    private Object id_paciente;
    @SerializedName("id_tipo_agenda")
    private int id_tipoAgenda;
    @SerializedName("historico_agenda")
    private ArrayList<HistoricoAgendaLlamadas> historicoAgendaLlamadas;
    @SerializedName("fecha_registro")
    private String fecha_registro;
    @SerializedName("fecha_prevista")
    private String fecha_prevista;
    @SerializedName("fecha_resolucion")
    private String fecha_resolucion;
    @SerializedName("observaciones")
    private String observaciones;

    public Agenda() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Object getId_paciente() {
        return id_paciente;
    }

    public void setId_paciente(Object id_paciente) {
        this.id_paciente = id_paciente;
    }

    public int getId_tipoAgenda() {
        return id_tipoAgenda;
    }

    public void setId_tipoAgenda(int id_tipoAgenda) {
        this.id_tipoAgenda = id_tipoAgenda;
    }

    public String getFecha_registro() {
        return fecha_registro;
    }

    public void setFecha_registro(String fecha_registro) {
        this.fecha_registro = fecha_registro;
    }

    public String getFecha_prevista() {
        return fecha_prevista;
    }

    public void setFecha_prevista(String fecha_prevista) {
        this.fecha_prevista = fecha_prevista;
    }

    public String getFecha_resolucion() {
        return fecha_resolucion;
    }

    public void setFecha_resolucion(String fecha_resolucion) {
        this.fecha_resolucion = fecha_resolucion;
    }

    public ArrayList<HistoricoAgendaLlamadas> getHistoricoAgendaLlamadas() {
        return historicoAgendaLlamadas;
    }

    public void setHistoricoAgendaLlamadas(ArrayList<HistoricoAgendaLlamadas> historicoAgendaLlamadas) {
        this.historicoAgendaLlamadas = historicoAgendaLlamadas;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}
