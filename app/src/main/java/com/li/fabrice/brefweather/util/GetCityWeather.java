package com.li.fabrice.brefweather.util;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.li.fabrice.brefweather.service.WeatherServiceListener;
import com.li.fabrice.brefweather.model.Channel;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Fabrice on 19/02/2016.
 */
public class GetCityWeather {
    public static Exception error;
    private WeatherServiceListener listener;

    public void refreshWeather(String woeid, String unit) {
        final String getWeatherurl = makeGetCityWeatherURL(woeid,unit);
        new AsyncTask<Void, Void, Channel>() {
            @Override
            protected Channel doInBackground(Void... arg0) {

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
                        error = new LocationWeatherException("No weather information found for ");
                        return null;
                    }

                    JSONObject channelJSON = queryResults.optJSONObject("results").optJSONObject("channel");
                    channel.populate(channelJSON);

                    return channel;

                } catch (Exception e) {
                    error = e;
                }

                return null;
            }

            @Override
            protected void onPostExecute(Channel channel) {

                if (channel == null && error != null) {
                    listener.serviceFailure(error);
                } else {
                    listener.serviceSuccess(channel);
                }

            }

        }.execute();
    }

    public GetCityWeather(WeatherServiceListener listener){
        this.listener = listener;
    }

    /**
     *
     * URL pour cityname
     *
     * */
    private static String makeGetCityWeatherURL(String woeid, String unit) {
        String YQL = String.format("select * from weather.forecast where woeid=%s and u='%s'",woeid,unit);
        //String endpoint = String.format("https://query.yahooapis.com/v1/public/yql?q=%s&format=json", Uri.encode(YQL));
        return String.format("https://query.yahooapis.com/v1/public/yql?q=%s&format=json", Uri.encode(YQL));
    }

    public static class LocationWeatherException extends Exception {
        public LocationWeatherException(String detailMessage) {
            super(detailMessage);
        }
    }

}
