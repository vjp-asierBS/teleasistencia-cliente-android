package com.example.teleappsistencia.ui.fragments.agenda;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.teleappsistencia.R;
import com.example.teleappsistencia.modelos.Agenda;
import com.example.teleappsistencia.modelos.HistoricoAgendaLlamadas;
import com.example.teleappsistencia.ui.fragments.tipo_vivienda.servicios.APIService;
import com.example.teleappsistencia.ui.fragments.tipo_vivienda.servicios.ClienteRetrofit;
import com.example.teleappsistencia.utilidades.Constantes;
import com.example.teleappsistencia.utilidades.Utilidad;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResolverAgenda extends Fragment implements View.OnClickListener {

    private HistoricoAgendaLlamadas historicoAgendaLlamadas;
    private Agenda agenda;
    private EditText observaciones;
    private Button buttonGuardar;
    private Button buttonVolver;

    public ResolverAgenda() {
    }

    public static ResolverAgenda newInstance(Agenda agenda) {
        ResolverAgenda fragment = new ResolverAgenda();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_resolver_agenda, container, false);

        capturarElementos(view);
        asignarListener();

        return view;
    }

    private void capturarElementos(View view) {
        this.observaciones = (EditText) view.findViewById(R.id.editTextObservacionesResolverAgenda);
        this.buttonGuardar = (Button) view.findViewById(R.id.button_guardar);
        this.buttonVolver = (Button) view.findViewById(R.id.button_volver);
    }

    private void asignarListener() {
        this.buttonGuardar.setOnClickListener(this);
        this.buttonVolver.setOnClickListener(this);
    }

    private void guardarDatos() {

        this.historicoAgendaLlamadas = new HistoricoAgendaLlamadas();

        this.historicoAgendaLlamadas.setIdAgenda(this.agenda.getId());

        this.historicoAgendaLlamadas.setObservaciones(this.observaciones.getText().toString());

        this.historicoAgendaLlamadas.setIdTeleoperador(Utilidad.getUserLogged().getPk());

        this.agenda.setFecha_resolucion(Utilidad.getStringFechaNowYYYYMMDD());
    }

    private void persistirResolverAgenda() {
        APIService apiService = ClienteRetrofit.getInstance().getAPIService();
        Call<HistoricoAgendaLlamadas> call = apiService.addHistoricoAgendaLlamadas(this.historicoAgendaLlamadas, Constantes.BEARER_ESPACIO + Utilidad.getToken().getAccess());
        call.enqueue(new Callback<HistoricoAgendaLlamadas>() {
            @Override
            public void onResponse(Call<HistoricoAgendaLlamadas> call, Response<HistoricoAgendaLlamadas> response) {
                if(response.isSuccessful()){
                    Toast.makeText(getContext(), Constantes.AGENDA_RESUELTA,  Toast.LENGTH_LONG).show();
                    volver();
                }
                else{
                    Toast.makeText(getContext(), Constantes.ERROR_CREACION + response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<HistoricoAgendaLlamadas> call, Throwable t) {
                Toast.makeText(getContext(), Constantes.ERROR_+t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void resolverAgenda() {
        APIService apiService = ClienteRetrofit.getInstance().getAPIService();
        Call<ResponseBody> call = apiService.actualizarAgenda(this.agenda.getId(), Constantes.BEARER_ESPACIO + Utilidad.getToken().getAccess(), this.agenda);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    Toast.makeText(getContext(), Constantes.MODIFICADO_CON_EXITO, Toast.LENGTH_LONG).show();
                    volver();
                }
                else{
                    Toast.makeText(getContext(), Constantes.ERROR_ + response.message() , Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getContext(), Constantes.ERROR_+t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
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
            case R.id.button_guardar:
                guardarDatos();
                resolverAgenda();
                persistirResolverAgenda();
                break;
            case R.id.button_volver:
                volver();
                break;
        }
    }
}
