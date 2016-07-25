package com.li.fabrice.brefweather.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Fabrice on 19/02/2016.
 */
public class Item implements JsonPopulate {
    private Condition condition;
    private JSONArray dayJSONArray;

    public JSONArray getDayJSONArray() {
        return dayJSONArray;
    }

    public Condition getCondition() {
        return condition;
    }

    @Override
    public void populate(JSONObject data) {
        condition = new Condition();
        dayJSONArray = new JSONArray();
        condition.populate(data.optJSONObject("condition"));
        dayJSONArray = data.optJSONArray("forecast");

    }

    @Override
    public JSONObject toJSON() {
        JSONObject data = new JSONObject();
        try {
            data.put("condition", condition.toJSON());
            data.put("forecast",dayJSONArray);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return data;
    }
}
