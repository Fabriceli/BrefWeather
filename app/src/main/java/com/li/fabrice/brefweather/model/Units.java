package com.li.fabrice.brefweather.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Fabrice on 19/02/2016.
 */
public class Units implements JsonPopulate {
    private String distance;
    private String pressure;
    private String speed;
    private String temperature;

    public String getDistance() {
        return distance;
    }

    public String getPressure() {
        return pressure;
    }

    public String getSpeed() {
        return speed;
    }

    public String getTemperature() {
        return temperature;
    }

    @Override
    public void populate(JSONObject data) {
        distance = data.optString("distance");
        pressure = data.optString("pressure");
        speed = data.optString("speed");
        temperature = data.optString("temperature");
    }

    @Override
    public JSONObject toJSON() {
        JSONObject data = new JSONObject();

        try {
            data.put("distance", distance);
            data.put("pressure", pressure);
            data.put("speed", speed);
            data.put("temperature", temperature);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return data;
    }
}
