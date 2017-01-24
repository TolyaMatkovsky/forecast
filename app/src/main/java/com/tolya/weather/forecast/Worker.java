package com.tolya.weather.forecast;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.zetterstrom.com.forecast.ForecastClient;
import android.zetterstrom.com.forecast.models.DataPoint;
import android.zetterstrom.com.forecast.models.Forecast;

import com.tolya.weather.forecast.models.HourlyWeather;
import com.tolya.weather.forecast.models.Location;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Tolik on 24.01.2017.
 */

public class Worker {
    private static final String TOKYO = "Tokyo";
    private static final String KIEV = "Kiev";
    private static final String LONDON = "London";
    private static final String NEW_YORK = "New York";

    private Map<String, Location> citiesLocations;
    private CallBack callBack;
    private List<Map<String, List<HourlyWeather>>> result;
    private int threadCount = 1;

    public Worker(CallBack callBack) {
        this.callBack = callBack;
        this.result = new ArrayList<>();
        this.citiesLocations = new HashMap<>();
        citiesLocations.put(TOKYO, new Location(35.652832, 139.839478));
        citiesLocations.put(KIEV, new Location(50.448853, 30.513346));
        citiesLocations.put(LONDON, new Location(51.508530, -0.076132));
        citiesLocations.put(NEW_YORK, new Location(40.792240, -73.138260));
    }

    public void doWork(){
        new ForecastThread(citiesLocations.get(TOKYO), TOKYO);
        new ForecastThread(citiesLocations.get(KIEV), KIEV);
        new ForecastThread(citiesLocations.get(LONDON), LONDON);
        new ForecastThread(citiesLocations.get(NEW_YORK), NEW_YORK);
    }

    private synchronized void addToResult(Map<String, List<HourlyWeather>> map){
        result.add(map);
        if(threadCount == citiesLocations.size()){
            Handler uiHandler = new Handler(Looper.getMainLooper());
            uiHandler.post(new Runnable() {
                @Override
                public void run() {
                    callBack.onSuccess(result);
                }
            });
            return;
        }
        threadCount++;
    }

    public interface CallBack{
        void onSuccess(List<Map<String, List<HourlyWeather>>> result);
        void onError(Throwable tw);
    }

    private class ForecastThread extends Thread {
        private String keyName;
        private Location location;

        ForecastThread(Location location, String keyName){
            super();
            this.keyName = keyName;
            this.location = location;
            new Thread(this).start();
        }

        @Override
        public void run() {
            super.run();

            ForecastClient.getInstance()
                    .getForecast(location.getLatitude(), location.getLongitude(), new Callback<Forecast>() {
                        @Override
                        public void onResponse(Call<Forecast> forecastCall, Response<Forecast> response) {
                            if (response.isSuccessful()) {
                                Forecast forecast = response.body();

                                List<DataPoint> list = forecast.getHourly().getDataPoints();

                                List<HourlyWeather> allData = new ArrayList<HourlyWeather>();

                                for(DataPoint d : list){
                                    HourlyWeather hourlyWeather = new HourlyWeather(d.getTime(), convertToCelcius(d.getTemperature()));
                                    allData.add(hourlyWeather);
                                }
                                Map<String, List<HourlyWeather>> map = new HashMap<String, List<HourlyWeather>>();
                                map.put(keyName, getCorrectDataFromAll(allData));

                                addToResult(map);
                            }
                        }

                        @Override
                        public void onFailure(Call<Forecast> forecastCall, final Throwable t) {
                            Handler uiHandler = new Handler(Looper.getMainLooper());
                            uiHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    callBack.onError(t);
                                }
                            });
                        }
                    });
        }
    }

    private int convertToCelcius(double faranh){
        double celcius = (5.0 / 9.0) * (faranh - 32);
        return (int) Math.round(celcius);
    }

    private List<HourlyWeather> getCorrectDataFromAll(List<HourlyWeather> allData){
        final int HOUR_BORDER = 23;
        SimpleDateFormat dayFormat = new SimpleDateFormat("d");
        SimpleDateFormat hourFormat = new SimpleDateFormat("H");

        Date currentDate = new Date(System.currentTimeMillis());
        final int DAY = Integer.parseInt(dayFormat.format(currentDate)) + 1;
        final int HOUR = Integer.parseInt(hourFormat.format(currentDate));

        List<HourlyWeather> correctData = new ArrayList<>();

        for (HourlyWeather w: allData){
            if(w.getDay() == DAY){
//                int wHour = w.getHour();
//                if(wHour >= HOUR && wHour <= HOUR_BORDER){
                correctData.add(w);
//                }
            }
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("MM.dd 'at' HH:mm");
        for (HourlyWeather w: correctData){
            Log.e("HOUR", dateFormat.format(w.getDate())+"\n");
            Log.e("TEMP", String.valueOf(w.getTemperature())+"\n");
            Log.e("enter", "\n");
        }

        return correctData;
    }
}
