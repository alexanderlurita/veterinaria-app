package com.example.appveterinaria;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class Utils {

    public static final String URL_BASE = "https://overhanging-bias.000webhostapp.com/";
    public static final String URLCliente = URL_BASE + "controllers/cliente.controller.php";
    public static final String URLMascota = URL_BASE + "controllers/mascota.controller.php";
    public static final String URLImages = URL_BASE + "images/uploads/";

    private static int globalIdCliente;
    private static String dniCliente;
    private static String tipoCliente;

    public static void openActivity(Context context, Class activity) {
        Intent intent = new Intent(context, activity);
        context.startActivity(intent);
    }

    public static void openActivity(Context context, Class activityClass, Bundle extras) {
        Intent intent = new Intent(context, activityClass);
        if (extras != null) {
            intent.putExtras(extras);
        }
        context.startActivity(intent);
    }

    public static void openActivityAndClearStack(Context context, Class activity) {
        Intent intent = new Intent(context, activity);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


    public static void showToast(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    public static int getGlobalIdCliente() {
        return globalIdCliente;
    }

    public static void setGlobalIdCliente(int globalIdCliente) {
        Utils.globalIdCliente = globalIdCliente;
    }

    public static String getDniCliente() {
        return dniCliente;
    }

    public static void setDniCliente(String dniCliente) {
        Utils.dniCliente = dniCliente;
    }

    public static String getTipoCliente() {
        return tipoCliente;
    }

    public static void setTipoCliente(String tipoCliente) {
        Utils.tipoCliente = tipoCliente;
    }
}
