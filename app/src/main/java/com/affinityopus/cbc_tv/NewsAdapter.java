package com.affinityopus.cbc_tv;


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

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

    private List<NewsData>newsData;
    Context context;

    SharedPreferences pref;



    public NewsAdapter(List<NewsData> newsData, Context context) {
        this.newsData = newsData;
        this.context = context;
    }
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.news_list,parent,false);
        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final NewsData  listData=newsData.get(position);
        holder.txtname.setText(listData.getTitle());
        // holder.txtmovie.setText(listData.getMoviename());


        //Glide.with(context).load(listData.getImageUrl()).into(holder.imageView);
        Glide.with(context)
                .load(listData.getImageUrl())
                .error(R.drawable.ic_youtube) //in case of error this is displayed
                .into(holder.imageView);
        holder.relative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context,NewsView.class);
              intent.putExtra("playlist",listData.getWebpath());
                context.startActivity(intent);

//                Intent stopintent = new Intent(context, myPlayService.class);
//                context.stopService(stopintent);

            }


        });


    }

    @Override
    public int getItemCount() {
        return  newsData.size();
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

