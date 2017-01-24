package com.tolya.weather.forecast;

import android.app.Service;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.zetterstrom.com.forecast.ForecastClient;
import android.zetterstrom.com.forecast.ForecastConfiguration;

import com.tolya.weather.forecast.adapters.RecycleViewWeatherAdapter;
import com.tolya.weather.forecast.adapters.decoration.VerticalSpaceItemDecoration;
import com.tolya.weather.forecast.models.HourlyWeather;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Tolik on 23.01.2017.
 */

public class MainActivity extends AppCompatActivity {
    private static final String API_KEY = "1fd6201a7cd89b9faf2d40462cd4da84";

    private static final int VERTICAL_ITEM_SPACE = 70;
    private static final int VERTICAL_SPACE_FROM_TOP = 70;

    @BindView(R.id.cities_recycle_view)
    RecyclerView citiesRecycleView;

    RecycleViewWeatherAdapter weatherAdapter;
    private Unbinder unbinder;
    private Worker worker = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        unbinder = ButterKnife.bind(this);

        ForecastConfiguration configuration =
                new ForecastConfiguration.Builder(API_KEY)
                        .setCacheDirectory(getCacheDir())
                        .build();
        ForecastClient.create(configuration);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        citiesRecycleView.setLayoutManager(linearLayoutManager);
        citiesRecycleView.addItemDecoration(new VerticalSpaceItemDecoration(VERTICAL_ITEM_SPACE, VERTICAL_SPACE_FROM_TOP));
    }

    @Override
    protected void onResume() {
        super.onResume();

        weatherAdapter = new RecycleViewWeatherAdapter(this);
        citiesRecycleView.setAdapter(weatherAdapter);

        final SweetAlertDialog progressBar = AlertManager.showSweetProgressBar(this, getResString(R.string.loading));
        worker = new Worker(new Worker.CallBack() {
            @Override
            public void onSuccess(List<Map<String, List<HourlyWeather>>> result) {
                MainActivity.this.onSuccess(result);
                progressBar.dismiss();
                AlertManager.showSweetSuccessDialog(MainActivity.this, getResString(R.string.success), getResString(R.string.you_see));
            }

            @Override
            public void onError(Throwable tw) {
                MainActivity.this.onError(tw);
            }
        });

        if(checkWifiOrMobileInternet(this)){
            progressBar.show();
            worker.doWork();
        }else {
            AlertManager.showSweetWarning(this, getResString(R.string.internet_problem), getResString(R.string.please_connect));
        }
    }

    private void onSuccess(List<Map<String, List<HourlyWeather>>> results){
        weatherAdapter.setResultsCollection(results);
    }

    private void onError(Throwable tw) {
        AlertManager.showToast(this, getResString(R.string.wrong));
    }

    private boolean checkWifiOrMobileInternet(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Service.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobile = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        return wifi.isConnected() || mobile.isConnected();
    }

    private String getResString(int strRes){
        return getResources().getString(strRes);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(unbinder != null){
            unbinder = null;
        }
    }
}
