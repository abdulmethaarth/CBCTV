package com.affinityopus.cbc_tv;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.OrientationEventListener;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.cast.framework.CastButtonFactory;
import com.google.android.material.navigation.NavigationView;


public class LiveStramActivity extends  BaseActivity {

    //THEOplayerView theoPlayerView;
    Button btnPlayPause;
    TextView txtPlayStatus, txtTimeUpdate;
    WebView wv_web;
    ImageView cast,back;

    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);


        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            getSupportActionBar().hide();
            //  getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            getSupportActionBar().show();
            //       getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_live_stram);

        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame); //Remember this is the FrameLayout area within your activity_main.xml
        getLayoutInflater().inflate(R.layout.activity_live_stram, contentFrameLayout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(2).setChecked(true);

        ActionBar actionBar;
        actionBar = getSupportActionBar();
//        ColorDrawable colorDrawable
//                = new ColorDrawable(Color.parseColor(ContextCompat.getColor(context,R.color.feedbackBG)));
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(getApplicationContext(), R.color.reelsTop)));
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        //  getSupportActionBar().setTitle("Hooom");
        Toolbar toolbarTop = (Toolbar) findViewById(R.id.toolbar);
        TextView mTitle = (TextView) toolbarTop.findViewById(R.id.toolbar_title);
        mTitle.setText("Live Stream");

       /* ImageView backbtn = (ImageView) findViewById(R.id.backbtn);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inten = new Intent(LiveStramActivity.this, FirstActivity.class);
                startActivity(inten);
            }
        });
*/

        /*back = (ImageView)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LiveStramActivity.this, FirstActivity.class);
                startActivity(intent);
            }
        });*/
        wv_web=findViewById(R.id.wv_web);
        wv_web.getSettings().setLoadsImagesAutomatically(true);
        wv_web.getSettings().setJavaScriptEnabled(true);
        wv_web.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        wv_web.loadUrl("https://play.streamingvideoprovider.com/popplayer.php?it=eca8g19tj688&is_link=1&w=720&h=405&pause=1&title=CBCTV+TEST&skin=3&repeat=&brandNW=1&start_volume=34&bg_gradient1=%23ffffff&bg_gradient2=%23e9e9e9&fullscreen=1&fs_mode=2&skinAlpha=50&colorBase=%23250864&colorIcon=%23ffffff&colorHighlight=%237f54f8&direct=false&no_ctrl=&auto_hide=1&viewers_limit=0&cc_position=bottom&cc_positionOffset=70&cc_multiplier=0.03&cc_textColor=%23ffffff&cc_textOutlineColor=%23ffffff&cc_bkgColor=%23000000&cc_bkgAlpha=0.1&image=https%3A%2F%2Fmember.streamingvideoprovider.com%2Fpanel%2Fserver%2Fclip%3Fa%3DGenerateThumbnail%26clip_id%3D3964555%26size%3Dlarge&mainBg_Color=%23ffffff&aspect_ratio=16%3A9&play_button=1&play_button_style=pulsing&sleek_player=1&stretch=&auto_play=&auto_play_type=unMute&floating_player=none");
        //wv_web.loadUrl("https://play.streamingvideoprovider.com/popplayer.php?it=9j9ysc0egtoo&is_link=1&w=720&h=405&pause=1&title=My+live+stream&skin=3&repeat=&brandNW=1&start_volume=34&bg_gradient1=%23ffffff&bg_gradient2=%23e9e9e9&fullscreen=1&fs_mode=2&skinAlpha=50&colorBase=%23250864&colorIcon=%23ffffff&colorHighlight=%237f54f8&direct=false&no_ctrl=&auto_hide=1&viewers_limit=0&cc_position=bottom&cc_positionOffset=70&cc_multiplier=0.03&cc_textColor=%23ffffff&cc_textOutlineColor=%23ffffff&cc_bkgColor=%23000000&cc_bkgAlpha=0.1&image=https%3A%2F%2Fmember.streamingvideoprovider.com%2Fpanel%2Fserver%2Fclip%3Fa%3DGenerateThumbnail%26clip_id%3D3849409%26size%3Dlarge&mainBg_Color=%23ffffff&aspect_ratio=16%3A9&play_button=1&play_button_style=pulsing&sleek_player=1&stretch=&auto_play=&auto_play_type=unMute&floating_player=none");

        OrientationEventListener orientationEventListener = new OrientationEventListener(LiveStramActivity.this) {
            @Override
            public void onOrientationChanged(int orientation) {
                int epsilon = 10;
                int leftLandscape = 90;
                int rightLandscape = 270;
                if(epsilonCheck(orientation, leftLandscape, epsilon) ||
                        epsilonCheck(orientation, rightLandscape, epsilon)){
                    LiveStramActivity.this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
                }
            }

            private boolean epsilonCheck(int a, int b, int epsilon) {
                return a > b - epsilon && a < b + epsilon;
            }
        };
        orientationEventListener.enable();

    }



      /*  theoPlayerView = findViewById(R.id.theoplayer);


        *//*THEOplayerConfig playerConfig = new THEOplayerConfig.Builder()
                .chromeless(false)
                .defaultCss(true)
                .jsPaths("path/to/script.js")
                .build();
        theoPlayerView = new THEOplayerView(this, playerConfig);*//*


        theoPlayerView.addJavaScriptMessageListener("messagetype", new MessageListener() {
            @Override public void handleMessage(String message) {
                System.out.println("Received message from JavaScript: " + message);
                Toast.makeText(LiveStramActivity.this, ""+message, Toast.LENGTH_SHORT).show();
            }
        });

        TypedSource typedSource = TypedSource.Builder
                .typedSource()
                .src("https://play.streamingvideoprovider.com/popplayer.php?it=9j9ysc0egtoo&is_link=1&w=720&h=405&pause=1&title=My+live+stream&skin=3&repeat=&brandNW=1&start_volume=34&bg_gradient1=%23ffffff&bg_gradient2=%23e9e9e9&fullscreen=1&fs_mode=2&skinAlpha=50&colorBase=%23250864&colorIcon=%23ffffff&colorHighlight=%237f54f8&direct=false&no_ctrl=&auto_hide=1&viewers_limit=0&cc_position=bottom&cc_positionOffset=70&cc_multiplier=0.03&cc_textColor=%23ffffff&cc_textOutlineColor=%23ffffff&cc_bkgColor=%23000000&cc_bkgAlpha=0.1&image=https%3A%2F%2Fmember.streamingvideoprovider.com%2Fpanel%2Fserver%2Fclip%3Fa%3DGenerateThumbnail%26clip_id%3D3849409%26size%3Dlarge&mainBg_Color=%23ffffff&aspect_ratio=16%3A9&play_button=1&play_button_style=pulsing&sleek_player=1&stretch=&auto_play=&auto_play_type=unMute&floating_player=none")
                // .src("https://play.streamingvideoprovider.com/popplayer.php?it=3xc9bobrgiec&is_link=1&w=720&h=405&pause=1&title=My+live+stream&skin=3&repeat=&brandNW=1&start_volume=34&bg_gradient1=%23ffffff&bg_gradient2=%23e9e9e9&fullscreen=1&fs_mode=2&skinAlpha=50&colorBase=%23250864&colorIcon=%23ffffff&colorHighlight=%237f54f8&direct=false&no_ctrl=&auto_hide=1&viewers_limit=0&cc_position=bottom&cc_positionOffset=70&cc_multiplier=0.03&cc_textColor=%23ffffff&cc_textOutlineColor=%23ffffff&cc_bkgColor=%23000000&cc_bkgAlpha=0.1&image=https%3A%2F%2Fmember.streamingvideoprovider.com%2Fpanel%2Fserver%2Fclip%3Fa%3DGenerateThumbnail%26clip_id%3D3841797%26size%3Dlarge&mainBg_Color=%23ffffff&aspect_ratio=16%3A9&play_button=1&play_button_style=pulsing&sleek_player=1&stretch=&auto_play=&auto_play_type=unMute&floating_player=none")
                .type(SourceType.DASH)
                .build();

        SourceDescription sourceDescription = SourceDescription.Builder
                .sourceDescription(typedSource)
                .build();

        MediaController mediaController= new MediaController(LiveStramActivity.this);
        mediaController.setAnchorView(theoPlayerView);

        theoPlayerView.getPlayer().setSource(sourceDescription);

        boolean isLive = Double.isNaN(theoPlayerView.getPlayer().getDuration());
        btnPlayPause = findViewById(R.id.btn_playpause);
        btnPlayPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (theoPlayerView.getPlayer().isPaused()) {
                    theoPlayerView.getPlayer().play();

                } else {
                    theoPlayerView.getPlayer().pause();
                }
            }
        });

        txtPlayStatus = findViewById(R.id.txt_playstatus);
        txtTimeUpdate = findViewById(R.id.txt_timeupdate);

        theoPlayerView.getPlayer().addEventListener(PlayerEventTypes.PLAY, new EventListener<PlayEvent>() {
            @Override
            public void handleEvent(PlayEvent playEvent) {
                txtPlayStatus.setText("Playing");
                Toast.makeText(LiveStramActivity.this, ""+isLive, Toast.LENGTH_SHORT).show();
            }
        });

        theoPlayerView.getPlayer().addEventListener(PlayerEventTypes.PAUSE, new EventListener<PauseEvent>() {
            @Override
            public void handleEvent(PauseEvent pauseEvent) {
                txtPlayStatus.setText("Paused");
                Toast.makeText(LiveStramActivity.this, ""+isLive, Toast.LENGTH_SHORT).show();
            }
        });

        theoPlayerView.getPlayer().addEventListener(PlayerEventTypes.TIMEUPDATE, new EventListener<TimeUpdateEvent>() {
            @Override
            public void handleEvent(TimeUpdateEvent timeUpdateEvent) {
                txtTimeUpdate.setText(String.valueOf(timeUpdateEvent.getCurrentTime()));
            }
        });*/


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.activity_player_menu, menu);
        CastButtonFactory.setUpMediaRouteButton(getApplicationContext(), menu, R.id.castMenuItem);
        return true;
    }

   /* @Override
    protected void onPause() {
        super.onPause();
        theoPlayerView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        theoPlayerView.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        theoPlayerView.onDestroy();
    }*/

}