package com.li.fabrice.brefweather.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Fabrice on 19/02/2016.
 * "humidity":"87",
 "pressure":"1015.92",
 "rising":"0",
 "visibility":"9.99"
 */
public class Astronomy implements JsonPopulate {
    private String sunrise;
    private String sunset;

    public String getSunrise() {
        return sunrise;
    }

    public String getSunset() {
        return sunset;
    }

    @Override
    public void populate(JSONObject data) {
        sunrise = data.optString("sunrise");
        sunset = data.optString("sunset");
    }

    @Override
    public JSONObject toJSON() {
        JSONObject data = new JSONObject();

        try {
            data.put("sunrise", sunrise);
            data.put("sunset", sunset);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return data;
    }
}
