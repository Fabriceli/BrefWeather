package com.li.fabrice.brefweather.model;

import org.json.JSONObject;

/**
 * Created by Fabrice on 19/02/2016.
 */
public interface JsonPopulate {

    void populate(JSONObject data);

    JSONObject toJSON();
}
