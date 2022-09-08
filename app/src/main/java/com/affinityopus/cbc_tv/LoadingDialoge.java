package com.affinityopus.cbc_tv;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;

class LoadingDialoge {
   private  Activity activity;
  private    AlertDialog alertDialog;

    LoadingDialoge(Activity myactivity) {
        activity = myactivity;
    }

    void startLoadingDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater =  activity.getLayoutInflater();

        builder.setView(inflater.inflate(R.layout.custome_dialoge,null));

        alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        alertDialog.show();

    }

    void dismiss() {
        alertDialog.dismiss();
    }
}
