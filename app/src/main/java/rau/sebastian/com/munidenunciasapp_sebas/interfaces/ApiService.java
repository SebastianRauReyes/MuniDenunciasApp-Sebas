package rau.sebastian.com.munidenunciasapp_sebas.interfaces;


import rau.sebastian.com.munidenunciasapp_sebas.models.Usuario;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiService {

    String API_BASE_URL = "https://app-denuncias-sebas-sebasraureyes.c9users.io";

    @FormUrlEncoded
    @POST("/api/v1/login")
    Call<Usuario> login(@Field("username") String username,
                        @Field("password") String password);
}
