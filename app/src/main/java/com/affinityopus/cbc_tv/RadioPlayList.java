package com.affinityopus.cbc_tv;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class RadioPlayList extends BaseActivity {
    private static final String URL =  "http://radiorangh.com/app/app.php";
    ArrayList<List_Data>list_data = new ArrayList<>();
    private RecyclerView recyclerView;
    private MyAdapter adapter;
    ShimmerFrameLayout shimmerFrameLayout;

    SwipeRefreshLayout swipeRefreshLayout;
    SharedPreferences pref;
    private MusicPlayerServices  player;
    boolean serviceBound = false;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame); //Remember this is the FrameLayout area within your activity_main.xml
        getLayoutInflater().inflate(R.layout.activity_radio_play_list, contentFrameLayout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(0).setChecked(true);
// super.onCreate(savedInstanceState);
//    setContentView(R.layout.activity_radio_play_list);


        SharedPreferences pref = getSharedPreferences("user_details", MODE_PRIVATE);  //Activity1.class
        if(pref.contains("username")) {

            Intent intent = new Intent(this, RadioPlayer.class);
            startActivity(intent);

        }







        recyclerView =  findViewById(R.id.recyclerview);

        shimmerFrameLayout = findViewById(R.id.shimmer_view_container);

        swipeRefreshLayout = findViewById(R.id.swipeContainer);


        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list_data = new ArrayList<>();
        adapter = new MyAdapter(list_data,this);



        // Set the color

         //getMovieData();
        showShimmer();
        new Handler().postDelayed(new Runnable() {

            View someView = findViewById(R.id.radioPlayer);// get Any child View

            // Find the root view
            View root = someView.getRootView();
            @Override
            public void run() {
                root.setBackgroundColor(getResources().getColor(R.color.mainBg));
                getMovieData();
                hideShimmer();
                adapter.isShimmer = false;
                adapter.notifyDataSetChanged();
            }
        },3000);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {


                Toast.makeText(getApplicationContext(), "Refreshing!", Toast.LENGTH_LONG).show();
                // To keep animation for 4 seconds
                new Handler().postDelayed(new Runnable() {
                    @Override public void run() {
                        list_data.clear();

                        // Stop animation (This will be after 3 seconds)
                        swipeRefreshLayout.setRefreshing(false);
                        getMovieData();
                        hideShimmer();
                        adapter.isShimmer = false;
                        adapter.notifyDataSetChanged();
                    }
                }, 4000); // Delay in millis


            }
        });

        swipeRefreshLayout.setColorSchemeColors(
                getResources().getColor(android.R.color.holo_blue_bright),
                getResources().getColor(android.R.color.holo_green_light),
                getResources().getColor(android.R.color.holo_orange_light),
                getResources().getColor(android.R.color.holo_red_light)
        );

    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            MusicPlayerServices.LocalBinder binder = (MusicPlayerServices.LocalBinder) service;
            player = binder.getService();

            //  player.stopMedia();
            serviceBound = true;

            Toast.makeText(RadioPlayList.this, "Service Bound", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            serviceBound = false;
        }

    };


    private void getMovieData() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {


                    JSONObject jsonObject=new JSONObject(response);
                    JSONArray array=jsonObject.getJSONArray("data");
                    for (int i=0; i<array.length(); i++ ){
                        JSONObject ob=array.getJSONObject(i);
                        List_Data listData=new List_Data(ob.getString("name"),ob.getString("playlist")
                                ,ob.getString("imageUrl"),ob.getString("addUrl"),ob.getString("addImage") );
                        list_data.add(listData);


                    }


                    recyclerView.setAdapter(adapter);
//
                } catch (JSONException e) {
                    e.printStackTrace();
                   Toast.makeText(RadioPlayList.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

             Toast.makeText(RadioPlayList.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();

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
}
