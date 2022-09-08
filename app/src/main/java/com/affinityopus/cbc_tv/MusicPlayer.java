/*
package com.affinityopus.cbctv;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.ServiceConnection;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.SeekBar.OnSeekBarChangeListener;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.google.android.material.navigation.NavigationView;

public class MusicPlayer  extends BaseActivity  implements OnSeekBarChangeListener {
    Intent serviceIntent;
    private ImageButton buttonPlayStop,ic_instagram,ic_facebook,ic_youtube,ic_twitter,ic_whatsapp;

    // -- PUT THE NAME OF YOUR AUDIO FILE HERE...URL GOES IN THE SERVICE
    String strAudioLink = "http://radiorangh.com/admin/CI-admin//assets/images/podcast/song.mp3";

    private boolean isOnline;
    private boolean boolMusicPlaying = false;


    private MusicPlayerServices  player;
    boolean serviceBound = false;
    TelephonyManager telephonyManager;
    PhoneStateListener listener;

    // --Seekbar variables --
    private SeekBar seekBar;
    private int seekMax;
    private static int songEnded = 0;
    boolean mBroadcastIsRegistered;

    TextView textCurrentTime,textTotalDuration;

    // --Set up constant ID for broadcast of seekbar position--
    public static final String BROADCAST_SEEKBAR = "com.affinityopus.radiorangh.sendseekbar";
    Intent intent;


    Button pause;
    // Progress dialogue and broadcast receiver variables
    boolean mBufferBroadcastIsRegistered;
    private ProgressDialog  pdBuff = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame); //Remember this is the FrameLayout area within your activity_main.xml
        getLayoutInflater().inflate(R.layout.activity_music_player, contentFrameLayout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(0).setChecked(true);



        try {
            serviceIntent = new Intent(this, MusicPlayerServices.class);

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
        long  currentDuration = serviceIntent.getLongExtra("currentDuration",1);
        long totalDuration  = serviceIntent.getLongExtra("totalDuration",1);
        textTotalDuration.setText(milliSecondsToTimer(totalDuration));
        textCurrentTime.setText(milliSecondsToTimer(currentDuration));
         Log.d("updateUI","" + counter + mediamax);
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
        textCurrentTime =  findViewById(R.id.textCurrentTime);
        textTotalDuration =  findViewById(R.id.textTotalDuration);
        // --Reference seekbar in main.xml

//        ic_instagram = (ImageButton)  findViewById(R.id.ic_instagram);
//        ic_facebook =(ImageButton) findViewById(R.id.ic_facebook);
//        //  ic_twitter = (ImageButton) findViewById(R.id.ic_twitter);
//        ic_youtube = (ImageButton) findViewById(R.id.ic_youtube);
//        ic_whatsapp = (ImageButton) findViewById(R.id.ic_whatsapp);
//
//
        ActionBar actionBar;
        actionBar = getSupportActionBar();
//        ColorDrawable colorDrawable
//                = new ColorDrawable(Color.parseColor(ContextCompat.getColor(context,R.color.feedbackBG)));
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(getApplicationContext(),R.color.frontTop)));
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        //  getSupportActionBar().setTitle("Hooom");
        Toolbar toolbarTop = (Toolbar) findViewById(R.id.toolbar);
        TextView mTitle = (TextView) toolbarTop.findViewById(R.id.toolbar_title);
      //  mTitle.setVisibility(View.VISIBLE);
        //mTitle.setText("PODCAST");



//        ic_twitter.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String url = "https://twitter.com/RadioRangh/";
//                Intent intent = new Intent(Intent.ACTION_VIEW);
//                intent.setData(Uri.parse(url));
//                startActivity(intent);
//            }
//        });





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
            //	playAudio();
            playAudios( getIntent().getExtras().getString("playlist"));
            boolMusicPlaying = true;
        } else {
            if (boolMusicPlaying) {
                buttonPlayStop.setImageResource(R.drawable.playbtn);
                //		stopMyPlayService();
                if(serviceBound && player.isPlaying() && !player.isStopped())
                    player.pauseMedia();
                boolMusicPlaying = false;
            }
        }

        Log.d("status",""+boolMusicPlaying);
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

            serviceIntent.putExtra("sentAudioLink", strAudioLink);

            try {
                startService(serviceIntent);
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
            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // here you can add functions
                }
            });
            alertDialog.setIcon(R.drawable.logo);
            buttonPlayStop.setImageResource(R.drawable.playbtn);
            alertDialog.show();
        }
    }

    // Handle progress dialogue for buffering...
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
    private void BufferDialogue() {

        pdBuff = ProgressDialog.show(MusicPlayer.this, "Buffering...",
                "Acquiring song...", true);
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


    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            MusicPlayerServices.LocalBinder binder = (MusicPlayerServices.LocalBinder) service;
            player = binder.getService();
            serviceBound = true;

         //   Toast.makeText(MusicPlayer.this, "Service Bound", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            serviceBound = false;
        }
    };


    private void playAudios(String media) {
        //Check is service is active
        checkConnectivity();
        if(isOnline) {
            if (!serviceBound) {
                Intent playerIntent = new Intent(this, MusicPlayerServices.class);
                playerIntent.putExtra("media", media);
                playerIntent.putExtra("play", true);
                startService(playerIntent);
                bindService(playerIntent, serviceConnection, Context.BIND_AUTO_CREATE);
            } else {

                //Service is active
                //Send media with BroadcastReceiver
                if (!player.isStopped())
                    player.playMedia();

                else {

                    Intent playerIntent = new Intent(this, MusicPlayerServices.class);
                    playerIntent.putExtra("media", media);
                    playerIntent.putExtra("play", true);
                    startService(playerIntent);
                    bindService(playerIntent, serviceConnection, Context.BIND_AUTO_CREATE);


                }
            }
            registerReceiver(broadcastReceiver, new IntentFilter(
                    MusicPlayerServices.BROADCAST_ACTION));

            mBroadcastIsRegistered = true;
        }

    else {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Network Not Connected...");
        alertDialog.setMessage("Please connect to a network and try again");
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // here you can add functions
            }
        });
        alertDialog.setIcon(R.drawable.logo);
        buttonPlayStop.setImageResource(R.drawable.playbtn);
        alertDialog.show();
    }
    }

    private String milliSecondsToTimer(long  millSeconds) {
        String timerString="";
        String secondString="";


        int hours = (int)(millSeconds /(1000 * 60 * 60 ));
        int minutes = (int)(millSeconds % (1000 * 60 * 60)) /(1000*60);
        int seconds = (int)((millSeconds % (1000 *60 * 60)) % (1000 *  60) / 1000);

        if(hours > 0) {
            timerString = hours + ":";
        }

        if(seconds < 10) {
            secondString = "0" + seconds;
        } else {
            secondString = "" + seconds;
        }
        timerString = timerString + minutes + ":" + secondString;
        return  timerString;
    }



}*/
