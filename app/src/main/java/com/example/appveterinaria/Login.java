package com.example.appveterinaria;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.net.Uri;
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

import org.json.JSONObject;

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
                validateInputs();
            }
        });
    }

    private void resetInputs() {
        etNombreUsuario.setText(null);
        etContrasenia.setText(null);

        etNombreUsuario.requestFocus();
    }

    private void validateInputs() {
        boolean nombreUsuario = etNombreUsuario.getText().toString().trim().isEmpty();
        boolean contrasenia = etContrasenia.getText().toString().trim().isEmpty();

        if (nombreUsuario || contrasenia) {
            Utils.showToast(context, "Escriba su nombre de usuario y/o contraseña");
        } else {
            login();
        }
    }

    private void login() {
        String URL = Utils.URL + "cliente.controller.php";
        String nombreUsuario = etNombreUsuario.getText().toString().trim();
        String contrasenia = etContrasenia.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean login = jsonObject.getBoolean("login");
                    String nombres = jsonObject.getString("nombres") + " " + jsonObject.getString("apellidos");

                    if (login) {
                        Utils.showToast(context, "¡Bienvenido " + nombres + "!");
                        Utils.openActivity(context, MainActivity.class);
                        resetInputs();
                    } else {
                        Utils.showToast(context, jsonObject.getString("message"));
                    }
                } catch (Exception e) {
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
                parametros.put("operacion", "login");
                parametros.put("username", nombreUsuario);
                parametros.put("password", contrasenia);
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