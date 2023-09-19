package com.example.appveterinaria;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
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

public class Login extends AppCompatActivity {

    EditText etNombreUsuario, etContrasenia;
    Button btIniciarSesion;

    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loadUI();

        btIniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
    }

    private boolean validateInputs() {
        boolean nombreUsuario = etNombreUsuario.getText().toString().trim().isEmpty();
        boolean contrasenia = etContrasenia.getText().toString().trim().isEmpty();
        return !nombreUsuario && !contrasenia;
    }

    private void login() {
        boolean isSuccess = validateInputs();

        if (!isSuccess) {
            Utils.showToast(context, "Credenciales incorrectas");
        } else {
            getData();
        }
    }

    private void getData() {
        String URL = Utils.URL + "cliente.controller.php";
        String nombreUsuario = etNombreUsuario.getText().toString().trim();
        String contrasenia = etContrasenia.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<>();
                parametros.put("operacion", "iniciarSesion");
                parametros.put("nombreUsuario", nombreUsuario);
                parametros.put("contrasenia", contrasenia);
                return parametros;
            }
        };

        Volley.newRequestQueue(context).add(stringRequest);
    }

    private void loadUI() {
        etNombreUsuario = findViewById(R.id.etNombreUsuario);
        etContrasenia = findViewById(R.id.etContrasenia);

        btIniciarSesion = findViewById(R.id.btIniciarSesión);
    }
}