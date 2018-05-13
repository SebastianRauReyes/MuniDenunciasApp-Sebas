package rau.sebastian.com.munidenunciasapp_sebas.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import rau.sebastian.com.munidenunciasapp_sebas.R;
import rau.sebastian.com.munidenunciasapp_sebas.interfaces.ApiService;
import rau.sebastian.com.munidenunciasapp_sebas.models.Usuario;
import rau.sebastian.com.munidenunciasapp_sebas.singletons.ApiServiceGenerator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    // SharedPreferences
    private SharedPreferences sharedPreferences;

    private static final String TAG = LoginActivity.class.getSimpleName();
    private EditText usernameInput;
    private EditText passwordInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameInput = findViewById(R.id.username_input);
        passwordInput = findViewById(R.id.password_input);

    //---------------------------------------------------------------------------------------------
    // SharePreference

        // init SharedPreferences
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        // username remember
        String username = sharedPreferences.getString("username", null);
        if(username != null){
            usernameInput.setText(username);
            passwordInput.requestFocus();
        }
        // islogged remember
        if(sharedPreferences.getBoolean("islogged" , false)){
                sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
                if(sharedPreferences.getString("tipo", "99999").equals("2")){
                    goDashboard();
                    finish();
                }else{
                    goDashboardMain();
                    finish();
                }
        }

    }

    //----------------------------------------------------------------------------------------------
    //----  Dashboard
    private void goDashboardMain() {
        Intent intent = new Intent(LoginActivity.this, DashboardMainActivity.class);
        startActivity(intent);
    }
    //----------------------------------------------------------------------------------------------


    //----------------------------------------------------------------------------------------------
    //----  Dashboard
    private void goDashboard() {
        Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
        startActivity(intent);
    }

    //----------------------------------------------------------------------------------------------
    //---- User Login
    public void callLogin(View view) {
        final String username = usernameInput.getText().toString();
        String password = passwordInput.getText().toString();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Completar todos los campos requeridos", Toast.LENGTH_SHORT).show();
            return;
        }

        ApiService service = ApiServiceGenerator.createService(ApiService.class);
        Call<Usuario> call;
        call = service.login(username, password);

        call.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                try {

                    int statusCode = response.code();
                    Log.d(TAG, "HTTP status code: " + statusCode);

                    if (response.isSuccessful()) {

                        Usuario usuario = response.body();
                        Toast.makeText(LoginActivity.this, "Bienvenido "+usuario.getNombres(), Toast.LENGTH_SHORT).show();

                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        boolean success = editor
                                .putString("username", usuario.getUsername())
                                .putString("fullname" ,usuario.getNombres())
                                .putInt("id", usuario.getId())
                                .putString("tipo", usuario.getTipo())
                                .putBoolean("islogged", true)
                                .commit();

                        if(usuario.getTipo().equals("2")){
                            goDashboard();
                            finish();
                        }else {
                            goDashboardMain();
                            finish();
                        }



                    } else {
                        Log.e(TAG, "onError: " + response.errorBody().string());
                        throw new Exception("Usuario o Clave incorrectos");
                    }

                } catch (Throwable t) {
                    try {
                        Log.e(TAG, "onThrowable: " + t.toString(), t);
                        Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                    } catch (Throwable x) {
                    }
                }
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {

            }
        });
    }

    //----------------------------------------------------------------------------------------------
    //---- User Register

    public void callSingUp(View view) {
        Intent intent = new Intent(LoginActivity.this, RegisterUserActivity.class);
        startActivity(intent);
    }






}
