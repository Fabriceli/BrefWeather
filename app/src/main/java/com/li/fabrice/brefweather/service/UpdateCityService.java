package com.li.fabrice.brefweather.service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

import com.li.fabrice.brefweather.model.Channel;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class UpdateCityService extends Service {
    private boolean running = false;
    private Callback callback = null;
    private SharedPreferences prefs;
    private int UPDATE_INTERVAL ;//更新时间间隔 2h

    public UpdateCityService() {
    }

    @Override
    public void onCreate() {
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        UPDATE_INTERVAL = 0;
        running = true;
        super.onCreate();
        new Thread(){
            @Override
            public void run() {
                super.run();
                while (running){
                    try {
                        UPDATE_INTERVAL = prefs.getInt("inverval",2);
                        sleep(UPDATE_INTERVAL*60*60*1000);
                        Log.e("UPDATE_INTERVAL",UPDATE_INTERVAL+"!!!!");
                        updateWeather();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        }.start();
    }


    @Override
    public void onDestroy() {
        running = false;
        super.onDestroy();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }


    private void updateWeather() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String woeid = preferences.getString("woeid", null);

        Log.e("woeid",woeid);

        if (woeid != null) {
            String s,unit;
            s = preferences.getString("unit",null);
            if(s != null){
                if(s.equals("c")){
                    unit = "c";
                    Weather(woeid, unit);
                }else{
                    unit = "f";
                    Weather(woeid, unit);
                }
            }else {
                unit = "c";
                Weather(woeid, unit);
            }

        }
    }

    private void Weather(String woeid, String unit) {
        final String getWeatherurl = makeGetCityWeatherURL(woeid, unit);
        Channel channel = new Channel();

        try {
            URL url = new URL(getWeatherurl);

            URLConnection connection = url.openConnection();
            connection.setUseCaches(false);

            InputStream inputStream = connection.getInputStream();

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder result = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }

            JSONObject data = new JSONObject(result.toString());

            JSONObject queryResults = data.optJSONObject("query");
            Log.e("queryResults", String.valueOf(queryResults));
            int count = queryResults.optInt("count");

            if (count == 0) {
                //error
            }

            JSONObject channelJSON = queryResults.optJSONObject("results").optJSONObject("channel");
            channel.populate(channelJSON);
            //handleWeather(channel);
            callback.DataChange(channel);

        } catch (Exception e) {
            //error
            e.printStackTrace();
        }
    }

    private String makeGetCityWeatherURL(String woeid, String unit) {
        String YQL = String.format("select * from weather.forecast where woeid=%s and u='%s'", woeid, unit);
        return String.format("https://query.yahooapis.com/v1/public/yql?q=%s&format=json", Uri.encode(YQL));
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return new Binder();
    }

    public class Binder extends android.os.Binder{
        public UpdateCityService getService(){
            return UpdateCityService.this;
        }
    }

    public Callback getCallback() {
        return callback;
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }


    public static interface Callback{
        void DataChange(Channel result);
    }

}
