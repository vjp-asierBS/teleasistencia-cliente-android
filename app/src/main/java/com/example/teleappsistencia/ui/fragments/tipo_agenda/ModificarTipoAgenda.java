package com.example.teleappsistencia.ui.fragments.tipo_agenda;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.teleappsistencia.R;
import com.example.teleappsistencia.modelos.TipoAgenda;
import com.example.teleappsistencia.ui.fragments.agenda.CrearAgenda;
import com.example.teleappsistencia.ui.fragments.tipo_vivienda.servicios.APIService;
import com.example.teleappsistencia.ui.fragments.tipo_vivienda.servicios.ClienteRetrofit;
import com.example.teleappsistencia.utilidades.Constantes;
import com.example.teleappsistencia.utilidades.Utilidad;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ModificarTipoAgenda extends Fragment implements View.OnClickListener {

    private TipoAgenda tipoAgenda;
    private EditText editTextNombreTipoAgenda;
    private EditText editTextCodigoTipoAgenda;
    private EditText editTextImportanciaTipoAgenda;
    private Button buttonGuardar;
    private Button buttonVolver;

    public ModificarTipoAgenda() {
    }

    public static ModificarTipoAgenda newInstance(TipoAgenda tipoAgenda) {
        ModificarTipoAgenda fragment = new ModificarTipoAgenda();
        Bundle args = new Bundle();
        args.putSerializable(Constantes.ARG_TIPOAGENDA, tipoAgenda);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.tipoAgenda = (TipoAgenda) getArguments().getSerializable(Constantes.ARG_TIPOAGENDA);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_modificar_tipo_agenda, container, false);

        // Capturar los elementos del layout
        capturarElementos(view);

        //Asignamos el listener a los botones
        asignarListener();

        //Cargamos los datos
        if(this.tipoAgenda != null){
            cargarDatos();
        }

        return view;
    }

    private void cargarDatos() {
        this.editTextNombreTipoAgenda.setText(this.tipoAgenda.getNombre());
        this.editTextCodigoTipoAgenda.setText(""+this.tipoAgenda.getCodigo());
        this.editTextImportanciaTipoAgenda.setText(this.tipoAgenda.getImportancia());
    }

    private void capturarElementos(View view) {
        this.editTextNombreTipoAgenda = (EditText) view.findViewById(R.id.editTextNombre);
        this.editTextCodigoTipoAgenda = (EditText) view.findViewById(R.id.editTextCodigo);
        this.editTextImportanciaTipoAgenda = (EditText) view.findViewById(R.id.editTextImportancia);
        this.buttonGuardar = (Button) view.findViewById(R.id.button_guardar);
        this.buttonVolver = (Button) view.findViewById(R.id.button_volver);
    }

    private void asignarListener() {
        this.buttonGuardar.setOnClickListener(this);
        this.buttonVolver.setOnClickListener(this);
    }

    private boolean comprobaciones() {
        if(this.editTextCodigoTipoAgenda.getText().toString().isEmpty()) {
            Toast.makeText(getContext(), Constantes.DEBES_INTRODUCIR_CODIGO_TIPO_AGENDA, Toast.LENGTH_SHORT).show();
            return false;
        }
        if(this.editTextNombreTipoAgenda.getText().toString().isEmpty()) {
            Toast.makeText(getContext(), Constantes.DEBES_INTRODUCIR_NOMBRE, Toast.LENGTH_SHORT).show();
            return false;
        }
        if(this.editTextImportanciaTipoAgenda.getText().toString().isEmpty()) {
            Toast.makeText(getContext(), Constantes.DEBES_INTRODUCIR_CODIGO_TIPO_AGENDA, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void modificarDatosTipoAgenda() {
        this.tipoAgenda.setNombre(this.editTextNombreTipoAgenda.getText().toString());
        this.tipoAgenda.setCodigo(Integer.parseInt(this.editTextCodigoTipoAgenda.getText().toString()));
        this.tipoAgenda.setImportancia(this.editTextImportanciaTipoAgenda.getText().toString());
    }

    private void modificarTipoAgenda() {
        APIService apiService = ClienteRetrofit.getInstance().getAPIService();
        Call<ResponseBody> call = apiService.actualizarTipoAgenda(this.tipoAgenda.getId(), Constantes.BEARER_ESPACIO + Utilidad.getToken().getAccess(), this.tipoAgenda);
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

    /**
     * Este método vuelve a cargar el fragment con el listado.
     */
    private void volver(){
        CrearAgenda crearAgenda = new CrearAgenda();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_fragment, crearAgenda)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_guardar:
                if(comprobaciones()){
                    modificarDatosTipoAgenda();
                    modificarTipoAgenda();
                }
                break;
            case R.id.button_volver:
                volver();
                break;
        }
    }
}
