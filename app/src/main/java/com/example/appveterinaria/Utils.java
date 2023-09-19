package com.example.appveterinaria;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class Utils {

    public static final String URL = "http://192.168.1.116/veterinaria/controllers/";

    public static void openActivity(Context context, Class activity) {
        Intent intent = new Intent(context, activity);
        context.startActivity(intent);
    }

    public static void showToast(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }
}
