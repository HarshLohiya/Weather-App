package com.example.weatherapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.weatherapp.adapters.WeekWeatherAdapter;
import com.example.weatherapp.models.WeekWeatherModel;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class WeekFragment extends Fragment {
    String cityName;
    TextView tomTempTextView, tomConditionTextView, rainTextView, windTextView, humidityTextView;
    ImageView tomConditionImageView, backImage;
    ArrayList<WeekWeatherModel> weekWeatherModelArrayList;
    WeekWeatherAdapter weekWeatherAdapter;
    RecyclerView weeklyRecyclerView;

    MainActivity mainActivity;
    String city;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_week, container, false);

        tomTempTextView = view.findViewById(R.id.tomTempTextView);
        backImage = view.findViewById(R.id.backImage);
        tomConditionTextView = view.findViewById(R.id.tomConditionTextView);
        rainTextView = view.findViewById(R.id.rainTextView);
        windTextView = view.findViewById(R.id.windTextView);
        humidityTextView = view.findViewById(R.id.humidityTextView);
        tomConditionImageView = view.findViewById(R.id.tomConditionImageView);
        weeklyRecyclerView = view.findViewById(R.id.weeklyRecyclerView);

        weekWeatherModelArrayList = new ArrayList<>();
        weeklyRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        weekWeatherAdapter = new WeekWeatherAdapter(getContext(), weekWeatherModelArrayList);
        weeklyRecyclerView.setAdapter(weekWeatherAdapter);

        mainActivity = (MainActivity) getActivity();
        city = mainActivity.cityName;
        getWeather(city);
        return view;
    }

    public void getWeather(String cityName) {
        String url = "https://api.weatherapi.com/v1/forecast.json?key=5b898ac69b864f1090c62413233107&q=" + cityName + "&days=8&aqi=yes&alerts=yes";

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                weekWeatherModelArrayList.clear();

                try {
                    int isDay = response.getJSONObject("current").getInt("is_day");
                    if (isDay == 1) {
                        Picasso.get().load("https://images.pond5.com/no-clouds-blue-sky-footage-101776792_iconl.jpeg").into(backImage);
                    } else {
                        Picasso.get().load("https://patch.com/img/cdn20/users/22910698/20221116/013158/night-sky-stars-002___16133152419.jpg").into(backImage);
//                        cityInputTextView.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));
                    }

                    JSONArray weekJsonArray = response.getJSONObject("forecast").getJSONArray("forecastday");
                    JSONObject tomDetailJsonObj = weekJsonArray.getJSONObject(1).getJSONObject("day");
                    tomTempTextView.setText(Math.round(tomDetailJsonObj.getDouble("avgtemp_c"))+"°C");
                    String tomCondition = tomDetailJsonObj.getJSONObject("condition").getString("text");
                    if (tomCondition.equals("Patchy rain possible"))
                        tomCondition = "Scattered rain";
                    tomConditionTextView.setText(tomCondition);
                    rainTextView.setText(Math.round(tomDetailJsonObj.getDouble("daily_chance_of_rain"))+"%");
                    windTextView.setText(Math.round(tomDetailJsonObj.getDouble("maxwind_kph"))+" kph");
                    humidityTextView.setText(Math.round(tomDetailJsonObj.getDouble("avghumidity"))+"%");

                    for (int i = 1; i <= 2; i++) {
                        JSONObject weekDetailJsonObj = weekJsonArray.getJSONObject(i).getJSONObject("day");
                        String date = weekJsonArray.getJSONObject(i).getString("date");
                        int maxTemp = (int) Math.round(weekDetailJsonObj.getDouble("maxtemp_c"));
                        int minTemp = (int) Math.round(weekDetailJsonObj.getDouble("mintemp_c"));
                        String icon = weekDetailJsonObj.getJSONObject("condition").getString("icon");
                        String condition = weekDetailJsonObj.getJSONObject("condition").getString("text");
                        if (condition.equals("Patchy rain possible"))
                            condition = "Scattered rain";
                        weekWeatherModelArrayList.add(new WeekWeatherModel(date, maxTemp, minTemp, icon, condition));
//                        Log.i("date",weekWeatherModelArrayList.get(i-1).getCondition());
                    }
                    weekWeatherAdapter.notifyDataSetChanged();
//                    Toast.makeText(WeekWeather.this, weekWeatherModelArrayList.size()+"", Toast.LENGTH_SHORT).show();
//                    Log.i("Size", weekWeatherModelArrayList.size() + "");
//       ##             Picasso.get().load("https:".concat(icon)).into(weatherImageView);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Log.i("tag3", "Error :" + error.toString());
                Toast.makeText(getContext(), "Please enter a valid City Name", Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(jsonObjectRequest);
    }
}