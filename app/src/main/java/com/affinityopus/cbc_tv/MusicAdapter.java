package com.affinityopus.cbc_tv;


import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
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

import java.util.List;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.ViewHolder> {

    private List<Music_Data>music_data;
    Context context;

    SharedPreferences pref;

    public MusicAdapter(List<Music_Data> music_data, Context context) {
        this.music_data = music_data;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.music_list,parent,false);
        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Music_Data  listData=music_data.get(position);
       holder.txtname.setText(listData.getName());
        // holder.txtmovie.setText(listData.getMoviename());

//        if (isMyServiceRunning(MusicPlayerServices.class)) {
//
//            Toast.makeText(context, "Service is running", Toast.LENGTH_SHORT).show();
//        } else {
//            Toast.makeText(context, "Service is Not Running", Toast.LENGTH_SHORT).show();
//        }



        //Glide.with(context).load(listData.getImageUrl()).into(holder.imageView);
        Glide.with(context)
                .load(listData.getImageUrl())
                .error(R.drawable.ic_youtube) //in case of error this is displayed
                .into(holder.imageView);
        holder.relative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

              /*  Intent intent =  new Intent(context,MusicPlayer.class);
                intent.putExtra("playlist",listData.getPlaylist());
                context.startActivity(intent);*/

                Intent stopintent = new Intent(context, myPlayService.class);
                context.stopService(stopintent);

            }


        });


    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) context. getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int getItemCount() {
        return  music_data.size();
    }

    public  class  ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtname,txtmovie,linear;
        //        private RelativeLayout relative;
        RelativeLayout relative;
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
       imageView =  (ImageView) itemView.findViewById(R.id.imageView);

            relative =  itemView.findViewById(R.id.relative);

            txtname=(TextView)itemView.findViewById(R.id.txt_name);
            //   txtmovie=(TextView)itemView.findViewById(R.id.txt_moviename);
            // linear=(RelativeLayout) itemView.findViewById(R.id.linear);





        }
    }

}

