package com.li.fabrice.brefweather.util;

import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.formatter.YAxisValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;

/**
 * Created by Fabrice on 22/11/2015.
 */
public class MyValueFormatter implements ValueFormatter, YAxisValueFormatter {

    private DecimalFormat mFormat;
    String label;

    public MyValueFormatter(String label) {
        this.label = label;
        mFormat = new DecimalFormat("###,###,##0.0"); // use one decimal
    }

    @Override
    public String getFormattedValue(float v, Entry entry, int i, ViewPortHandler viewPortHandler) {
        return null;
    }

    @Override
    public String getFormattedValue(float v, YAxis yAxis) {
        if(label != null){
            if(label.equals("T")){
                return mFormat.format(v) + " °C"; //
            }else {
                return mFormat.format(v) + " °F"; //
            }
        }

        return mFormat.format(v) + " °C";//default

    }
}
