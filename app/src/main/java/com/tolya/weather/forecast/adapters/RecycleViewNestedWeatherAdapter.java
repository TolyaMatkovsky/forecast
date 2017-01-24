package com.tolya.weather.forecast.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tolya.weather.forecast.R;
import com.tolya.weather.forecast.models.HourlyWeather;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Tolik on 24.01.2017.
 */

public class RecycleViewNestedWeatherAdapter extends RecyclerView.Adapter<RecycleViewNestedWeatherAdapter.WeatherViewHolder>{
    private List<HourlyWeather> weatherResults;
    private final LayoutInflater layoutInflater;


    public RecycleViewNestedWeatherAdapter(Context context) {
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.weatherResults = Collections.emptyList();
    }

    @Override
    public WeatherViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = this.layoutInflater.inflate(R.layout.nested_item, parent, false);
        return new WeatherViewHolder(view);
    }

    @Override
    public void onBindViewHolder(WeatherViewHolder holder, int position) {
        final HourlyWeather weather = this.weatherResults.get(position);
        holder.hour.setText(weather.getSpecificHour());
        if(weather.getTemperature() > 0){
            holder.temperature.setText("+"+String.valueOf(weather.getTemperature()));
        }else {
            holder.temperature.setText(String.valueOf(weather.getTemperature()));
        }
    }

    @Override
    public int getItemCount() {
        return (this.weatherResults != null) ? this.weatherResults.size() : 0;
    }

    public void setResultsCollection(Collection<HourlyWeather> resultsCollection) {
        this.validateResultsCollection(resultsCollection);
        this.weatherResults = (List<HourlyWeather>) resultsCollection;
        this.notifyDataSetChanged();
    }

    private void validateResultsCollection(Collection<HourlyWeather> resultsCollection) {
        if (resultsCollection == null) {
            throw new IllegalArgumentException("The list cannot be null");
        }
    }

    static class WeatherViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.hour)
        TextView hour;
        @BindView(R.id.temp)
        TextView temperature;

        public WeatherViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
