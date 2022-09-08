package com.affinityopus.cbc_tv;

import android.graphics.Color;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;

public class RadioPlayer extends BaseActivity implements SeekBar.OnSeekBarChangeListener {
    Intent serviceIntent;
    private ImageButton buttonPlayStop,ic_instagram,ic_facebook,ic_youtube,ic_twitter,ic_whatsapp;
ImageView imageView;

    SharedPreferences pref;




    private NotificationManagerCompat notificationManagerCompat;

    // -- PUT THE NAME OF YOUR AUDIO FILE HERE...URL GOES IN THE SERVICE
    String strAudioLink = "https://streaming.radio.co/sc9c730664/listen";

    private boolean isOnline;
    private boolean boolMusicPlaying = false;
    TelephonyManager telephonyManager;
    PhoneStateListener listener;

    // --Seekbar variables --
    private SeekBar seekBar;
    private int seekMax;
    private static int songEnded = 0;
    boolean mBroadcastIsRegistered;

    // --Set up constant ID for broadcast of seekbar position--
    public static final String BROADCAST_SEEKBAR = "com.example.radiorain.sendseekbar";
    Intent intent;

    // Progress dialogue and broadcast receiver variables
    boolean mBufferBroadcastIsRegistered;
    private     LoadingDialoge  pdBuff = new LoadingDialoge(RadioPlayer.this);

    ImageView imageviewAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame); //Remember this is the FrameLayout area within your activity_main.xml
        getLayoutInflater().inflate(R.layout.activity_radio_player, contentFrameLayout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(0).setChecked(true);
        //setContentView(R.layout.activity_main);



//        stopService(new Intent(this,MusicPlayerServices.class));
//        unbindService(serviceConnection);

//        TextView tv = (TextView)  findViewById(R.id.scrollTop);
//        tv.setSelected(true);
        try {
            serviceIntent = new Intent(this, myPlayService.class);

            // --- set up seekbar intent for broadcasting new position to service ---
            intent = new Intent(BROADCAST_SEEKBAR);

            initViews();
            setListeners();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),
                    e.getClass().getName() + " " + e.getMessage(),
                    Toast.LENGTH_LONG).show();
        }
//        pref = getSharedPreferences("user_details",context.MODE_PRIVATE);
//        String musicUrl  =    pref.getString("playlist",null);
//        String name = pref.getString("username",null);
//        serviceIntent.putExtra("name",name);
//     serviceIntent.putExtra("sentAudioLink", musicUrl);
//
//          ContextCompat.startForegroundService(this, serviceIntent);

    //     imageviewAdd=(ImageView) findViewById(R.id.imageAdd);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                buttonPlayStop.performClick();
             }
        }, 1000);

        ic_instagram = (ImageButton)  findViewById(R.id.ic_instagram);
        ic_facebook =(ImageButton) findViewById(R.id.ic_facebook);
      //  ic_twitter = (ImageButton) findViewById(R.id.ic_twitter);
        ic_youtube = (ImageButton) findViewById(R.id.ic_youtube);
        ic_whatsapp = (ImageButton) findViewById(R.id.ic_whatsapp);

        ic_whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String text = "";// Replace with your message.
                    String toNumber = "+918078121006"; // Replace with mobile phone number without +Sign or leading zeros, but with country code
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("http://api.whatsapp.com/send?phone=" + toNumber + "&text=" + text));
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        ic_youtube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://www.youtube.com/channel/UCWzRgT1ToiFkgAprZPAEFMg/featured?view_as=subscriber";
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        });



//        ic_twitter.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String url = "https://twitter.com/RadioRangh/";
//                Intent intent = new Intent(Intent.ACTION_VIEW);
//                intent.setData(Uri.parse(url));
//                startActivity(intent);
//            }
//        });


        ic_instagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://www.instagram.com/radiorangh/";
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        });

        ic_facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://www.facebook.com/radiorangh/";
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        });



    }





    // -- Broadcast Receiver to update position of seekbar from service --
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent serviceIntent) {
            updateUI(serviceIntent);
        }
    };

    private void updateUI(Intent serviceIntent) {
        String counter = serviceIntent.getStringExtra("counter");
        String mediamax = serviceIntent.getStringExtra("mediamax");
        String strSongEnded = serviceIntent.getStringExtra("song_ended");
        int seekProgress = Integer.parseInt(counter);
        seekMax = Integer.parseInt(mediamax);
        songEnded = Integer.parseInt(strSongEnded);
        seekBar.setMax(seekMax);
        seekBar.setProgress(seekProgress);
        if (songEnded == 1) {
            buttonPlayStop.setImageResource(R.drawable.playbtn);
        }
    }

    // --End of seekbar update code--

    // --- Set up initial screen ---
    private void initViews() {
        buttonPlayStop = (ImageButton) findViewById(R.id.buttonPlayStop);
        buttonPlayStop.setImageResource(R.drawable.playbtn);

        // --Reference seekbar in main.xml
        seekBar = (SeekBar) findViewById(R.id.SeekBar01);
    }

    // --- Set up listeners ---
    private void setListeners() {
        buttonPlayStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonPlayStopClick();
            }
        });
        seekBar.setOnSeekBarChangeListener(this);

    }


    // --- invoked from ButtonPlayStop listener above ----
    private void buttonPlayStopClick() {
        if (!boolMusicPlaying) {
            buttonPlayStop.setImageResource(R.drawable.pausebutton);
            playAudio();
            boolMusicPlaying = true;
        } else {
            if (boolMusicPlaying) {
                buttonPlayStop.setImageResource(R.drawable.playbtn);
                stopMyPlayService();
                boolMusicPlaying = false;
            }
        }
    }

    // --- Stop service (and music) ---
    private void stopMyPlayService() {
        // --Unregister broadcastReceiver for seekbar
        if (mBroadcastIsRegistered) {
            try {
                unregisterReceiver(broadcastReceiver);
                mBroadcastIsRegistered = false;
            } catch (Exception e) {
                // Log.e(TAG, "Error in Activity", e);
                // TODO Auto-generated catch block

                e.printStackTrace();
                Toast.makeText(

                        getApplicationContext(),

                        e.getClass().getName() + " " + e.getMessage(),

                        Toast.LENGTH_LONG).show();
            }
        }

        try {
            stopService(serviceIntent);


        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),
                    e.getClass().getName() + " " + e.getMessage(),
                    Toast.LENGTH_LONG).show();
        }
        boolMusicPlaying = false;
    }

    // --- Start service and play music ---
    private void playAudio() {

        checkConnectivity();
        if (isOnline) {
            stopMyPlayService();

            pref = getSharedPreferences("user_details",context.MODE_PRIVATE);
            String musicUrl  =    pref.getString("playlist",null);
            serviceIntent.putExtra("sentAudioLink", musicUrl);

            try {
                //  startService(serviceIntent);
                ContextCompat.startForegroundService(this, serviceIntent);
                buttonPlayStop.setImageResource(R.drawable.pausebutton);

            } catch (Exception e) {

                e.printStackTrace();
                Toast.makeText(

                        getApplicationContext(),

                        e.getClass().getName() + " " + e.getMessage(),

                        Toast.LENGTH_LONG).show();
            }

            // -- Register receiver for seekbar--
            registerReceiver(broadcastReceiver, new IntentFilter(
                    myPlayService.BROADCAST_ACTION));
            ;
            mBroadcastIsRegistered = true;

        } else {
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Network Not Connected...");
            alertDialog.setMessage("Please connect to a network and try again");
//            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    // here you can add functions
//                }
//            });
            alertDialog.setIcon(R.drawable.ic_alarm_add_black_24dp);
            buttonPlayStop.setImageResource(R.drawable.playbtn);
            alertDialog.show();
        }
    }

    // Handle progress dialogue for buffering...
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void showPD(Intent bufferIntent) {
        String bufferValue = bufferIntent.getStringExtra("buffering");
        int bufferIntValue = Integer.parseInt(bufferValue);

        // When the broadcasted "buffering" value is 1, show "Buffering"
        // progress dialogue.
        // When the broadcasted "buffering" value is 0, dismiss the progress
        // dialogue.

        switch (bufferIntValue) {
            case 0:
                // Log.v(TAG, "BufferIntValue=0 RemoveBufferDialogue");
                // txtBuffer.setText("");
                if (pdBuff != null) {
                    pdBuff.dismiss();
                    pref = getSharedPreferences("user_details",context.MODE_PRIVATE);

                    String imageAddUrl  =    pref.getString("addImage",null);

                    if (!imageAddUrl.isEmpty()) {
                        dialog(imageAddUrl);


                    } else {
                        //  dialog(imageAddUrl);
                    }
                }
                break;

            case 1:
                BufferDialogue();
                break;

            // Listen for "2" to reset the button to a play button
            case 2:
                buttonPlayStop.setImageResource(R.drawable.playbtn);
                break;

        }
    }

    // Progress dialogue...
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void BufferDialogue() {
//        ProgressDialog pdBuff = new ProgressDialog(this);
//         pdBuff.setIndeterminate(true);
//        pdBuff.setIndeterminateDrawable(getResources().getDrawable(R.drawable.customeprogressbar, null));


        //  pdBuff = ProgressDialog.show(RadioPlayer.this, "Buffering...", "Acquiring song...", true);

//        pdBuff = new ProgressDialog(this);
//
//        pdBuff.setCancelable(true);
//        pdBuff.setIcon(1);
//        pdBuff.setTitle("Buffering...");
//        pdBuff.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//        pdBuff.setIndeterminate(true);
//        pdBuff.setIndeterminateDrawable(getResources().getDrawable(R.drawable.customeprogressbar, null));
//         pdBuff.show();
        // pdBuff.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT))
        pdBuff.startLoadingDialog();

    }

    // Set up broadcast receiver
    private BroadcastReceiver broadcastBufferReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent bufferIntent) {
            showPD(bufferIntent);
        }
    };

    private void checkConnectivity() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        if (cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
                .isConnectedOrConnecting()
                || cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                .isConnectedOrConnecting())
            isOnline = true;
        else
            isOnline = false;
    }

    // -- onPause, unregister broadcast receiver. To improve, also save screen data ---
    @Override
    protected void onPause() {
        // Unregister broadcast receiver
        if (mBufferBroadcastIsRegistered) {
            unregisterReceiver(broadcastBufferReceiver);
            mBufferBroadcastIsRegistered = false;
        }
        super.onPause();
    }


    // -- onResume register broadcast receiver. To improve, retrieve saved screen data ---
    @Override
    protected void onResume() {
        // Register broadcast receiver
        if (!mBufferBroadcastIsRegistered) {
            registerReceiver(broadcastBufferReceiver, new IntentFilter(
                    myPlayService.BROADCAST_BUFFER));
            mBufferBroadcastIsRegistered = true;
        }
        super.onResume();
    }


    @Override
    public void onBackPressed() {
        stopMyPlayService();
        super.onBackPressed();
    }

    // --- When user manually moves seekbar, broadcast new position to service ---
    @Override
    public void onProgressChanged(SeekBar sb, int progress, boolean fromUser) {
        // TODO Auto-generated method stub
        if (fromUser) {
            int seekPos = sb.getProgress();
            intent.putExtra("seekpos", seekPos);
            sendBroadcast(intent);
        }
    }


    // --- The following two methods are alternatives to track seekbar if moved.
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        // TODO Auto-generated method stub

    }


    private void dialog(String imageAddUrl) {
        TextView textView;
        ImageView imageViews;
          final AlertDialog alertDialog;
          ImageButton imageButton;

        AlertDialog.Builder dialog  = new AlertDialog.Builder(RadioPlayer.this);

        LayoutInflater inflater = this.getLayoutInflater();
        View team = inflater.inflate(R.layout.addlayout, null);

        imageButton = (ImageButton) team.findViewById(R.id.idclose);




//        textView = (TextView) team.findViewById(R.id.colorTitle);
//        textView.setText(imageAddUrl);

       imageViews = (ImageView) team.findViewById(R.id.addImage);
//    imageView = (ImageView) team.findViewById(R.id.addImage);
// imageView.setImageResource(Integer.parseInt(imageAddUrl));
        Glide.with(getApplicationContext())
                .load(imageAddUrl)
                .error(R.drawable.ic_youtube) //in case of error this is displayed
                .into(imageViews);
        dialog.setView(team);
        alertDialog =   dialog.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
alertDialog.dismiss();
            }
        });
    }

}