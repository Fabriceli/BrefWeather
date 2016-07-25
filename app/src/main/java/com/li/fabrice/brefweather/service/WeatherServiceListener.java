package com.li.fabrice.brefweather.service;

import com.li.fabrice.brefweather.model.Channel;

/**
 * Created by Fabrice on 19/02/2016.
 */
public interface WeatherServiceListener {
    void serviceSuccess(Channel channel);

    void serviceFailure(Exception exception);
}
