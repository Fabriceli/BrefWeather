package com.li.fabrice.brefweather.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Fabrice on 19/02/2016.
 */
public class Wind implements JsonPopulate {
    private String chill;
    private String direction;
    private String speed;

    public String getChill() {
        return chill;
    }

    public String getDirection() {
        return direction;
    }

    public String getSpeed() {
        return speed;
    }

    @Override
    public void populate(JSONObject data) {
        chill = data.optString("chill");
        direction = data.optString("direction");
        speed = data.optString("speed");
    }

    @Override
    public JSONObject toJSON() {
        JSONObject data = new JSONObject();

        try {
            data.put("chill", chill);
            data.put("direction", direction);
            data.put("speed", speed);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return data;
    }
}
