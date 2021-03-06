package rau.sebastian.com.munidenunciasapp_sebas.adapters;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import rau.sebastian.com.munidenunciasapp_sebas.R;
import rau.sebastian.com.munidenunciasapp_sebas.activities.DashboardActivity;
import rau.sebastian.com.munidenunciasapp_sebas.interfaces.ApiService;
import rau.sebastian.com.munidenunciasapp_sebas.models.Denuncia;
import rau.sebastian.com.munidenunciasapp_sebas.models.Usuario;
import rau.sebastian.com.munidenunciasapp_sebas.singletons.ApiServiceGenerator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DenuciasAdapter extends RecyclerView.Adapter<DenuciasAdapter.ViewHolder> {

    private static final String TAG = DenuciasAdapter.class.getSimpleName();
    private List<Denuncia> denuncias;
    private Activity activity;

    public DenuciasAdapter(Activity activity){
        this.denuncias = new ArrayList<>();
    }
    public void setDenuncias(List<Denuncia> denuncias){
        this.denuncias = denuncias;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView titleText;
        public TextView autorText;
        public TextView locationText;
        public ImageView photoimage;


        public ViewHolder(View itemView) {
            super(itemView);
            titleText = itemView.findViewById(R.id.titulo_text);
            autorText = itemView.findViewById(R.id.autor_text);
            locationText = itemView.findViewById(R.id.ubicacion_text);
            photoimage = itemView.findViewById(R.id.image_denuncia);
        }
    }

    @NonNull
    @Override
    public DenuciasAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_denuncia, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final DenuciasAdapter.ViewHolder holder, int position) {

        final Denuncia denuncia= this.denuncias.get(position);

        ApiService service = ApiServiceGenerator.createService(ApiService.class);
        Call<Usuario> call;
        call = service.showUser(denuncia.getUsuarios_id());
        call.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                try {
                    int statusCode = response.code();
                    Log.d(TAG, "HTTP status code: " + statusCode);

                    if (response.isSuccessful()) {

                        Usuario usuario = response.body();

                        holder.titleText.setText(denuncia.getTitulo());
                        holder.autorText.setText(usuario.getNombres());
                        holder.locationText.setText(denuncia.getDireccion());
                        String url = ApiService.API_BASE_URL + "/images/" + denuncia.getImagen();
                        Picasso.with(holder.itemView.getContext()).load(url).into(holder.photoimage);

                    } else {
                        Log.e(TAG, "onError: " + response.errorBody().string());
                        throw new Exception("Error en el servicio");
                    }

                } catch (Throwable t) {
                    try {
                        Log.e(TAG, "onThrowable: " + t.toString(), t);

                    }catch (Throwable x){}
                }
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.toString());

            }
        });

    }

    @Override
    public int getItemCount() {
        return this.denuncias.size();
    }




}
