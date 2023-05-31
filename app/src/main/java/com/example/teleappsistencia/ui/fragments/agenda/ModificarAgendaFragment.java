package com.example.teleappsistencia.ui.fragments.agenda;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ModificarAgendaFragment extends Fragment implements View.OnClickListener {

    private Agenda agenda;
    private Spinner spinnerNombreYApellidos;
    private Spinner tipos;
    private Spinner prioridad;
    private EditText numExpediente;
    private EditText fecha;
    private EditText observaciones;
    private Button buttonGuardar;
    private Button buttonVolver;
    private ArrayList<TipoAgenda> lTipoAgenda;
    private ArrayList<Paciente> lPacientes;
    private Paciente paciente;
    private TipoAgenda tipoAgenda;
    private ArrayAdapter adapterNombreTiposAgenda;
    private ArrayAdapter adapterPacientes;
    private ArrayAdapter adapterImportanciaTiposAgenda;

    public static ModificarAgendaFragment newInstance(Agenda agenda) {
        ModificarAgendaFragment fragment = new ModificarAgendaFragment();
        Bundle args = new Bundle();
        args.putSerializable(Constantes.AGENDA, agenda);
        fragment.setArguments(args);
        return fragment;
    }

    public ModificarAgendaFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Comprobamos que la instancia se ha creado con argumentos y si es así las recogemos.
        if (getArguments() != null) {
            this.agenda = (Agenda) getArguments().getSerializable(Constantes.ARG_AGENDA);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragmen
        View view = inflater.inflate(R.layout.fragment_modificar_agenda, container, false);

        // Capturar los elementos del layout
        capturarElementos(view);

        //Asignamos el listener a los botones
        asignarListener();

        //Cargamos los datos
        if(this.agenda != null) {
            cargarDatos();
        }

        return view;
    }

    /**
     * Este método captura los elementos que hay en el layout correspondiente.
     * @param view
     */
    private void capturarElementos(View view) {
        this.spinnerNombreYApellidos = (Spinner) view.findViewById(R.id.spinnerNombreYApellidosModificar);
        this.tipos = (Spinner) view.findViewById(R.id.spinnerTiposAgendaModificar);
        this.prioridad = (Spinner) view.findViewById(R.id.spinnerPrioridadAgendaModificar);
        this.numExpediente = (EditText) view.findViewById(R.id.editTextNumExpedienteAgendaModificar);
        this.fecha = (EditText) view.findViewById(R.id.editTextFechaModificarAgenda);
        this.observaciones = (EditText) view.findViewById(R.id.editTextObservacionesAgendaModificar);
        this.buttonGuardar = (Button) view.findViewById(R.id.buttonGuardarAgendaModificada);
        this.buttonVolver = (Button) view.findViewById(R.id.button_volverEditAgenda);
    }

    private void asignarListener() {
        this.buttonGuardar.setOnClickListener(this);
        this.buttonVolver.setOnClickListener(this);
        this.fecha.setOnClickListener(this);
    }

    private void cargarDatos() {
        this.observaciones.setText(this.agenda.getObservaciones());
        this.fecha.setText(""+this.agenda.getFecha_prevista());
        cargarDatosPaciente();
        cargarDatosTiposAgendas();
    }

    private void cargarDatosPaciente() {
        String[] nombreYApellidos = null;
        APIService apiService = ClienteRetrofit.getInstance().getAPIService();
        Call<List<Object>> call = apiService.getPacientes(Constantes.BEARER_ESPACIO + Utilidad.getToken().getAccess());
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

    private void modificarDatos() {
            this.paciente = lPacientes.get(spinnerNombreYApellidos.getSelectedItemPosition());
            this.tipoAgenda = lTipoAgenda.get(tipos.getSelectedItemPosition());
            this.agenda.setFecha_prevista(this.fecha.getText().toString());
            this.agenda.setObservaciones(this.observaciones.getText().toString());
            this.agenda.setId_tipoAgenda(lTipoAgenda.get(tipos.getSelectedItemPosition()).getId());
            paciente.setNumeroExpediente(this.numExpediente.getText().toString());
            tipoAgenda.setImportancia((String) prioridad.getSelectedItem());
    }

    private void persistirAgenda(){
        APIService apiService = ClienteRetrofit.getInstance().getAPIService();
        Call<ResponseBody> call = apiService.actualizarAgenda(agenda.getId(), Constantes.BEARER_ESPACIO + Utilidad.getToken().getAccess(), agenda);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    Toast.makeText(getContext(), Constantes.AGENDA_MODIFICADA, Toast.LENGTH_LONG).show();
                    volver();
                }
                else{
                    Toast.makeText(getContext(), Constantes.ERROR_MODIFICACION + Constantes.PISTA_TELEOPERADOR_ID , Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getContext(), Constantes.ERROR_ +t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void persistirPaciente() {
        APIService apiService = ClienteRetrofit.getInstance().getAPIService();
        Call<Paciente> call = apiService.updatePaciente(paciente.getId(), paciente, Constantes.BEARER_ESPACIO + Utilidad.getToken().getAccess());
        call.enqueue(new Callback<Paciente>() {
            @Override
            public void onResponse(Call<Paciente> call, Response<Paciente> response) {
                if(response.isSuccessful()){
                    Toast.makeText(getContext(), Constantes.AGENDA_MODIFICADA, Toast.LENGTH_LONG).show();
                    volver();
                }
                else{
                    Toast.makeText(getContext(), Constantes.ERROR_MODIFICACION + Constantes.PISTA_TELEOPERADOR_ID , Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<Paciente> call, Throwable t) {
                Toast.makeText(getContext(), Constantes.ERROR_ +t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void volver(){
        ListarAgendasDeHoy listarAgendasDeHoy = new ListarAgendasDeHoy();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_fragment, listarAgendasDeHoy)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.buttonGuardarAgendaModificada:
                modificarDatos();
                persistirAgenda();
                persistirPaciente();
                break;
            case R.id.button_volverEditAgenda:
                volver();
                break;
        }
    }
}