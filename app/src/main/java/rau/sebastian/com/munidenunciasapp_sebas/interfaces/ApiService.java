package rau.sebastian.com.munidenunciasapp_sebas.interfaces;


import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rau.sebastian.com.munidenunciasapp_sebas.models.Denuncia;
import rau.sebastian.com.munidenunciasapp_sebas.models.ResponseMessage;
import rau.sebastian.com.munidenunciasapp_sebas.models.Usuario;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ApiService {

    //----------------------------------------------------------------------------------------------
    // URL Base del Cloud9 - API REST
    String API_BASE_URL = "https://app-denuncias-sebas-sebasraureyes.c9users.io";

    //----------------------------------------------------------------------------------------------
    //---- USUARIOS --------------------------------------------------------------------------------
    //----------------------------------------------------------------------------------------------

    //----------------------------------------------------------------------------------------------
    // Iniciar Sessión
    @FormUrlEncoded
    @POST("/api/v1/login")
    Call<Usuario> login(@Field("username") String username,
                        @Field("password") String password);

    //----------------------------------------------------------------------------------------------
    // Crear nuevo Usuarios
    @FormUrlEncoded
    @POST("/api/v1/usuarios")
    Call<ResponseMessage> createUser(@Field("username") String username,
                                     @Field("password") String password,
                                     @Field("nombres")  String nombres,
                                     @Field("tipo")     String tipo);

    //----------------------------------------------------------------------------------------------
    // Buscar Usuario por "ID"
    @GET("api/v1/usuarios/{id}")
    Call<Usuario> showUser(@Path("id") Integer id);

    //----------------------------------------------------------------------------------------------
    //---- DENUNCIAS -------------------------------------------------------------------------------
    //----------------------------------------------------------------------------------------------

    //----------------------------------------------------------------------------------------------
    // Listar Denuncias
    @GET("api/v1/denuncias")
    Call<List<Denuncia>> getDenuncias();

    //----------------------------------------------------------------------------------------------
    // Crear Denuncia con imagen
    @Multipart
    @POST("/api/v1/denuncias")
    Call<ResponseMessage> createDenunciaImagen(
            @Part("usuarios_id") RequestBody usuarios_id,
            @Part("titulo")      RequestBody titulo,
            @Part("descripcion") RequestBody descripcion,
            @Part("direccion")   RequestBody direccion,
            @Part MultipartBody.Part imagen
    );

    //----------------------------------------------------------------------------------------------
    // Crear Denuncia
    @FormUrlEncoded
    @POST("/api/v1/denuncias")
    Call<ResponseMessage> createDenuncia(@Field("usuarios_id") Integer usuarios_id,
                                         @Field("titulo")      String titulo,
                                         @Field("descripcion") String descripcion,
                                         @Field("direccion")   String direccion);

    //----------------------------------------------------------------------------------------------
    // Filtrar por ID
    @GET("api/v1/denuncias/find/{usuarios_id}")
    Call<List<Denuncia>> getDenunciasByUserId(@Path("usuarios_id") Integer usuarios_id);


}
