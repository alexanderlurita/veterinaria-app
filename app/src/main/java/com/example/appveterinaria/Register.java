package com.example.appveterinaria;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {

    EditText etApellidosRegistro, etNombresRegistro, etDNIRegistro, etClaveAccesoRegistro, etConfirmarClaveAccesoRegistro;

    Button btRegistrarse;

    Context context = this;

    String apellidos, nombres, dni, claveAcceso, confirmarClaveAcceso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        loadUI();

        btRegistrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateInputs();
            }
        });
    }

    private void validateInputs() {
        apellidos = etApellidosRegistro.getText().toString().trim();
        nombres = etNombresRegistro.getText().toString().trim();
        dni = etDNIRegistro.getText().toString().trim();
        claveAcceso = etClaveAccesoRegistro.getText().toString().trim();
        confirmarClaveAcceso = etConfirmarClaveAccesoRegistro.getText().toString().trim();

        if (apellidos.isEmpty() || nombres.isEmpty() || dni.isEmpty() || claveAcceso.isEmpty()) {
            Utils.showToast(context, "Complete todos los datos");
        } else if (!claveAcceso.equals(confirmarClaveAcceso)) {
            Utils.showToast(context, "Las contraseñas no coinciden");
        } else {
            registerClient();
        }
    }

    private void registerClient() {
        String URL = Utils.URL + "cliente.controller.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    if (response.contains("LAST_INSERT_ID()")) {
                        Utils.showToast(context, "Registrado correctamente");

                        JSONObject jsonObject = new JSONObject(response);
                        int idCliente = jsonObject.getInt("LAST_INSERT_ID()");

                        Utils.setGlobalIdCliente(idCliente);
                        Utils.setDniCliente(dni);
                        Utils.setTipoCliente("E");

                        Utils.openActivityAndClearStack(context, MainActivity.class);
                    } else {
                        Utils.showToast(context, "Problemas al registrar");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
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
                parametros.put("apellidos", apellidos);
                parametros.put("nombres", nombres);
                parametros.put("dni", dni);
                parametros.put("claveacceso", confirmarClaveAcceso);
                return parametros;
            }
        };

        Volley.newRequestQueue(context).add(stringRequest);
    }

    private void loadUI() {
        etApellidosRegistro = findViewById(R.id.etApellidosRegistro);
        etNombresRegistro = findViewById(R.id.etNombresRegistro);
        etDNIRegistro = findViewById(R.id.etDNIRegistro);
        etClaveAccesoRegistro = findViewById(R.id.etClaveAccesoRegistro);
        etConfirmarClaveAccesoRegistro = findViewById(R.id.etConfirmarClaveAccesoRegistro);

        btRegistrarse = findViewById(R.id.btRegistrarse);
    }
}