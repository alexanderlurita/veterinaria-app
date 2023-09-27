package com.example.appveterinaria;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ClientPetList extends AppCompatActivity {

    EditText etDNIDuenioBuscar, etDatosDuenio;
    ListView lvMascotasDuenio;
    Button btBuscarDuenio;

    private List<Mascota> dataList = new ArrayList<>();
    private List<Integer> dataID = new ArrayList<>();
    private MascotaAdapter adapter;

    private String dni;

    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_pet_list);

        loadUI();

        btBuscarDuenio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchClient();
            }
        });

        lvMascotasDuenio.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Bundle extras = new Bundle();
                extras.putInt("idmascota", dataID.get(i));

                Utils.openActivity(context, PetDetails.class, extras);
            }
        });

        if (Utils.getTipoCliente().equals("E")) {
            etDNIDuenioBuscar.setText(Utils.getDniCliente());
            etDNIDuenioBuscar.setFocusable(false);
            btBuscarDuenio.performClick();
        }
    }

    private void getPets(String dni)    {
        dataID.clear();
        dataList.clear();
        adapter = new MascotaAdapter(context, dataList);
        lvMascotasDuenio.setAdapter(adapter);

        String URL = Utils.URLMascota + "?operacion=listPetsByOwner&dni=" + dni;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, URL, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    if (response.length() > 0) {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject jsonObject = new JSONObject(response.getString(i));
                            Mascota mascota = new Mascota(jsonObject.getString("nombre"), jsonObject.getString("tipo"));
                            dataList.add(mascota);
                            dataID.add(jsonObject.getInt("idmascota"));
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        Utils.showToast(context, "El cliente no tiene mascotas registradas");
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
        });

        Volley.newRequestQueue(context).add(jsonArrayRequest);
    }

    private void searchClient() {
        dni = etDNIDuenioBuscar.getText().toString().trim();
        String URL = Utils.URLCliente + "?operacion=search&dni=" + dni;

        if (dni.isEmpty() || dni.length() < 8) {
            Utils.showToast(context, "Escriba el DNI");
        } else {
            StringRequest jsonObjectRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (!response.equals("false")) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String nombresCompletos = jsonObject.getString("nombres") + " " + jsonObject.getString("apellidos");
                            etDatosDuenio.setText(nombresCompletos);

                            getPets(dni);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Utils.showToast(context, "Cliente no encontrado");
                        etDatosDuenio.setText(null);
                        dataID.clear();
                        dataList.clear();
                        adapter.notifyDataSetChanged();
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

    private void loadUI() {
        etDNIDuenioBuscar = findViewById(R.id.etDNIDuenioBuscar);
        etDatosDuenio = findViewById(R.id.etDatosDuenio);
        lvMascotasDuenio = findViewById(R.id.lvMascotasDuenio);

        btBuscarDuenio = findViewById(R.id.btBuscarDuenio);
    }
}