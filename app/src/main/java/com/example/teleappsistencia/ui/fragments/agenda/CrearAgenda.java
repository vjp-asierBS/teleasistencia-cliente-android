package com.example.teleappsistencia.ui.fragments.agenda;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.teleappsistencia.R;
import com.example.teleappsistencia.modelos.Agenda;
import com.example.teleappsistencia.modelos.Paciente;
import com.example.teleappsistencia.modelos.Persona;
import com.example.teleappsistencia.modelos.TipoAgenda;
import com.example.teleappsistencia.modelos.Usuario;
import com.example.teleappsistencia.ui.fragments.tipo_agenda.InsertarTipoAgenda;
import com.example.teleappsistencia.ui.fragments.tipo_agenda.ModificarTipoAgenda;
import com.example.teleappsistencia.ui.fragments.tipo_vivienda.servicios.APIService;
import com.example.teleappsistencia.ui.fragments.tipo_vivienda.servicios.ClienteRetrofit;
import com.example.teleappsistencia.utilidades.Constantes;
import com.example.teleappsistencia.utilidades.Utilidad;
import com.example.teleappsistencia.utilidades.dialogs.DatePickerFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CrearAgenda extends Fragment implements View.OnClickListener {

    private Agenda agenda;
    private Paciente paciente;
    private Spinner spinnerNombreYApellidos;
    private Spinner tipos;
    private Spinner prioridad;
    private EditText numExpediente;
    private EditText observaciones;
    private EditText fechaDatePicker;
    private Button buttonEditarTipo;
    private Button buttonBorrarTipo;
    private Button buttonNuevoTipo;
    private Button buttonGuardar;
    private Button buttonVolver;
    private List<TipoAgenda> lTipoAgenda;
    private List<Paciente> lPacientes;
    private ArrayAdapter adapterNombreTiposAgenda;
    private ArrayAdapter adapterPacientes;
    private ArrayAdapter adapterImportanciaTiposAgenda;

    public CrearAgenda() {
    }

    public static CrearAgenda newInstance(Agenda agenda) {
        CrearAgenda fragment = new CrearAgenda();
        Bundle args = new Bundle();
        args.putSerializable(Constantes.AGENDA, agenda);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nueva_agenda, container, false);

        capturarElementos(view);
        asignarListener();
        cargarDatosSpinners();

        return view;
    }

    private void capturarElementos(View view) {
        this.spinnerNombreYApellidos = (Spinner) view.findViewById(R.id.spinnerNombreYApellidos);
        this.tipos = (Spinner) view.findViewById(R.id.spinnerTiposAgenda);
        this.prioridad = (Spinner) view.findViewById(R.id.spinnerPrioridadAgenda);
        this.numExpediente = (EditText) view.findViewById(R.id.editTextNumExpedienteAgenda);
        this.fechaDatePicker = (EditText) view.findViewById(R.id.editTextFechaAgenda);
        this.observaciones = (EditText) view.findViewById(R.id.editTextObservacionesAgenda);
        this.buttonEditarTipo = (Button) view.findViewById(R.id.buttonEditarTipoAgenda);
        this.buttonBorrarTipo = (Button) view.findViewById(R.id.buttonBorrarTipoAgenda);
        this.buttonNuevoTipo = (Button) view.findViewById(R.id.buttonNuevoTipoAgenda);
        this.buttonGuardar = (Button) view.findViewById(R.id.buttonGuardarAgenda);
        this.buttonVolver = (Button) view.findViewById(R.id.buttonVolverAgenda);
    }

    private void asignarListener() {
        this.buttonEditarTipo.setOnClickListener(this);
        this.buttonBorrarTipo.setOnClickListener(this);
        this.buttonNuevoTipo.setOnClickListener(this);
        this.buttonGuardar.setOnClickListener(this);
        this.buttonVolver.setOnClickListener(this);
        this.fechaDatePicker.setOnClickListener(this);
    }

    private void cargarDatosSpinners(){
        cargarDatosPaciente();
        cargarDatosTiposAgendas();
    }

    private void cargarDatosPaciente() {
        APIService apiService = ClienteRetrofit.getInstance().getAPIService();
        Call<List<Object>> call = apiService.getPacientes(Constantes.BEARER_ESPACIO + Utilidad.getToken().getAccess());
        call.enqueue(new Callback<List<Object>>() {
            @Override
            public void onResponse(Call<List<Object>> call, Response<List<Object>> response) {
                if(response.isSuccessful()) {
                    List<Object> lObjetos = response.body();
                    lPacientes = (ArrayList<Paciente>) Utilidad.getObjeto(lObjetos, Constantes.AL_PACIENTE);
                    String[] nombreYApellidos = new String[lPacientes.size()];
                    for (int i = 0; i < lPacientes.size(); i++) {
                        Persona persona = (Persona) Utilidad.getObjeto(lPacientes.get(i).getPersona(), Constantes.PERSONA);
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
                                numExpediente.setEnabled(false);
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
        String[] importanciaTipos = new String[2];
        APIService apiService = ClienteRetrofit.getInstance().getAPIService();
        Call<List<Object>> call = apiService.getTiposAgenda(Constantes.BEARER_ESPACIO + Utilidad.getToken().getAccess());
        call.enqueue(new Callback<List<Object>>() {
            @Override
            public void onResponse(Call<List<Object>> call, Response<List<Object>> response) {
                if(response.isSuccessful()){
                    List<Object> lObjetos = response.body();
                    lTipoAgenda = (ArrayList<TipoAgenda>) Utilidad.getObjeto(lObjetos, Constantes.AL_TIPO_AGENDA);
                    String[] nombresTipos = new String[lTipoAgenda.size()];
                    for (int i = 0; i < lTipoAgenda.size(); i++) {
                        nombresTipos[i] = lTipoAgenda.get(i).getNombre();
                    }
                    adapterNombreTiposAgenda = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, nombresTipos);
                    tipos.setAdapter(adapterNombreTiposAgenda);
                    importanciaTipos[0] = lTipoAgenda.get(tipos.getSelectedItemPosition()).getImportancia();
                    adapterImportanciaTiposAgenda = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, importanciaTipos);
                    prioridad.setAdapter(adapterImportanciaTiposAgenda);
                    prioridad.setEnabled(false);
                    tipos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            String selection = (String) parent.getItemAtPosition(position);
                            if (!TextUtils.isEmpty(selection)) {
                                importanciaTipos[0] = lTipoAgenda.get(tipos.getSelectedItemPosition()).getImportancia();
                                adapterImportanciaTiposAgenda = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, importanciaTipos);
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

    private void guardarDatos() {
            this.agenda = new Agenda();
            this.paciente = lPacientes.get(spinnerNombreYApellidos.getSelectedItemPosition());
            this.agenda.setId_paciente(this.paciente);
            this.agenda.setId_tipoAgenda(lTipoAgenda.get(tipos.getSelectedItemPosition()).getId());
            this.agenda.setFecha_registro(Utilidad.getStringFechaNowYYYYMMDD());
            this.agenda.setFecha_prevista(this.fechaDatePicker.getText().toString());
            this.agenda.setObservaciones(this.observaciones.getText().toString());
    }

    private void persistirDatos() {
        APIService apiService = ClienteRetrofit.getInstance().getAPIService();
        Call<Agenda> call = apiService.addAgenda(this.agenda, Constantes.BEARER_ESPACIO + Utilidad.getToken().getAccess());
        call.enqueue(new Callback<Agenda>() {
            @Override
            public void onResponse(Call<Agenda> call, Response<Agenda> response) {
                if(response.isSuccessful()){
                    Toast.makeText(getContext(), Constantes.AGENDA_GUARDADA,  Toast.LENGTH_LONG).show();
                    volver();
                }
                else{
                    Toast.makeText(getContext(), Constantes.ERROR_CREACION + response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Agenda> call, Throwable t) {
                Toast.makeText(getContext(), Constantes.ERROR_+t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void modificarTipoAgenda() {
        ModificarTipoAgenda modificarTipoAgenda = ModificarTipoAgenda.newInstance(lTipoAgenda.get(tipos.getSelectedItemPosition()));
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_fragment, modificarTipoAgenda)
                .addToBackStack(null)
                .commit();
    }

    private void borrarTipoAgenda() {
        TipoAgenda tipoAgenda;
        tipoAgenda = lTipoAgenda.get(tipos.getSelectedItemPosition());
        APIService apiService = ClienteRetrofit.getInstance().getAPIService();
        Call<ResponseBody> call = apiService.deleteTipoAgendabyId(tipoAgenda.getId(), Constantes.BEARER_ESPACIO + Utilidad.getToken().getAccess());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    Toast.makeText(getContext(), Constantes.TIPO_AGENDA_BORRADO, Toast.LENGTH_LONG).show();
                    cargarDatosTiposAgendas();
                }else{
                    Toast.makeText(getContext(), Constantes.ERROR_BORRADO + response.message(), Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getContext(), Constantes.ERROR_+t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void crearTipoAgenda() {
        InsertarTipoAgenda insertarTipoAgenda = InsertarTipoAgenda.newInstance();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_fragment, insertarTipoAgenda)
                .addToBackStack(null)
                .commit();
    }

    private void showDatePickerDialog() {
        DatePickerFragment newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                // +1 porque Enero es cero
                final String selectedDate = year + "-" + (month+1) + "-" + day;
                fechaDatePicker.setText(selectedDate);
            }
        });

        newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
    }

    private void volver() {
        ListarAgendasDeHoy listarAgendasFragment = ListarAgendasDeHoy.newInstance();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_fragment, listarAgendasFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.editTextFechaAgenda:
                Utilidad.dialogoFecha(getContext(), this.fechaDatePicker);
                break;
            case R.id.buttonEditarTipoAgenda:
                modificarTipoAgenda();
                break;
            case R.id.buttonBorrarTipoAgenda:
                borrarTipoAgenda();
                break;
            case R.id.buttonNuevoTipoAgenda:
                crearTipoAgenda();
                break;
            case R.id.buttonVolverAgenda:
                volver();
                break;
            case R.id.buttonGuardarAgenda:
                guardarDatos();
                persistirDatos();
                break;
        }
    }
}
