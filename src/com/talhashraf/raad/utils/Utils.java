package com.talhashraf.raad.utils;


import android.widget.Toast;

import android.content.Context;
import android.content.DialogInterface;

import android.app.AlertDialog;


public class Utils {
    static void showDialog(Context context, String title, String message) {
    	AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);

        builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
       });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    static void toast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
}