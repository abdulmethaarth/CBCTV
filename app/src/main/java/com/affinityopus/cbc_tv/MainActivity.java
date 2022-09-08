package com.affinityopus.cbc_tv;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import static android.Manifest.permission.READ_PHONE_STATE;


public class MainActivity<redirecting> extends BaseActivity {

    private TextView mTextMessage;
   ImageButton buttonContinue, ic_instagram,ic_facebook,ic_youtube,ic_twitter,ic_whatsapp;

    SharedPreferences pref;
    Context context;
    String TAG = "MainActivityTAG";
//        private static final int MY_PERMISSION_REQUEST_CODE_PHONE_STATE = 1;
// 
// private static final String LOG_TAG = "AndroidExample";
    private static  final int MY_PERMISSION_REQUEST_CODE_PHONE_STATE =1;
    private static final String LOG_TAG = "MAINTAG";
    private int PERMISSION_REQUEST_CODE  = 1;

    ImageView imageView;
  //  private static final int PERMISSION_REQUEST_CODE = 100;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@Nullable MenuItem item) {
            switch (item.getItemId()) {
                case R.id.nav_radio:
                    mTextMessage.setText(R.string.title_home);
                    return true;
              /*  case R.id.nav_home:
                    mTextMessage.setText(R.string.title_dashboard);
                    return true;*/
                case R.id.nav_cycle:
                    mTextMessage.setText(R.string.title_notifications);
                    return true;

            }
            return false;
        }

    };

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame); //Remember this is the FrameLayout area within your activity_main.xml
        getLayoutInflater().inflate(R.layout.activity_main, contentFrameLayout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(0).setChecked(true);



        ActionBar actionBar;
        actionBar = getSupportActionBar();
//        ColorDrawable colorDrawable
//                = new ColorDrawable(Color.parseColor(ContextCompat.getColor(context,R.color.feedbackBG)));
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(getApplicationContext(),R.color.frontTop)));

        // Set BackgroundDrawable
    //    actionBar.setBackgroundDrawable(colorDrawable);
        // Set BackgroundDrawable
 //        if (ActivityCompat.checkSelfPermission(this,
//                READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
//            TelephonyManager tMgr = (TelephonyManager)   this.getSystemService(Context.TELEPHONY_SERVICE);
//            String mPhoneNumber = tMgr.getLine1Number();
//
//            return;
//        } else {
//            requestPermission();
//        }

     askPermissionAndGetPhoneNumbers();

        pref = getSharedPreferences("user",context.MODE_PRIVATE);
        String getID  =    pref.getString("getID",null);
        String getImages  =    pref.getString("getImages",null);

        imageView = (ImageView) findViewById(R.id.imageView);
        Glide.with(getApplicationContext())
                .load(/*getImages*/"http://cbctv.in/music/images/JLMEDIA.png")
                .error(R.mipmap.ic_launcher_round) //in case of error this is displayed
                .into(imageView);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              /*  pref = getSharedPreferences("user",context.MODE_PRIVATE);
                pref = getSharedPreferences("user",context.MODE_PRIVATE);
                String getID  =    pref.getString("getID",null);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("getID",getID);
//                editor.putString("getImages",listData.getImageUrl());
                //  SharedPrefManager.getInstance(context).userLogin(user);
                editor.commit();
                if(getID.toString().equalsIgnoreCase("5")){*/
                    Intent ynIntent = new Intent(getApplicationContext(), LiveStramActivity.class);
                    startActivity(ynIntent);

               /* }else{
                    Intent ynIntent = new Intent(getApplicationContext(), Youtube_Fragment.class);
                    startActivity(ynIntent);
                }*/

            }
        });
    }



    private void askPermissionAndGetPhoneNumbers() {

        // With Android Level >= 23, you have to ask the user
        // for permission to get Phone Number.
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) { // 23

            // Check if we have READ_PHONE_STATE permission
            int readPhoneStatePermission = ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.READ_PHONE_STATE);

            if ( readPhoneStatePermission != PackageManager.PERMISSION_GRANTED) {
                // If don't have permission so prompt the user.
                this.requestPermissions(
                        new String[]{Manifest.permission.READ_PHONE_STATE},
                        MY_PERMISSION_REQUEST_CODE_PHONE_STATE
                );
                return;
            }
        }
        this.getPhoneNumbers();
    }


    @SuppressLint("MissingPermission")
    private void getPhoneNumbers() {
        try {
            TelephonyManager manager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);

            String phoneNumber1 = manager.getLine1Number();

        //    this.editTextPhoneNumbers.setText(phoneNumber1);

            //
           // Log.i( LOG_TAG,"Your Phone Number: " + phoneNumber1);
      //      Toast.makeText(this,"Your Phone Number: " + phoneNumber1,Toast.LENGTH_LONG).show();

            // Other Informations:
            if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) { // API Level 26.
                String imei = manager.getImei();
                int phoneCount = manager.getPhoneCount();

                Log.i(LOG_TAG,"Phone Count: " + phoneCount);
                Log.i(LOG_TAG,"EMEI: " + imei);
            }
            if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) { // API Level 28.
                SignalStrength signalStrength = manager.getSignalStrength();
                int level = signalStrength.getLevel();

                Log.i(LOG_TAG,"Signal Strength Level: " + level);
            }

        } catch (Exception ex) {
            Log.e( LOG_TAG,"Error: ", ex);
         //   Toast.makeText(this,"Error: " + ex.getMessage(),Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
             requestPermissions(new String[]{  READ_PHONE_STATE}, 100);
        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //
        switch (requestCode) {
            case MY_PERMISSION_REQUEST_CODE_PHONE_STATE: {

                // Note: If request is cancelled, the result arrays are empty.
                // Permissions granted (SEND_SMS).
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Log.i( LOG_TAG,"Permission granted!");
                    Toast.makeText(this, "Permission granted!", Toast.LENGTH_LONG).show();

                    this.getPhoneNumbers();

                }
                // Cancelled or denied.
                else {
                    Log.i( LOG_TAG,"Permission denied!");
                    Toast.makeText(this, "Permission denied!", Toast.LENGTH_LONG).show();
                }
                break;
            }
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == MY_PERMISSION_REQUEST_CODE_PHONE_STATE) {
            if (resultCode == RESULT_OK) {
                // Do something with data (Result returned).
                Toast.makeText(this, "Action OK", Toast.LENGTH_LONG).show();
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Action Cancelled", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Action Failed", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void openNewActivity(){

        Intent intent = new Intent(this, RadioPlayer.class);
        startActivity(intent);

    }




    public void playerList(){

        Intent intent = new Intent(this, RadioPlayList.class);
        startActivity(intent);

    }
}
