package com.affinityopus.cbc_tv;

/**
 * Created by ravi on 3/8/2017.
 */

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;


public class BaseActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;
    Context context;
    boolean mBroadcastIsRegistered;
private Button btn;
    private MusicPlayerServices  player;
    boolean serviceBound = false;
    boolean boolMusicPlaying = false;

    Intent serviceIntent;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);


        View    headerView = navigationView.inflateHeaderView(R.layout.nav_header_main);
        btn   = (Button) headerView.findViewById(R.id.buttonRadio);
//        Intent playerIntent = new Intent(this, MusicPlayerServices.class);
//         bindService(playerIntent, serviceConnection, Context.BIND_AUTO_CREATE);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent viewIntent =
                        new Intent("android.intent.action.VIEW",
                                Uri.parse("http://www.radiorangh.com/"));
                startActivity(viewIntent);
             }
        });
        serviceIntent=new Intent(getApplicationContext(),MusicPlayerServices.class);
        bindService(serviceIntent,serviceConnection,Context.BIND_AUTO_CREATE);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(false);

        actionBarDrawerToggle.syncState();
        actionBarDrawerToggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        actionBarDrawerToggle.setHomeAsUpIndicator(R.drawable.dot);

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(false);
//        actionBarDrawerToggle.setDrawerIndicatorEnabled(false);
//        actionBarDrawerToggle.setHomeAsUpIndicator(R.drawable.emailicon);
//        navigationView.setNavigationItemSelectedListener((NavigationView.OnNavigationItemSelectedListener) this);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                final String appPackageName = getPackageName();
                Intent stopintent = new Intent(BaseActivity.this, myPlayService.class);

                switch (item.getItemId()) {

                    case R.id.nav_radio:
                      //  stopService(stopintent);
                        Intent dash = new Intent(getApplicationContext(), FirstActivity.class);
                        startActivity(dash);

//                        onDestroy();

//                        finish();
//                        context.stopService(new Intent(context, MusicPlayerServices.class));
//                        context.unbindService(serviceConnection);

//                                if (isMyServiceRunning(MusicPlayerServices.class)) {
//
//
//                              }


                        drawerLayout.closeDrawers();
                 unbindService();
                      break;


                    case R.id.nav_live_stream:
                       // stopService(stopintent);
                        Intent anIntent = new Intent(getApplicationContext(), LiveStramActivity.class);
                        startActivity(anIntent);
//                        finish();
                        drawerLayout.closeDrawers();
                        break;

                    case R.id.nav_cycle:
                      //  stopService(stopintent);
                        Intent ynIntent = new Intent(getApplicationContext(), Youtube_Fragment.class);
                        startActivity(ynIntent);
//                        finish();
                        drawerLayout.closeDrawers();
                        unbindService();
                        break;


                    case R.id.nav_reset:
                     //   stopService(stopintent);
                        SharedPreferences preferences = getSharedPreferences("user_details", context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.clear();
                        editor.commit();

                        Intent cnIntent = new Intent(getApplicationContext(), RadioPlayList.class);
                        startActivity(cnIntent);
//                        if (isMyServiceRunning(MusicPlayerServices.class)) {
//                            stopService(new Intent(BaseActivity.this,MusicPlayerServices.class));
//                            unbindService(serviceConnection);
//                        }

//                        finish();
                        drawerLayout.closeDrawers();
                        unbindService();
                        break;



                    case R.id.nav_share:

                        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                        sharingIntent.setType("text/plain");
                        String shareBody = "https://play.google.com/store/apps/details?id=com.affinityopus.radiorangh";
                        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Please Share Radio Rangh App");
                        sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                        startActivity(Intent.createChooser(sharingIntent, "Share via"));
                        break;

                    case R.id.nav_schedule:

                   //     stopService(stopintent);
                        Intent scheduleIntent = new Intent(getApplicationContext(), ScheduleActivity.class);
                        startActivity(scheduleIntent);
//                        finish();
                        drawerLayout.closeDrawers();
                        break;

                    case R.id.nav_feedback:
                        Intent fnIntent = new Intent(getApplicationContext(), FeedbackActivity.class);
                        startActivity(fnIntent);
//                        finish();
                        drawerLayout.closeDrawers();
                        break;

                }
                return false;
            }
        });


    }




    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            MusicPlayerServices.LocalBinder binder = (MusicPlayerServices.LocalBinder) service;
            player = binder.getService();

          //  player.stopMedia();
            serviceBound = true;

  //  Toast.makeText(BaseActivity.this, "Service Bound", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            serviceBound = false;
        }

    };


//    private void bindService(){
//        if(serviceConnection==null){
//            serviceConnection=new ServiceConnection() {
//                @Override
//                public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
//                    MusicPlayerServices.LocalBinder binder=(MusicPlayerServices.LocalBinder)iBinder;
//                    player=binder.getService();
//                    serviceBound=true;
//                }
//
//                @Override
//                public void onServiceDisconnected(ComponentName componentName) {
//                    serviceBound=false;
//                }
//            };
//        }
//
//        bindService(serviceIntent,serviceConnection,Context.BIND_AUTO_CREATE);
//
//    }

    private void unbindService(){
        if(serviceBound){
//           getApplicationContext(). stopService(new Intent(BaseActivity.this,MusicPlayerServices.class));
//            getApplicationContext().    unbindService(serviceConnection);
          //

            player.stopMedia();
            serviceBound=false;
            Log.d("Bound","Service is bound"+player);
        } else {
            Log.d("Bound","Service is NOT bound"+player);
        }
    }
//    public void stop() {
////        Intent intent = new Intent(this, MusicPlayerServices.class);
////        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
//   serviceBound =   bindService( new Intent(getApplicationContext(), MusicPlayerServices.class), serviceConnection, Context.BIND_AUTO_CREATE );
//
// if(serviceBound){
//
//     Log.d("Bound","Service is bound"+player);
// //    unbindService(serviceConnection);
//     stopService(new Intent(BaseActivity.this,MusicPlayerServices.class));
//
//     unbindService(serviceConnection);
//
// } else {
//     Log.d("Bound","Service is Not  bound"+player);
//  }
//
//  }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager)  getSystemService(ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
    public void redirecting(View v) {
        Toast.makeText(context, "Redirecting", Toast.LENGTH_SHORT).show();
    }
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        actionBarDrawerToggle.syncState();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransitionExit();
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransitionEnter();
    }

    /**
     * Overrides the pending Activity transition by performing the "Enter" animation.
     */
    protected void overridePendingTransitionEnter() {
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }

    /**
     * Overrides the pending Activity transition by performing the "Exit" animation.
     */
    protected void overridePendingTransitionExit() {
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}