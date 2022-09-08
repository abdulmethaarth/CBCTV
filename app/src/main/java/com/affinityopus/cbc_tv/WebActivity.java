package com.affinityopus.cbc_tv;

import android.os.Bundle;
import android.widget.ImageButton;

public class WebActivity extends BaseActivity {


    public static WebActivity getInstance;
    private static WebActivity instance;
    private  ImageButton ic_instagram,ic_facebook;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
  setContentView(R.layout.activity_web);



    }



}
