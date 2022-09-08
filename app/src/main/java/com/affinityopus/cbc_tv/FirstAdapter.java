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

public class FirstAdapter extends RecyclerView.Adapter<FirstAdapter.ViewHolder> {

    private List<FirstData>firstData;
    Context context;

    SharedPreferences pref;

    public FirstAdapter(List<FirstData> firstData, Context context) {
        this.firstData = firstData;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.front_list,parent,false);
        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final FirstData  listData=firstData.get(position);
        holder.txtname.setText(listData.getId());

        Glide.with(context)
                .load(listData.getImageUrl())
                .error(R.drawable.ic_youtube) //in case of error this is displayed
                .into(holder.imageView);
        holder.relative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pref = context.getSharedPreferences("user",context.MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("getID",listData.getId());
                editor.putString("getImages",listData.getImageUrl());
                editor.commit();
                context.startActivity(new Intent(context, MainActivity.class));
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
        return  firstData.size();
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

