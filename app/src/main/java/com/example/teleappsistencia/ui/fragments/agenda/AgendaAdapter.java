package com.example.teleappsistencia.ui.fragments.agenda;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.teleappsistencia.R;
import com.example.teleappsistencia.modelos.Agenda;
import com.example.teleappsistencia.modelos.Paciente;
import com.example.teleappsistencia.modelos.Persona;
import com.example.teleappsistencia.modelos.TipoAgenda;
import com.example.teleappsistencia.utilidades.Constantes;
import com.example.teleappsistencia.utilidades.Utilidad;

import java.util.List;

/**
 * Este adapter será el que carge las tarjetas en la lista. Las tarjetas serán sobre las agendas que
 * le pasemos en el adapter.
 */
public class AgendaAdapter extends RecyclerView.Adapter<AgendaAdapter.AgendaViewHolder> {
    private List<Agenda> items;

    public static class AgendaViewHolder extends RecyclerView.ViewHolder {
        // Campos respectivos de un item.
        private Context context;
        private TextView prioridadAgenda;
        private TextView nombreUsuarioAgenda;
        private TextView apellidosUsuarioAgenda;
        private TextView tipoAgenda;
        private TextView telef;
        private TextView numExpediente;
        private Agenda agenda;

        public AgendaViewHolder(View v) {
            super(v);
            this.context = v.getContext();
            // Capturamos los elementos del layout
            this.prioridadAgenda = (TextView) v.findViewById(R.id.prioridad_agenda);
            this.nombreUsuarioAgenda = (TextView) v.findViewById(R.id.nombre_usuario_agenda);
            this.apellidosUsuarioAgenda = (TextView) v.findViewById(R.id.apellidos_usuario_agenda);
            this.tipoAgenda = (TextView) v.findViewById(R.id.tipo_agenda);
            this.telef = (TextView) v.findViewById(R.id.telefono);
            this.numExpediente = (TextView) v.findViewById(R.id.num_expediente);
        }

        // Setter para poder pasarle la agenda desde del Adapter
        public void setAgenda(Agenda agenda) {
            this.agenda = agenda;
        }
    }

    /**
     * Se le carga la lista de items al Adapter, en este caso de Agenda
     *
     * @param items
     */
    public AgendaAdapter(List<Agenda> items) {
        this.items = items;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public AgendaAdapter.AgendaViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.fragment_agenda_card, viewGroup, false);
        return new AgendaAdapter.AgendaViewHolder(v);
    }

    @Override
    public void onBindViewHolder(AgendaViewHolder viewHolder, int i) {
        // En el bind, le cargamos los atributos al layout de la tarjeta
        Agenda agenda = items.get(i);
        viewHolder.setAgenda(agenda);
        Paciente paciente = (Paciente) Utilidad.getObjeto(agenda.getId_paciente(), Constantes.PACIENTE);
        Persona persona = (Persona) paciente.getPersona();
        TipoAgenda tipo = (TipoAgenda) Utilidad.getObjeto(agenda.getId_tipoAgenda(), Constantes.TIPOAGENDA);
        viewHolder.prioridadAgenda.setText(Constantes.PRIORIDAD_DP_SP + tipo.getImportancia());
        viewHolder.nombreUsuarioAgenda.setText(Constantes.NOMBRE_DP_SP + persona.getNombre());
        viewHolder.apellidosUsuarioAgenda.setText(Constantes.FECHA_DP_SP + persona.getApellidos());
        viewHolder.tipoAgenda.setText(Constantes.TIPO_DP_SP + tipo.getNombre());
        viewHolder.telef.setText(Constantes.TELEFONO_DP_SP + persona.getTelefonoMovil());
        viewHolder.numExpediente.setText(Constantes.NUM_EXPEDIENTE_DS_SP + paciente.getNumeroExpediente());

        if (tipo != null) { // Control de error si el tipo es nulo.
            viewHolder.tipoAgenda.setText(Constantes.TIPO_DP_SP + tipo.getNombre());
        } else {
            viewHolder.tipoAgenda.setText(Constantes.TIPO_DP_SP + Constantes.ESPACIO_EN_BLANCO);
        }
    }
}
