package com.example.appveterinaria;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class Utils {

    public static final String URL = "https://overhanging-bias.000webhostapp.com/controllers/";
    public static final String URLImages = "https://overhanging-bias.000webhostapp.com/images/uploads/";

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

    public static void showToast(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }
}
