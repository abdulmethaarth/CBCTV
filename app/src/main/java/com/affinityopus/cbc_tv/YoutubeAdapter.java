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
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class YoutubeAdapter extends RecyclerView.Adapter<YoutubeAdapter.ViewHolder> {

    private List<Youtube_Data>youtube_data;
    Context context;

    SharedPreferences pref;

    private MusicPlayerServices  player;
    boolean serviceBound = false;

    public YoutubeAdapter(List<Youtube_Data> youtube_data, Context context) {
        this.youtube_data = youtube_data;
        this.context = context;
    }
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.youtube_data,parent,false);
        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Youtube_Data  listData=youtube_data.get(position);
       holder.txtname.setText(listData.getVideoname());
        // holder.txtmovie.setText(listData.getMoviename());


            //Glide.with(context).load(listData.getImageUrl()).into(holder.imageView);
        Glide.with(context)
                .load(listData.getImageUrl())
                .error(R.drawable.ic_youtube) //in case of error this is displayed
                .into(holder.imageView);
        holder.relative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, Youtube.class);
                intent.putExtra("playlist",listData.getVideo());
                context.startActivity(intent);
//                Intent stopintent = new Intent(context, myPlayService.class);
//                context.stopService(stopintent);

//                context.stopService(new Intent(context, MusicPlayerServices.class));
//                context.unbindService(serviceConnection);

         //     Toast.makeText(context, "Clicked ", Toast.LENGTH_SHORT).show();

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
        return  youtube_data.size();
    }

    public  class  ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtname,txtmovie,linear;
        //        private RelativeLayout relative;
        LinearLayout relative;
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
       imageView =  (ImageView) itemView.findViewById(R.id.imageView);

            relative =  itemView.findViewById(R.id.relative);

            txtname=(TextView)itemView.findViewById(R.id.txt_name);
            //   txtmovie=(TextView)itemView.findViewById(R.id.txt_moviename);
            // linear=(RelativeLayout) itemView.findViewById(R.id.linear);


            pref = context.getSharedPreferences("user_details",context.MODE_PRIVATE);
            if(pref.contains("username")) {
               // context.startActivity(new Intent(context,MainActivity.class));
            }
            else {
                //  context.startActivity(new Intent(context,MainActivity.class));
            }


        }
    }

}

