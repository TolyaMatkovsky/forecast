package com.tolya.weather.forecast;

import android.content.Context;
import android.widget.Toast;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Tolik on 31.12.2016.
 */

public class AlertManager {
    public static SweetAlertDialog showSweetProgressBar(Context context, String message){
        SweetAlertDialog pDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(context.getResources().getColor(R.color.colorPrimaryDark));
        pDialog.setTitleText(message+"...");
        pDialog.setCancelable(false);
        return pDialog;
    }

    public static void showSweetWarning(Context context, String title, String message){
        new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                .setTitleText(title)
                .setContentText(message)
                .setConfirmText(context.getResources().getString(R.string.ok))
                .show();
    }

    public static void showSweetSuccessDialog(Context context, String title, String message){
        new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText(title)
                .setContentText(message)
                .setConfirmText(context.getResources().getString(R.string.ok))
                .show();
    }



    public static void showToast(Context context, String message){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
