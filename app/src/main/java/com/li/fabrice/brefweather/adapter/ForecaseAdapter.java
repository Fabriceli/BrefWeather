package com.li.fabrice.brefweather.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.li.fabrice.brefweather.R;
import com.li.fabrice.brefweather.model.ForecastDay;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fabrice on 20/02/2016.
 */
public class ForecaseAdapter extends BaseAdapter {

    private Context context;
    private SparseArray<View> lmap = new SparseArray<>();
    private List<ForecastDay> daysList = new ArrayList<>();

    public ForecaseAdapter(Context context,List<ForecastDay> daysList) {
        this.context = context;
        this.daysList = daysList;
    }

    @Override
    public int getCount() {
        if (daysList != null)
            return daysList.size();
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (daysList != null)
            return daysList.get(position);
        return null;
    }

    @Override
    public long getItemId(int position) {
        if (daysList != null)
            return daysList.get(position).hashCode();

        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (lmap.get(position) == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_forecase, null);
            holder.forecase_day = (TextView) convertView.findViewById(R.id.forecase_day);
            holder.forecase_image = (ImageView) convertView.findViewById(R.id.forecase_image);
            holder.forecase_text = (TextView) convertView.findViewById(R.id.forecase_text);
            holder.forecase_high = (TextView) convertView.findViewById(R.id.forecase_high);
            holder.forecase_low = (TextView) convertView.findViewById(R.id.forecase_low);
            lmap.put(position, convertView);
            convertView.setTag(holder);
        }else{
            convertView = lmap.get(position);
            holder = (ViewHolder) convertView.getTag();

        }
        if(daysList.get(position).getCode()<48 && daysList.get(position).getCode()>=0){
            int resourceId = convertView.getResources().getIdentifier("drawable/icon_" + daysList.get(position).getCode(),
                    null, context.getPackageName());
            @SuppressWarnings("deprecation")
            Drawable weatherIconDrawable = convertView.getResources().getDrawable(resourceId);
            holder.forecase_image.setImageDrawable(weatherIconDrawable);
        }else {
            int resourceId = convertView.getResources().getIdentifier("drawable/icon_na", null, context.getPackageName());
            @SuppressWarnings("deprecation")
            Drawable weatherIconDrawable = convertView.getResources().getDrawable(resourceId);
            holder.forecase_image.setImageDrawable(weatherIconDrawable);
        }

        holder.forecase_day.setText(String.format("%s", daysList.get(position).getDay()));
        holder.forecase_high.setText(String.format("%s", daysList.get(position).getHigh()));
        holder.forecase_low.setText(String.format("%s", daysList.get(position).getLow()));
        holder.forecase_text.setText(String.format("%s", daysList.get(position).getText()));
        return convertView;
    }

    public class ViewHolder {
        public TextView forecase_day;
        public ImageView forecase_image;
        public TextView forecase_text;
        public TextView forecase_high;
        public TextView forecase_low;

    }

}
