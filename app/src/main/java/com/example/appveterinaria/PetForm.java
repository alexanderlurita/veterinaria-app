package com.example.appveterinaria;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PetForm extends AppCompatActivity {

    Button btOpenGallery;
    Spinner spListaRazas;
    List<String> listaRazas = new ArrayList<>();
    private List<String> idsRazas = new ArrayList<>();
    private static final int PICK_IMAGE_REQUEST = 1;

    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_form);

        loadUI();

        getRaces();

        spListaRazas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                String selectedId = idsRazas.get(position);
                Toast.makeText(getApplicationContext(), "Seleccionaste: " + selectedItem + " (ID: " + selectedId + ")", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        btOpenGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, PICK_IMAGE_REQUEST);
            }
        });
    }

    private void getRaces() {
        String URL = Utils.URL + "mascota.controller.php?operacion=listRaces";

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                URL,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = response.getJSONObject(i);
                                System.out.println(jsonObject);
                                String idRaza = jsonObject.getString("idraza");
                                String animalRaza = jsonObject.getString("animalraza");

                                idsRazas.add(idRaza);
                                listaRazas.add(animalRaza);
                            }

                            ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplication(), android.R.layout.simple_spinner_item, listaRazas);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spListaRazas.setAdapter(adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error", error.toString());
                Utils.showToast(context, "Problemas con el servidor");
            }
        });

        Volley.newRequestQueue(context).add(jsonArrayRequest);
    }

    private void loadUI() {
        btOpenGallery = findViewById(R.id.openGalleryButton);

        spListaRazas = findViewById(R.id.spListaRazas);
    }
}