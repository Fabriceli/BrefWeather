package com.li.fabrice.brefweather.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Fabrice on 19/02/2016.
 */
public class Channel implements JsonPopulate {
    private Location location;
    private Units units;
    private Wind wind;
    private Atmosphere atmosphere;
    private Astronomy astronomy;
    private Item item;

    public Location getLocation() {
        return location;
    }

    public Units getUnits() {
        return units;
    }

    public Wind getWind() {
        return wind;
    }

    public Atmosphere getAtmosphere() {
        return atmosphere;
    }

    public Astronomy getAstronomy() {
        return astronomy;
    }

    public Item getItem() {
        return item;
    }

    @Override
    public void populate(JSONObject data) {
        units = new Units();
        units.populate(data.optJSONObject("units"));

        item = new Item();
        item.populate(data.optJSONObject("item"));

        location = new Location();
        location.populate(data.optJSONObject("location"));

        wind = new Wind();
        wind.populate(data.optJSONObject("wind"));

        atmosphere = new Atmosphere();
        atmosphere.populate(data.optJSONObject("atmosphere"));

        astronomy = new Astronomy();
        astronomy.populate(data.optJSONObject("astronomy"));
    }

    @Override
    public JSONObject toJSON() {
        JSONObject data = new JSONObject();

        try {
            data.put("units", units.toJSON());
            data.put("item", item.toJSON());
            data.put("location", location.toJSON());
            data.put("wind", wind.toJSON());
            data.put("atmosphere", atmosphere.toJSON());
            data.put("astronomy", astronomy.toJSON());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return data;
    }
}
