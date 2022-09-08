package com.affinityopus.cbc_tv;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
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

public class ScheduleActivity extends BaseActivity {

    private static final String URL =  "http://radiorangh.com/app/schedule.php";
    ArrayList<ScheduleData> scheduleData = new ArrayList<>();
    private RecyclerView recyclerView;
    private ScheduleAdapter adapter;
    ShimmerFrameLayout shimmerFrameLayout;

    SwipeRefreshLayout swipeRefreshLayout;
 //   SharedPreferences pref;

    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame); //Remember this is the FrameLayout area within your activity_main.xml
        getLayoutInflater().inflate(R.layout.activity_schedule, contentFrameLayout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(0).setChecked(true);
// super.onCreate(savedInstanceState);
//    setContentView(R.layout.activity_radio_play_list);


//        SharedPreferences pref = getSharedPreferences("user_details", MODE_PRIVATE);  //Activity1.class
//        if(pref.contains("username")) {
//            Intent intent = new Intent(this, RadioPlayer.class);
//            startActivity(intent);
//        }







        recyclerView =  findViewById(R.id.recyclerview);

        shimmerFrameLayout = findViewById(R.id.shimmer_view_container);

        swipeRefreshLayout = findViewById(R.id.swipeContainer);


        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        scheduleData = new ArrayList<>();
        adapter = new ScheduleAdapter(scheduleData,this);



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
                        scheduleData.clear();

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

    private void getMovieData() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {


                    JSONObject jsonObject=new JSONObject(response);
                    JSONArray array=jsonObject.getJSONArray("data");
                    for (int i=0; i<array.length(); i++ ){
                        JSONObject ob=array.getJSONObject(i);
                        ScheduleData listData=new ScheduleData(ob.getString("title"),
                                ob.getString("imageUrl")
                                 );
                        scheduleData.add(listData);


                    }


                    recyclerView.setAdapter(adapter);
//
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(ScheduleActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(ScheduleActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();

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
