package com.li.fabrice.brefweather.model;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by Fabrice on 19/02/2016.
 */
public class Condition implements JsonPopulate {
    private int code;
    private int temperature;
    private String description;
    private String date;

    public int getCode() {
        return code;
    }

    public int getTemperature() {
        return temperature;
    }

    public String getDescription() {
        return description;
    }

    public String getDate() {
        return date;
    }

    @Override
    public void populate(JSONObject data) {
        code = data.optInt("code");
        temperature = data.optInt("temp");
        description = data.optString("text");
        date = data.optString("date");
    }

    @Override
    public JSONObject toJSON() {
        JSONObject data = new JSONObject();

        try {
            data.put("code", code);
            data.put("temp", temperature);
            data.put("text", description);
            data.put("date", date);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return data;
    }
}
