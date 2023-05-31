package com.example.teleappsistencia.ui.fragments.agenda;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.teleappsistencia.R;
import com.example.teleappsistencia.modelos.Agenda;
import com.example.teleappsistencia.modelos.Paciente;
import com.example.teleappsistencia.modelos.Persona;
import com.example.teleappsistencia.modelos.TipoAgenda;
import com.example.teleappsistencia.ui.fragments.tipo_vivienda.servicios.APIService;
import com.example.teleappsistencia.ui.fragments.tipo_vivienda.servicios.ClienteRetrofit;
import com.example.teleappsistencia.utilidades.Constantes;
import com.example.teleappsistencia.utilidades.Utilidad;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MostrarInfoAgenda extends Fragment implements View.OnClickListener {

    private Agenda agenda;
    private Paciente paciente;
    private TextView id;
    private TextView nombre;
    private TextView numExpediente;
    private TextView tipo;
    private TextView prioridad;
    private TextView fecha_prevista;
    private TextView fecha_resolucion;
    private TextView observaciones;
    private TextView telef;
    private Button buttonVolver;

    public MostrarInfoAgenda() {
    }

    public static MostrarInfoAgenda newInstance(Agenda agenda) {
        MostrarInfoAgenda fragment = new MostrarInfoAgenda();
        Bundle args = new Bundle();
        args.putSerializable(Constantes.AGENDA, agenda);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Comprobamos que la instancia se ha creado con argumentos y si es así las recogemos.
        if (getArguments() != null) {
            this.agenda = (Agenda) getArguments().getSerializable(Constantes.ARG_AGENDA);
        }
    }

    private void capturarElementos(View view) {
        this.id = (TextView) view.findViewById(R.id.infoId);
        this.nombre = (TextView) view.findViewById(R.id.infoNombre);
        this.numExpediente = (TextView) view.findViewById(R.id.infoNumExpediente);
        this.tipo = (TextView) view.findViewById(R.id.infoTipo);
        this.prioridad = (TextView) view.findViewById(R.id.infoPrioridad);
        this.fecha_prevista = (TextView) view.findViewById(R.id.infoFechaPrevista);
        this.fecha_resolucion = (TextView) view.findViewById(R.id.infoFechaResolucion);
        this.observaciones = (TextView) view.findViewById(R.id.infoObservaciones);
        this.telef = (TextView) view.findViewById(R.id.infoTelefono);
        this.buttonVolver = (Button) view.findViewById(R.id.button_volverEditAgenda);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragmen
        View view = inflater.inflate(R.layout.fragment_mostrar_info_agenda, container, false);

        capturarElementos(view);

        //Cargamos los datos
        if(this.agenda != null) {
            //cargarDatos();
        }

        return view;
    }
    /*
    private void cargarDatos() {
        this.observaciones.setText(this.agenda.getObservaciones());
        this.fecha_prevista.setText(""+this.agenda.getFecha_prevista());
        this.fecha_resolucion.setText(""+this.agenda.getFecha_resolucion());
        cargarPaciente();
        cargarTipoAgenda();
    }

    private void cargarPaciente() {
        APIService apiService = ClienteRetrofit.getInstance().getAPIService();
        Call<List<Object>> call = apiService.getPacienteById(Constantes.BEARER_ESPACIO + Utilidad.getToken().getAccess());
        call.enqueue(new Callback<List<Object>>() {
            @Override
            public void onResponse(Call<List<Object>> call, Response<List<Object>> response) {
                if(response.isSuccessful()) {
                    List<Object> lObjetos = response.body();
                    lPacientes = (ArrayList<Paciente>) Utilidad.getObjeto(lObjetos, Constantes.AL_PACIENTE);
                    for (int i = 0; i < lPacientes.size(); i++) {
                        Persona persona = (Persona) lPacientes.get(i).getPersona();
                        nombreYApellidos[i] = persona.getNombre().concat(" ").concat(persona.getApellidos());
                    }
                    adapterPacientes = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, nombreYApellidos);
                    spinnerNombreYApellidos.setAdapter(adapterPacientes);
                    spinnerNombreYApellidos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            String selection = (String) parent.getItemAtPosition(position);
                            if (!TextUtils.isEmpty(selection)) {
                                numExpediente.setText(lPacientes.get(position).getNumeroExpediente());
                            }
                        }

                        // Debido a que el AdapterView es una abstract class, onNothingSelected debe ser tambien definido
                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                        }
                    });
                }else{
                    Toast.makeText(getContext(), Constantes.ERROR_ + response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<Object>> call, Throwable t) {
                Toast.makeText(getContext(), Constantes.ERROR_+t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void cargarDatosTiposAgendas() {
        String[] nombresTipos = null;
        String[] importanciaTipos = null;
        APIService apiService = ClienteRetrofit.getInstance().getAPIService();
        Call<List<Object>> call = apiService.getTiposAgenda(Constantes.BEARER_ESPACIO + Utilidad.getToken().getAccess());
        call.enqueue(new Callback<List<Object>>() {
            @Override
            public void onResponse(Call<List<Object>> call, Response<List<Object>> response) {
                if(response.isSuccessful()){
                    List<Object> lObjetos = response.body();
                    lTipoAgenda = (ArrayList<TipoAgenda>) Utilidad.getObjeto(lObjetos, Constantes.AL_TIPO_AGENDA);
                    for (int i = 0; i < lTipoAgenda.size(); i++) {
                        nombresTipos[i] = lTipoAgenda.get(i).getNombre();
                    }
                    adapterNombreTiposAgenda = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, nombresTipos);
                    tipos.setAdapter(adapterNombreTiposAgenda);
                    TipoAgenda tipoSeleccionado = (TipoAgenda) tipos.getSelectedItem();
                    importanciaTipos[0] = tipoSeleccionado.getImportancia();
                    adapterImportanciaTiposAgenda = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, importanciaTipos);
                    prioridad.setAdapter(adapterImportanciaTiposAgenda);
                    tipos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            String selection = (String) parent.getItemAtPosition(position);
                            if (!TextUtils.isEmpty(selection)) {
                                String[] importancia = null;
                                importancia[0]=lTipoAgenda.get(position).getImportancia();
                                adapterImportanciaTiposAgenda = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, importancia);
                                prioridad.setAdapter(adapterImportanciaTiposAgenda);
                            }
                        }

                        // Debido a que el AdapterView es una abstract class, onNothingSelected debe ser tambien definido
                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                        }
                    });
                }else{
                    Toast.makeText(getContext(), Constantes.ERROR_ + response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<Object>> call, Throwable t) {
                Toast.makeText(getContext(), Constantes.ERROR_+t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
*/
    private void volver(){
        ListarAgendasDeHoy listarAgendasDeHoy = new ListarAgendasDeHoy();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_fragment, listarAgendasDeHoy)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onClick(View view) {
        volver();
    }
}