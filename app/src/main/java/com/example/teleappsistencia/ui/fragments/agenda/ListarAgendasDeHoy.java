package com.example.teleappsistencia.ui.fragments.agenda;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.teleappsistencia.MainActivity;
import com.example.teleappsistencia.R;
import com.example.teleappsistencia.modelos.Agenda;
import com.example.teleappsistencia.ui.fragments.tipo_agenda.InsertarTipoAgenda;
import com.example.teleappsistencia.ui.fragments.tipo_vivienda.servicios.APIService;
import com.example.teleappsistencia.ui.fragments.tipo_vivienda.servicios.ClienteRetrofit;
import com.example.teleappsistencia.utilidades.Constantes;
import com.example.teleappsistencia.utilidades.Utilidad;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListarAgendasDeHoy extends Fragment implements View.OnClickListener {

    private List<Agenda> lAgendas;
    private RecyclerView recycler;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager lManager;
    private Context context;
    private Agenda agenda;
    private TextView fecha;
    private Button buttonCrearNuevaAgenda;
    private ImageButton buttonEditarAgenda;
    private ImageButton buttonBorrarAgenda;
    private ImageButton buttonInfoAgenda;
    private Button buttonResolverAgenda;

    public ListarAgendasDeHoy() {
    }

    public static ListarAgendasDeHoy newInstance() {
        ListarAgendasDeHoy fragment = new ListarAgendasDeHoy();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_listar_agenda, container, false);
        this.buttonCrearNuevaAgenda = (Button) view.findViewById(R.id.button_nueva_agenda);
        this.buttonEditarAgenda = (ImageButton) view.findViewById(R.id.edit_button);
        this.buttonBorrarAgenda = (ImageButton) view.findViewById(R.id.delete_button);
        this.buttonResolverAgenda = (Button) view.findViewById(R.id.button_resolverAgenda);
        this.buttonInfoAgenda = (ImageButton) view.findViewById(R.id.view_details_button);
        this.fecha = (TextView) view.findViewById(R.id.fecha_actual);
        fecha.setText(Utilidad.getStringFechaNowYYYYMMDD());

        // Obtener el Recycler.
        recycler = (RecyclerView) view.findViewById(R.id.listRecyclerViewAgenda);
        recycler.setHasFixedSize(true);

        //Cargamos un adaptador vacío mientras se carga la lista desde la API REST
        this.lAgendas = new ArrayList<>();
        adapter = new AgendaAdapter(lAgendas);
        recycler.setAdapter(adapter);

        //Asignamos los Listener
        asignarListener();

        //Cargamos lista desde la API REST
        cargarLista();

        return view;
    }

    private void asignarListener(){
        this.buttonCrearNuevaAgenda.setOnClickListener(this);
        this.buttonEditarAgenda.setOnClickListener(this);
        this.buttonBorrarAgenda.setOnClickListener(this);
        this.buttonResolverAgenda.setOnClickListener(this);
        this.buttonInfoAgenda.setOnClickListener(this);
    }

    private void cargarLista(){
        APIService apiService = ClienteRetrofit.getInstance().getAPIService();
        Call<List<Object>> call = apiService.getAgendasByFechaPrevista(Utilidad.getStringFechaNowYYYYMMDD(), Constantes.BEARER_ESPACIO + Utilidad.getToken().getAccess());
        call.enqueue(new Callback<List<Object>>() {
            @Override
            public void onResponse(Call<List<Object>> call, Response<List<Object>> response) {
                if(response.isSuccessful()) {
                    List<Object> lObjetos = response.body();
                    lAgendas = (ArrayList<Agenda>) Utilidad.getObjeto(lObjetos, Constantes.AL_AGENDA);
                    adapter = new AgendaAdapter(lAgendas);
                    recycler.setAdapter(adapter);
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

    private void nuevaAgenda() {
        CrearAgenda insertarAgenda = CrearAgenda.newInstance(this.agenda);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_fragment, insertarAgenda)
                .addToBackStack(null)
                .commit();
    }

    private void editarAgenda() {
        ModificarAgendaFragment fragmentModificarAgenda = ModificarAgendaFragment.newInstance(this.agenda);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_fragment, fragmentModificarAgenda)
                .addToBackStack(null)
                .commit();
    }

    private void infoAgenda() {
        MostrarInfoAgenda infoAgenda = MostrarInfoAgenda.newInstance(this.agenda);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_fragment, infoAgenda)
                .addToBackStack(null)
                .commit();
    }

    private void borrarAgenda() {
        APIService apiService = ClienteRetrofit.getInstance().getAPIService();
        Call<ResponseBody> call = apiService.deleteAgendabyId(this.agenda.getId(), Constantes.BEARER_ESPACIO + Utilidad.getToken().getAccess());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    Toast.makeText(context, Constantes.AGENDA_BORRADA, Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(context, Constantes.ERROR_BORRADO + response.message(), Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(context, Constantes.ERROR_+t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void resolverAgenda() {
        ResolverAgenda resolverAgenda = ResolverAgenda.newInstance(this.agenda);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_fragment, resolverAgenda)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_nueva_agenda:
                nuevaAgenda();
                break;
            case R.id.view_details_button:
                infoAgenda();
                break;
            case R.id.edit_button:
                editarAgenda();
                break;
            case R.id.delete_button:
                borrarAgenda();
                break;
            case R.id.button_resolverAgenda:
                resolverAgenda();
                break;
        }
    }
}