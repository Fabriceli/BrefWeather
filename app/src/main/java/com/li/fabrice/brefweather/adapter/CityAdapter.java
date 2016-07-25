package com.li.fabrice.brefweather.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filterable;
import android.widget.TextView;

import com.li.fabrice.brefweather.R;
import com.li.fabrice.brefweather.model.CityResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fabrice on 15/02/2016.
 */
public class CityAdapter extends ArrayAdapter<CityResult> implements Filterable {
    private Context context;
    private List<CityResult> cityList = new ArrayList<>();

    public CityAdapter(Context context, List<CityResult> cityList) {
        super(context, R.layout.citylist_layout, cityList);
        this.cityList = cityList;
        this.context = context;
    }

    @Override
    public long getItemId(int position) {
        if (cityList != null)
            return cityList.get(position).hashCode();

        return 0;
    }

    @Override
    public CityResult getItem(int position) {
        if (cityList != null)
            return cityList.get(position);

        return null;
    }

    @Override
    public int getCount() {
        if (cityList != null)
            return cityList.size();

        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View result = convertView;

        if (result == null) {
            LayoutInflater inf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            result = inf.inflate(R.layout.citylist_layout, parent, false);

        }

        TextView tv = (TextView) result.findViewById(R.id.txtCityName);
        tv.setText(cityList.get(position).getCityName() + ", " + cityList.get(position).getCountry());

        return result;
    }

}
