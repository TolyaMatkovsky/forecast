package com.tolya.weather.forecast.models;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Tolik on 24.01.2017.
 */

public class HourlyWeather {
    private Date date;
    private int temperature;

    public HourlyWeather(Date date, int temperature) {
        this.date = date;
        this.temperature = temperature;
    }

    public Date getDate() {
        return date;
    }

    public int getTemperature() {
        return temperature;
    }

    public int getDay(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("d");
        return Integer.parseInt(dateFormat.format(date));
    }

    public int getHour(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("H");
        return Integer.parseInt(dateFormat.format(date));
    }

    public String getSpecificHour(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        return dateFormat.format(date);
    }
}
