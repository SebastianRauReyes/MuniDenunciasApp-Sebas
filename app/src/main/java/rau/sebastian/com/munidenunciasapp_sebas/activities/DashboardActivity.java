package rau.sebastian.com.munidenunciasapp_sebas.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import rau.sebastian.com.munidenunciasapp_sebas.R;
import rau.sebastian.com.munidenunciasapp_sebas.adapters.DenuciasAdapter;
import rau.sebastian.com.munidenunciasapp_sebas.interfaces.ApiService;
import rau.sebastian.com.munidenunciasapp_sebas.models.Denuncia;
import rau.sebastian.com.munidenunciasapp_sebas.models.Usuario;
import rau.sebastian.com.munidenunciasapp_sebas.singletons.ApiServiceGenerator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardActivity extends AppCompatActivity {

    private static final String TAG = DashboardActivity.class.getSimpleName();
    private SharedPreferences sharedPreferences;
    private RecyclerView DenunciasList;
    private Integer id;
    private TextView user_name_textview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        //------------------------------------------------------------------------------------------
        //------ SharedPreferences
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        id = sharedPreferences.getInt("id", 99999);
        String user_name = sharedPreferences.getString("fullname",null);

        BuscarUsuario(id);
        //------------------------------------------------------------------------------------------
            user_name_textview = findViewById(R.id.user_text_dash);
        Log.e(TAG, "onEraaaaaaaaaaaaaaaaaaaaaaror: " + user_name);
            user_name_textview.setText(user_name);
        //------------------------------------------------------------------------------------------
        DenunciasList = findViewById(R.id.recyclerview);
        DenunciasList.setLayoutManager(new LinearLayoutManager(this));

        DenunciasList.setAdapter(new DenuciasAdapter(this));

        ListarDenuncias();

    }
    //----------------------------------------------------------------------------------------------
    private void ListarDenuncias() {
        ApiService service = ApiServiceGenerator.createService(ApiService.class);

        Call<List<Denuncia>> call = service.getDenunciasByUserId(id);

        call.enqueue(new Callback<List<Denuncia>>() {
            @Override
            public void onResponse(Call<List<Denuncia>> call, Response<List<Denuncia>> response) {
                try {

                    int statusCode = response.code();
                    Log.d(TAG, "HTTP status code: " + statusCode);

                    if (response.isSuccessful()) {

                        List<Denuncia> denuncias = response.body();
                        DenuciasAdapter denuciasAdapter = (DenuciasAdapter) DenunciasList.getAdapter();
                        denuciasAdapter.setDenuncias(denuncias);
                        denuciasAdapter.notifyDataSetChanged();

                    } else {
                        Log.e(TAG, "onError: " + response.errorBody().string());
                        throw new Exception("Error en el servicio");
                    }

                } catch (Throwable t) {
                    try {
                        Log.e(TAG, "onThrowable: " + t.toString(), t);
                        Toast.makeText(DashboardActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                    }catch (Throwable x){}
                }
            }

            @Override
            public void onFailure(Call<List<Denuncia>> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.toString());
                Toast.makeText(DashboardActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }

        });


    }

    //----------------------------------------------------------------------------------------------
    //------ Buscar Usuario por ID
    public void BuscarUsuario(Integer id){

        ApiService service = ApiServiceGenerator.createService(ApiService.class);
        Call<Usuario> call;
        call = service.showUser(id);

        call.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                try {
                    int statusCode = response.code();
                    Log.d(TAG, "HTTP status code: " + statusCode);

                    if (response.isSuccessful()) {
                        //Asignar valores a usuario
                        Usuario usuario = response.body();
                        Log.d(TAG, "Usuario Actual: " + usuario.getNombres());
                    } else {
                        Log.e(TAG, "onError: " + response.errorBody().string());
                        throw new Exception("Error en el servicio");
                    }

                } catch (Throwable t) {
                    try {
                        Log.e(TAG, "onThrowable: " + t.toString(), t);
                        Toast.makeText(DashboardActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                    }catch (Throwable x){}
                }
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.toString());
                Toast.makeText(DashboardActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    //----------------------------------------------------------------------------------------------
    //------ Cerrar Sesión
    public void callLogout() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("islogged").apply();
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    //----------------------------------------------------------------------------------------------
    //------ Registrar Nueva Denuncia

    private static final int REGISTER_FORM_REQUEST = 100;

    public void showRegister() {
        startActivityForResult(new Intent(this, RegisterComplaintActivity.class), REGISTER_FORM_REQUEST);
    }
    //----------------------------------------------------------------------------------------------
    //------ Refrescar lista
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REGISTER_FORM_REQUEST) {
            ListarDenuncias();
        }
    }
    //----------------------------------------------------------------------------------------------
    //------ MENU

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.action_logout:
                callLogout();
                return true;
            case R.id.action_add:
                showRegister();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
