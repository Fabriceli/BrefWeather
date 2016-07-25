package com.li.fabrice.brefweather.model;

/**
 * Created by Fabrice on 20/02/2016.
 */
public class ForecastDay {
    private int code;
    private String day;
    private String high;
    private String low;
    private String text;

    public ForecastDay(int code,String day,String high,String low,String text){
        setCode(code);
        setDay(day);
        setHigh(high);
        setLow(low);
        setText(text);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getHigh() {
        return high;
    }

    public void setHigh(String high) {
        this.high = high;
    }

    public String getLow() {
        return low;
    }

    public void setLow(String low) {
        this.low = low;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
