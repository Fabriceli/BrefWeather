package com.li.fabrice.brefweather.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Fabrice on 19/02/2016.
 */
public class Location implements JsonPopulate {
    private String city;
    private String country;
    private String region;

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public String getRegion() {
        return region;
    }

    @Override
    public void populate(JSONObject data) {
        city = data.optString("city");
        country = data.optString("country");
        region = data.optString("region");
    }

    @Override
    public JSONObject toJSON() {
        JSONObject data = new JSONObject();

        try {
            data.put("city", city);
            data.put("country", country);
            data.put("region", region);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return data;
    }
}
