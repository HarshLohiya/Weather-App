package com.example.weatherapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.weatherapp.adapters.WeatherAdapter;
import com.example.weatherapp.models.WeatherModel;
import com.google.android.material.textfield.TextInputEditText;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class TodayFragment extends Fragment {
    ProgressBar progressBar;
    RelativeLayout homeRL;
    TextView cityNameTextView,timeTextView, maxTempTextView, tempTextView, feelTextView, weatherTextView, detailsTextView;
    TextInputEditText cityInputTextView;
    ImageView backImage, weatherImageView, inputImageView, voiceInputImageView;
    RecyclerView hourlyRecyclerView;
    ArrayList<WeatherModel> weatherModelArrayList;
    WeatherAdapter weatherAdapter;

    MainActivity mainActivity;
    String city;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_today, container, false);

        progressBar = view.findViewById(R.id.progressBar);
        homeRL = view.findViewById(R.id.homeRL);
        cityNameTextView = view.findViewById(R.id.cityNameTextView);
        timeTextView = view.findViewById(R.id.timeTextView);
        maxTempTextView = view.findViewById(R.id.maxTempTextView);
        tempTextView = view.findViewById(R.id.tempTextView);
        feelTextView = view.findViewById(R.id.feelTextView);
        weatherTextView = view.findViewById(R.id.weatherTextView);
        detailsTextView = view.findViewById(R.id.detailsTextView);
//        cityInputTextView = view.findViewById(R.id.cityInputTextView);
        backImage = view.findViewById(R.id.backImage);
        weatherImageView = view.findViewById(R.id.weatherImageView);
//        inputImageView = view.findViewById(R.id.inputImageView);
//        voiceInputImageView = view.findViewById(R.id.voiceInputImageView);
        hourlyRecyclerView = view.findViewById(R.id.hourlyRecyclerView);
        weatherModelArrayList = new ArrayList<>();
        weatherAdapter = new WeatherAdapter(getContext(),weatherModelArrayList);
        hourlyRecyclerView.setAdapter(weatherAdapter);

        mainActivity = (MainActivity) getActivity();
        city = mainActivity.cityName;
        getWeather(city);
        return view;
    }

    public void getWeather(String cityName) {
        String url = "https://api.weatherapi.com/v1/forecast.json?key=5b898ac69b864f1090c62413233107&q=" + cityName + "&days=2&aqi=yes&alerts=yes";

//        InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//        mgr.hideSoftInputFromWindow(cityInputTextView.getWindowToken(), 0);
//        cityInputTextView.setText("");

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressBar.setVisibility(View.GONE);
                homeRL.setVisibility(View.VISIBLE);
                cityNameTextView.setText(cityName);
                weatherModelArrayList.clear();

                try {
                    SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd hh:mm");
                    SimpleDateFormat output = new SimpleDateFormat("dd MMMM, hh:mm aa");
                    String date = response.getJSONObject("location").getString("localtime");
                    try {
                        Date t = input.parse(date);
                        timeTextView.setText(output.format(t));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    int maxTemp = (int) Math.round(response.getJSONObject("forecast").getJSONArray("forecastday").getJSONObject(0).getJSONObject("day").getDouble("maxtemp_c"));
                    int minTemp = (int) Math.round(response.getJSONObject("forecast").getJSONArray("forecastday").getJSONObject(0).getJSONObject("day").getDouble("mintemp_c"));
                    maxTempTextView.setText("Day " + maxTemp + "° ↑ •  Night " + minTemp + "° ↓");

                    int temp = (int) Math.round(response.getJSONObject("current").getDouble("temp_c"));
                    tempTextView.setText(temp + "°C");

                    int feelsLike = (int) Math.round(response.getJSONObject("current").getDouble("feelslike_c"));
                    feelTextView.setText("Feels like " + feelsLike + "°");

                    String icon = response.getJSONObject("current").getJSONObject("condition").getString("icon");
                    Picasso.get().load("https:".concat(icon)).into(weatherImageView);

                    String condition = response.getJSONObject("current").getJSONObject("condition").getString("text");
                    weatherTextView.setText(condition);

                    int isDay = response.getJSONObject("current").getInt("is_day");
                    if (isDay == 1) {
                        Picasso.get().load("https://images.pond5.com/no-clouds-blue-sky-footage-101776792_iconl.jpeg").into(backImage);
                    } else {
                        Picasso.get().load("https://patch.com/img/cdn20/users/22910698/20221116/013158/night-sky-stars-002___16133152419.jpg").into(backImage);
//                        cityInputTextView.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));
                    }
                    JSONObject forecastObj = response.getJSONObject("forecast");
                    JSONObject forecastP = forecastObj.getJSONArray("forecastday").getJSONObject(0);
                    JSONArray hourArray = forecastP.getJSONArray("hour");

                    String t1 = (date.substring(11, 13));
                    int t2;
                    if (t1.charAt(1) == ':') {
                        t2 = t1.charAt(0) - '0';
                    } else {
                        t2 = Integer.parseInt(t1);
                    }
                    for (int i = t2 + 1; i < hourArray.length(); i++) {
                        JSONObject hourObj = hourArray.getJSONObject(i);
                        String time = hourObj.getString("time");
                        int tempC = (int) Math.round(hourObj.getDouble("temp_c"));
                        String img = hourObj.getJSONObject("condition").getString("icon");
                        int wind = (int) Math.round(hourObj.getDouble("wind_kph"));
                        weatherModelArrayList.add(new WeatherModel(time, tempC, img, wind));
                    }

                    forecastP = forecastObj.getJSONArray("forecastday").getJSONObject(1);
                    hourArray = forecastP.getJSONArray("hour");
                    for (int i = 0; i < t2 + 1; i++) {
                        JSONObject hourObj = hourArray.getJSONObject(i);
                        String time = hourObj.getString("time");
                        int tempC = (int) Math.round(hourObj.getDouble("temp_c"));
                        String img = hourObj.getJSONObject("condition").getString("icon");
                        int wind = (int) Math.round(hourObj.getDouble("wind_kph"));
                        weatherModelArrayList.add(new WeatherModel(time, tempC, img, wind));
                    }
                    weatherAdapter.notifyDataSetChanged();

                    String humidity = response.getJSONObject("current").getString("humidity");
                    int pressure = (int) Math.round(response.getJSONObject("current").getDouble("pressure_mb"));
                    int uv = (int) Math.round(response.getJSONObject("current").getDouble("uv"));
                    String uvindex;
                    if (uv <= 3)
                        uvindex = "Low";
                    else if (uv <= 6)
                        uvindex = "Moderate";
                    else
                        uvindex = "High";
                    int visibility = (int) Math.round(response.getJSONObject("current").getDouble("vis_km"));
                    detailsTextView.setText("Humidity \t\t\t\t\t\t\t\t " + humidity + "% \nPressure \t\t\t\t\t\t\t\t " + pressure + " mBar \nUV index \t\t\t\t\t\t\t\t " + uvindex + ", " + uv + " \nVisibility \t\t\t\t\t\t\t\t\t " + visibility + " km");

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