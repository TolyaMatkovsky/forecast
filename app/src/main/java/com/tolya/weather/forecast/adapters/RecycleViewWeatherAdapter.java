package com.tolya.weather.forecast.adapters;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tolya.weather.forecast.R;
import com.tolya.weather.forecast.adapters.decoration.HorizontalSpaceItemDecoration;
import com.tolya.weather.forecast.models.HourlyWeather;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Tolik on 24.01.2017.
 */

public class RecycleViewWeatherAdapter extends RecyclerView.Adapter<RecycleViewWeatherAdapter.CityWeatherViewHolder>{
    private static final int HORIZONTAL_OFFSET_LEFT = 40;
    private static final int HORIZONTAL_OFFSET_RIFHT = 40;

    private List<Map<String, List<HourlyWeather>>> results;
    private final LayoutInflater layoutInflater;
    private Context context;


    public RecycleViewWeatherAdapter(Context context) {
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.results = Collections.emptyList();
        this.context = context;
    }

    @Override
    public CityWeatherViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = this.layoutInflater.inflate(R.layout.main_item, parent, false);
        return new CityWeatherViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CityWeatherViewHolder holder, int position) {
        final Map<String, List<HourlyWeather>> weatherInCity = this.results.get(position);
        Set<String> citySet = new HashSet<>();
        holder.cityName.setText((weatherInCity.keySet().toArray(new String[1]))[0]);

        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        RecycleViewNestedWeatherAdapter nestedAdapter = new RecycleViewNestedWeatherAdapter(context);

        holder.nestedRecycleView.setLayoutManager(layoutManager);
        holder.nestedRecycleView.addItemDecoration(new HorizontalSpaceItemDecoration(HORIZONTAL_OFFSET_RIFHT, HORIZONTAL_OFFSET_LEFT));
        holder.nestedRecycleView.setAdapter(nestedAdapter);

        for (List<HourlyWeather> l: weatherInCity.values()){
            nestedAdapter.setResultsCollection(l);
            break;
        }
    }

    @Override
    public int getItemCount() {
        return (this.results != null) ? this.results.size() : 0;
    }

    public void setResultsCollection(Collection<Map<String, List<HourlyWeather>>> resultsCollection) {
        this.validateResultsCollection(resultsCollection);
        this.results = (List<Map<String, List<HourlyWeather>>>) resultsCollection;
        this.notifyDataSetChanged();
    }

    private void validateResultsCollection(Collection<Map<String, List<HourlyWeather>>> resultsCollection) {
        if (resultsCollection == null) {
            throw new IllegalArgumentException("The list cannot be null");
        }
    }

    static class CityWeatherViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.city_name)
        TextView cityName;
        @BindView(R.id.nested_recycle_view)
        RecyclerView nestedRecycleView;

        public CityWeatherViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
