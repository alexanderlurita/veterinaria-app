package com.example.appveterinaria;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.graphics.Bitmap;
import android.util.Base64;
import java.io.ByteArrayOutputStream;
import java.util.Map;

public class PetForm extends AppCompatActivity {

    EditText etDNIBuscar, etNombresDuenio, etNombreMascota, etColor;
    Spinner spListaRazas;
    RadioButton rbMacho, rbHembra;
    ImageView ivFotografia;
    Button btBuscarCliente, btSeleccionarFoto, btRegistrarMascota, btQuitarFoto;

    String idCliente, idRaza, nombreMascota, fotografia, color, genero;
    List<String> listaRazas = new ArrayList<>();
    private List<String> idsRazas = new ArrayList<>();

    Bitmap bitmap;
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_form);

        loadUI();
        getRaces();

        btBuscarCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchClient();
            }
        });

        spListaRazas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                idRaza = idsRazas.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        btSeleccionarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFileChooser();
            }
        });
        btQuitarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ivFotografia.setImageBitmap(null);
            }
        });
        btRegistrarMascota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateData();
            }
        });
    }

    public String getStringImagen(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void registerPet() {
        String URL = Utils.URL + "mascota.controller.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.isEmpty()) {
                    Utils.showToast(context, "Problemas al registrar");
                } else {
                    Utils.showToast(context, "Registrado correctamente");
                    resetUI();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Utils.showToast(context, "Problemas con el servidor");
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<>();
                parametros.put("operacion", "add");
                parametros.put("idcliente", idCliente);
                parametros.put("idraza", idRaza);
                parametros.put("nombre", nombreMascota);
                parametros.put("fotografia", fotografia);
                parametros.put("color", color);
                parametros.put("genero", genero);
                return parametros;
            }
        };

        Volley.newRequestQueue(context).add(stringRequest);
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Selecciona imagen"), 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                ivFotografia.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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

    private void searchClient() {
        String dni = etDNIBuscar.getText().toString().trim();
        String URL = Utils.URL + "cliente.controller.php?operacion=search&dni=" + dni;

        if (dni.isEmpty() || dni.length() < 8) {
            Utils.showToast(context, "Escriba el DNI");
        } else {
            StringRequest jsonObjectRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (!response.equals("false")) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            idCliente = jsonObject.getString("idcliente");
                            String nombresCompletos = jsonObject.getString("nombres") + " " + jsonObject.getString("apellidos");
                            etNombresDuenio.setText(nombresCompletos);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Utils.showToast(context, "Cliente no encontrado");
                        etNombresDuenio.setText(null);
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
    }

    private void validateData() {
        nombreMascota = etNombreMascota.getText().toString().trim();
        color = etColor.getText().toString().trim();
        genero = rbMacho.isChecked() ? "M" : "H";
        fotografia = ivFotografia.getDrawable() != null ? getStringImagen(bitmap) : "";

        if (idCliente.isEmpty() || idRaza.isEmpty() || nombreMascota.isEmpty() || color.isEmpty() || genero.isEmpty()) {
            Utils.showToast(context, "Complete todos los datos");
        } else {
            registerPet();
        }
    }

    private void resetUI() {
        spListaRazas.setSelection(0);
        etNombreMascota.setText(null);
        etColor.setText(null);
        rbMacho.setChecked(true);
        ivFotografia.setImageBitmap(null);

        etDNIBuscar.requestFocus();
    }

    private void loadUI() {
        etDNIBuscar = findViewById(R.id.etDNIBuscar);
        etNombresDuenio = findViewById(R.id.etNombresDuenio);
        etNombreMascota = findViewById(R.id.etNombreMascota);
        etColor = findViewById(R.id.etColor);

        spListaRazas = findViewById(R.id.spListaRazas);

        rbMacho = findViewById(R.id.rbMacho);
        rbHembra = findViewById(R.id.rbHembra);

        ivFotografia = findViewById(R.id.ivFotografia);

        btBuscarCliente = findViewById(R.id.btBuscarCliente);
        btSeleccionarFoto = findViewById(R.id.btSeleccionarFoto);
        btQuitarFoto = findViewById(R.id.btQuitarFoto);
        btRegistrarMascota = findViewById(R.id.btRegistrarMascota);
    }
}