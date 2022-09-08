package com.affinityopus.cbc_tv;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

public class CallReceiver extends BroadcastReceiver {
    TelephonyManager telManager;
    Context context;


    MediaPlayer mediaPlayer;

    @Override
    public void onReceive(Context context, Intent intent) {


        this.context=context;

        telManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        telManager.listen(phoneListener, PhoneStateListener.LISTEN_CALL_STATE);

    }

    private final PhoneStateListener phoneListener = new PhoneStateListener() {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            try {
                switch (state) {
                    case TelephonyManager.CALL_STATE_RINGING: {
                        /* PAUSE */
                        mediaPlayer.pause();
                        break;
                    }
                    case TelephonyManager.CALL_STATE_OFFHOOK: {
                        mediaPlayer.pause();
                        break;
                    }
                    case TelephonyManager.CALL_STATE_IDLE: {
                        mediaPlayer.start();
                        break;
                    }
                    default: { }
                }
            } catch (Exception ex) {

            }
        }
    };
}