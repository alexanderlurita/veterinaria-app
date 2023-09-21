package com.example.appveterinaria;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
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

import java.util.HashMap;
import java.util.Map;

public class ClientForm extends AppCompatActivity {

    EditText etApellidos, etNombres, etDNI, etClaveAcceso, etConfirmarClaveAcceso;
    Button btRegistrarCliente;
    Context context = this;

    String apellidos, nombres, dni, claveAcceso, confirmarClaveAcceso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_form);

        loadUI();

        btRegistrarCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateInputs();
            }
        });
    }

    private void resetInputs() {
        etApellidos.setText(null);
        etNombres.setText(null);
        etDNI.setText(null);
        etClaveAcceso.setText(null);
        etConfirmarClaveAcceso.setText(null);

        etApellidos.requestFocus();
    }

    private void validateInputs() {
        apellidos = etApellidos.getText().toString().trim();
        nombres = etNombres.getText().toString().trim();
        dni = etDNI.getText().toString().trim();
        claveAcceso = etClaveAcceso.getText().toString().trim();
        confirmarClaveAcceso = etConfirmarClaveAcceso.getText().toString().trim();

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
                if (!response.isEmpty()) {
                    Utils.showToast(context, "Problemas al registrar");
                } else {
                    Utils.showToast(context, "Registrado correctamente");
                    resetInputs();
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
        etApellidos = findViewById(R.id.etApellidos);
        etNombres = findViewById(R.id.etNombres);
        etDNI = findViewById(R.id.etDNI);
        etClaveAcceso = findViewById(R.id.etClaveAcceso);
        etConfirmarClaveAcceso = findViewById(R.id.etConfirmarClaveAcceso);

        btRegistrarCliente = findViewById(R.id.btRegistrarCliente);
    }
}