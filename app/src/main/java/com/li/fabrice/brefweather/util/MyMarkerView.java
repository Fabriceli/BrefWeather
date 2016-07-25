package com.li.fabrice.brefweather.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.li.fabrice.brefweather.R;

import java.util.ArrayList;

/**
 * Created by Fabrice on 28/01/2016.
 */
public class MyMarkerView extends MarkerView {

    private TextView tvContent,tvContentJour;
    private ArrayList<String> xValuesArray;
    private SharedPreferences prefs;

    public MyMarkerView(Context context, int layoutResource) {
        super(context, layoutResource);

        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        tvContent = (TextView) findViewById(R.id.tvContent);
        tvContentJour = (TextView) findViewById(R.id.tvContentJour);
    }


    public void setxValuesArray(ArrayList<String> xValuesArray) {
        this.xValuesArray = xValuesArray;
    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {

        String xValue = xValuesArray.get(e.getXIndex());
        //Log.e("getXVals(): ", xValue);
        String s;
        s = prefs.getString("unit",null);
        if(s != null){
            if(s.equals("c")){
                tvContent.setText(String.format("%s°C", (int)e.getVal()));
            }else{
                tvContent.setText(String.format("%s°F", (int)e.getVal()));
            }
        }else {
            tvContent.setText(String.format("%s°C", (int)e.getVal()));
        }

        tvContentJour.setText(xValue);

    }

    @Override
    public int getXOffset(float xpos) {
        // this will center the marker-view horizontally
        return -(getWidth() / 2);
    }

    @Override
    public int getYOffset(float ypos) {
        // this will cause the marker-view to be above the selected value
        return -getHeight();
    }
}
