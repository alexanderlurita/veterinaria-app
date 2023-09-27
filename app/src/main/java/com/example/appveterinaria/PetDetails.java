package com.example.appveterinaria;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class PetDetails extends AppCompatActivity {

    TextView tvNombreMascotaDet, tvAnimalDet, tvRazaDet, tvColorDet, tvGeneroDet;
    ImageView ivFotografiaDet;
    Context context = this;
    int idMascota;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_details);

        loadUI();

        Bundle params = this.getIntent().getExtras();
        if (params != null) {
            idMascota = params.getInt("idmascota");
            getData(idMascota);
        }
    }

    private void getData(int idmascota) {
        String URL = Utils.URLMascota + "?operacion=search&idmascota=" + idmascota;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                resetUI();
                try {
                    String photoName = response.getString("fotografia");
                    getImage(photoName);

                    tvNombreMascotaDet.setText(response.getString("nombre"));
                    tvAnimalDet.setText(response.getString("animal"));
                    tvRazaDet.setText(response.getString("raza"));
                    tvColorDet.setText(response.getString("color"));
                    tvGeneroDet.setText(response.getString("genero").equals("M") ? "Macho" : "Hembra");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Utils.showToast(context, "Problemas con el servidor");
            }
        });

        Volley.newRequestQueue(context).add(jsonObjectRequest);
    }

    private void getImage(String nameFile) {
        String URL = !nameFile.equals("null")
                ? Utils.URLImages + nameFile
                : "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d1/Image_not_available.png/640px-Image_not_available.png";
        ImageRequest imageRequest = new ImageRequest(URL, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                ivFotografiaDet.setImageBitmap(response);
            }
        }, 0, 0, null, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        Volley.newRequestQueue(context).add(imageRequest);
    }

    private void resetUI() {
        tvNombreMascotaDet.setText("Nombre de la Mascota");
        ivFotografiaDet.setImageBitmap(null);
        tvAnimalDet.setText("Animal");
        tvRazaDet.setText("Raza");
        tvColorDet.setText("Color");
        tvGeneroDet.setText("Género");
    }

    private void loadUI() {
        tvNombreMascotaDet = findViewById(R.id.tvNombreMascotaDet);
        tvAnimalDet = findViewById(R.id.tvAnimalDet);
        tvRazaDet = findViewById(R.id.tvRazaDet);
        tvColorDet = findViewById(R.id.tvColorDet);
        tvGeneroDet = findViewById(R.id.tvGeneroDet);
        ivFotografiaDet = findViewById(R.id.ivFotografiaDet);
    }
}