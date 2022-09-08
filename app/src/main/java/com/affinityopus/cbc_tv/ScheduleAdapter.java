package com.affinityopus.cbc_tv;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class ScheduleAdapter  extends RecyclerView.Adapter<ScheduleAdapter.ViewHolder> {

    boolean isShimmer = true;
    int shimmerNumber = 15;
    private List<ScheduleData> scheduleData;
    Context context;

    SharedPreferences pref;



    public ScheduleAdapter(List<ScheduleData> scheduleData, Context context) {
        this.scheduleData = scheduleData;
        this.context = context;
    }


    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.schedule_list,parent,false);
        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final ScheduleData  listData=scheduleData.get(position);
//        holder.txtname.setText(listData.getTitle());
//     holder.yearMonth.setText(listData.getYear() +"/" +  listData.getMonth());
//       holder.date.setText(listData.getDay() + " / "+ listData.getDate());
//     holder.time.setText(listData.getTime());
        // holder.txtmovie.setText(listData.getMoviename());


        Glide.with(context).load(listData.getImageUrl()).fitCenter()
                .error(R.drawable.ic_youtube)
                .into(holder.imageView);
//        Glide.with(context)
//                .load(listData.getImageUrl())
//                .error(R.drawable.ic_youtube) //in case of error this is displayed
//                .into(holder.imageView);
//        holder.relative.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                Intent intent = new Intent(context,NewsView.class);
//                intent.putExtra("playlist",listData.getWebpath());
//                context.startActivity(intent);
//
//            }
//
//
//        });


    }

    @Override
    public int getItemCount() {
        return  scheduleData.size();
    }

    public  class  ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView =  (ImageView) itemView.findViewById(R.id.imageView);

//            relative =  itemView.findViewById(R.id.relative);
//
//            txtname=(TextView)itemView.findViewById(R.id.title);
//           yearMonth = (TextView) itemView.findViewById(R.id.yearMonth);
//           date = (TextView) itemView.findViewById(R.id.date);
//          time = (TextView) itemView.findViewById(R.id.time);
            //   txtmovie=(TextView)itemView.findViewById(R.id.txt_moviename);
            // linear=(RelativeLayout) itemView.findViewById(R.id.linear);





        }
    }

}

