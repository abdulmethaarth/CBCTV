package com.affinityopus.cbc_tv;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

   // private List<List_Data>list_data;
   ArrayList<List_Data> list_data = new ArrayList<>();
    Context context;

    SharedPreferences pref;

    boolean isShimmer = true;
    int shimmerNumber = 15;
    private MusicPlayerServices  player;
    boolean serviceBound = false;
    public MyAdapter(ArrayList<List_Data> list_data, Context context) {
        this.list_data = list_data;
        this.context = context;
    }

    //    public MyAdapter(List<List_Data> list_data,Context context) {
//        this.list_data = list_data;
//        this.context = context;
//    }
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_data,parent,false);
         return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final List_Data  listData=list_data.get(position);


//            holder.shimmerFrameLayout.stopShimmer();
//            holder.shimmerFrameLayout.setShimmer(null);
             holder.txtname.setBackground(null);

            holder.txtname.setText(listData.getName());

            holder.imageView.setBackground( null);


         Glide.with(context).load(listData.getImageurl()).into(holder.imageView);

          // holder.imageView.setImageDrawable(context.getDrawable(R.drawable.background_splash));


        // holder.txtmovie.setText(listData.getMoviename());


        holder.relative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =  new Intent(context,RadioPlayer.class);
                pref = context.getSharedPreferences("user_details",context.MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("username",listData.getName());


                editor.putString("playlist",listData.getPlaylist());
                editor.putString("addUrl",listData.getAddUrl());
                editor.putString("addImage",listData.getAddImage());





                editor.commit();
                context.startActivity(intent);
                Intent stopintent = new Intent(context, myPlayService.class);
                context.stopService(stopintent);
                Intent musicintent = new Intent(context,MusicPlayerServices.class);
                context.stopService(musicintent);

            //    context.stopService(new Intent(context, MusicPlayerServices.class));
//                context.unbindService(serviceConnection);

            }


        });


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

    @Override
    public int getItemCount() {
        return isShimmer ? shimmerNumber:list_data.size();
    }

    public  class  ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtname,txtmovie,linear;
        ShimmerFrameLayout shimmerFrameLayout;
        //        private RelativeLayout relative;
        RelativeLayout relative;


        ImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

             relative =  itemView.findViewById(R.id.relative);

            txtname=(TextView)itemView.findViewById(R.id.txt_name);
            //   txtmovie=(TextView)itemView.findViewById(R.id.txt_moviename);
            // linear=(RelativeLayout) itemView.findViewById(R.id.linear);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);

//
//            pref = context.getSharedPreferences("user_details",context.MODE_PRIVATE);
//            if(pref.contains("username")) {
//                  context.startActivity(new Intent(context,RadioPlayer.class));
//            }
//            else {
//                   context.startActivity(new Intent(context,MainActivity.class));
//            }


        }
    }

}

