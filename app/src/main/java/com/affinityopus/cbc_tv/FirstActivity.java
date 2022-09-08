package com.affinityopus.cbc_tv;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.MediaController;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
/*import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;*/

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FirstActivity extends AppCompatActivity {
    private static final String URL =  "http://cbctv.in/app/main.php";
    private List<FirstData> firstData;
    private RecyclerView recyclerView;
    private FirstAdapter adapter;
    ShimmerFrameLayout shimmerFrameLayout;

    SwipeRefreshLayout swipeRefreshLayout;
    RequestQueue queue;

    MediaController media_Controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        shimmerFrameLayout = findViewById(R.id.shimmer_view_container);
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        firstData = new ArrayList<>();
        adapter = new FirstAdapter(firstData,this);

        showShimmer();
        new Handler().postDelayed(new Runnable() {



            @Override
            public void run() {

                getMovieData();
                hideShimmer();

                adapter.notifyDataSetChanged();
            }
        },3000);

        swipeRefreshLayout = findViewById(R.id.swipeContainer);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {


                Toast.makeText(getApplicationContext(), "Refreshing!", Toast.LENGTH_LONG).show();
                // To keep animation for 4 seconds
                new Handler().postDelayed(new Runnable() {
                    @Override public void run() {
                        firstData.clear();

                        refreshgetMovieData();
                        // adapter.notifyDataSetChanged();

                        swipeRefreshLayout.setRefreshing(false);

                        hideShimmer();
                        adapter.notifyDataSetChanged();
                    }
                }, 3000); // Delay in millis


            }
        });

        swipeRefreshLayout.setColorSchemeColors(
                getResources().getColor(android.R.color.holo_blue_bright),
                getResources().getColor(android.R.color.holo_green_light),
                getResources().getColor(android.R.color.holo_orange_light),
                getResources().getColor(android.R.color.holo_red_light)
        );


        youtubeRequest();

    }




    public void youtubeRequest() {
        String URLS = "http://cbctv.in/app/homevideo.php";

        queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.GET, URLS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                 try {
                    JSONObject object=new JSONObject(response);
//                    JSONArray array=object.getJSONArray("data");
//                    JSONObject val = object.getJSONObject("data");
                     final String vald = object.getString("data");
                     Log.d("Success",vald );
                     YouTubePlayerView youTubePlayerView =(YouTubePlayerView)findViewById(R.id.youtube_player_view);
                     MediaController mediaController= new MediaController(FirstActivity.this);
                     mediaController.setAnchorView(youTubePlayerView);
                     Uri uri = Uri.parse(vald);
                     getLifecycle().addObserver(youTubePlayerView);
                     youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {


                       //  String musicUrl = getIntent().getExtras().getString("playlist");
                         @Override
                         public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                             //  String videoId = "S0Q4gqBUs7c";
                             youTubePlayer.loadVideo(vald , 0);
                         }
                     });
//                    for(int i=0;i<array.length();i++) {
//                        JSONObject object1=array.getJSONObject(i);
//                        String name =object1.getString("name");
//                     }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("error",error.toString());
            }
        });
        queue.add(request);
    }


    private void getMovieData() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    JSONArray array=jsonObject.getJSONArray("data");
                    for (int i=0; i<array.length(); i++ ){
                        JSONObject ob=array.getJSONObject(i);
                        FirstData listData=new FirstData(ob.getString("id"),


                                ob.getString("imageUrl") );
                        firstData.add(listData);

                    }
                    recyclerView.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                    //  Toast.makeText(DetailsActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                // Toast.makeText(DetailsActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }




    private void refreshgetMovieData() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    JSONArray array=jsonObject.getJSONArray("data");
                    for (int i=0; i<array.length(); i++ ){
                        JSONObject ob=array.getJSONObject(i);
                        FirstData listData=new FirstData(ob.getString("id"),

                                ob.getString("imageUrl") );

                        //  music_data.clear();
                        firstData.add(listData);

                    }
                    recyclerView.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                    //  Toast.makeText(DetailsActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                // Toast.makeText(DetailsActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }










    private void hideShimmer() {
        shimmerFrameLayout.stopShimmer();
        shimmerFrameLayout.setVisibility(View.GONE);
    }

    private void showShimmer() {
        shimmerFrameLayout.startShimmer();
        shimmerFrameLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Really You Want Exit?")
                .setMessage("Are You Sure")
                .setNegativeButton("NO", null)
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        //  HomeActivity.super.onBackPressed();
                        Intent a = new Intent(Intent.ACTION_MAIN);
                        a.addCategory(Intent.CATEGORY_HOME);
                        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(a);
                    }
                }).create().show();
    }
}