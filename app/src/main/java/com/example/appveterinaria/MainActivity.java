package com.example.appveterinaria;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button btRegistroCliente, btRegistroMascota, btClienteListaMascotas;
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadUI();

        btRegistroCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.openActivity(context, ClientForm.class);
            }
        });

        btRegistroMascota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.openActivity(context, PetForm.class);
            }
        });

        btClienteListaMascotas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.openActivity(context, ClientPetList.class);
            }
        });
    }

    private void loadUI() {
        btRegistroCliente = findViewById(R.id.btRegistroCliente);
        btRegistroMascota = findViewById(R.id.btRegistroMascota);
        btClienteListaMascotas = findViewById(R.id.btClienteListaMascotas);
    }
}