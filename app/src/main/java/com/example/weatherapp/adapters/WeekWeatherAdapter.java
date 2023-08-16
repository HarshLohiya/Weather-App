package com.example.weatherapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weatherapp.R;
import com.example.weatherapp.models.WeekWeatherModel;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class WeekWeatherAdapter extends RecyclerView.Adapter<WeekWeatherAdapter.ViewHolder> {
    private Context context;
    private ArrayList<WeekWeatherModel> weekWeatherModelArrayList;

    public WeekWeatherAdapter(Context context, ArrayList<WeekWeatherModel> weekWeatherModelArrayList){
        this.context = context;
        this.weekWeatherModelArrayList = weekWeatherModelArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.week_weather_adapter,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        WeekWeatherModel model = weekWeatherModelArrayList.get(position);
        holder.maxTempTextView.setText(model.getMaxTemp()+"°C");
        holder.minTempTextView.setText(model.getMinTemp()+"°C");
        holder.conditionTextView.setText(model.getCondition());
        Picasso.get().load("https:".concat(model.getIcon())).into(holder.iconImageView);
        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat output = new SimpleDateFormat("dd MMM");
        try{
            Date t= input.parse(model.getDate());
            holder.dateTextView.setText(output.format(t));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return weekWeatherModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView dateTextView, maxTempTextView, minTempTextView, conditionTextView;
        ImageView iconImageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            iconImageView = itemView.findViewById(R.id.weatherImageView);
            conditionTextView = itemView.findViewById(R.id.weatherTextView);
            maxTempTextView = itemView.findViewById(R.id.maxTempTextView);
            minTempTextView = itemView.findViewById(R.id.minTempTextView);
        }
    }
}
