package com.li.fabrice.brefweather;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.li.fabrice.brefweather.activity.AddCity;
import com.li.fabrice.brefweather.activity.BarCourbeChart;
import com.li.fabrice.brefweather.activity.Settings;
import com.li.fabrice.brefweather.adapter.ForecaseAdapter;
import com.li.fabrice.brefweather.model.Astronomy;
import com.li.fabrice.brefweather.model.Atmosphere;
import com.li.fabrice.brefweather.model.Channel;
import com.li.fabrice.brefweather.model.ForecastDay;
import com.li.fabrice.brefweather.model.Item;
import com.li.fabrice.brefweather.model.Location;
import com.li.fabrice.brefweather.model.Wind;
import com.li.fabrice.brefweather.service.UpdateCityService;
import com.li.fabrice.brefweather.service.WeatherServiceListener;
import com.li.fabrice.brefweather.util.GetCityWeather;
import com.li.fabrice.brefweather.view.ArcMenu;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements WeatherServiceListener {
    private ImageView imageWeather;
    private TextView city,temperature,description,sunrise,sunset,humidity,wind,visibility,pressure,date,today,forecase_high_today,forecase_low_today;
    private ArcMenu menu;
    private SharedPreferences prefs;
    private GetCityWeather getCityWeather;
    private ListView lvDay;
    private ForecaseAdapter adapter;
    private List<ForecastDay> daysList;
    private int code;
    private String day;
    private String high;
    private String low;
    private String text;
    private String woeid;


    private ArrayList<String> jourSemaine;
    private ArrayList<String> forecaseJour;
    private ArrayList<Integer> hightT;
    private ArrayList<Integer> lowT;
    private String unit ;


    Handler updateHandler = new Handler() {
        public void handleMessage(Message msg) {
            Channel channel;
            channel = (Channel) msg.obj;
            Log.e("channel", String.valueOf(channel));
            jourSemaine = new ArrayList<>();
            forecaseJour = new ArrayList<>();
            hightT = new ArrayList<>();
            lowT = new ArrayList<>();
            Item item = channel.getItem();
            Location location = channel.getLocation();
            Astronomy astronomy = channel.getAstronomy();
            Atmosphere atmosphere = channel.getAtmosphere();
            Wind winddata = channel.getWind();
            city.setText(String.format("%s ", location.getCity()));

            if(item.getCondition().getCode()<48 && item.getCondition().getCode()>=0){
                int resourceId = getResources().getIdentifier("drawable/icon_" + item.getCondition().getCode(), null, getPackageName());
                @SuppressWarnings("deprecation")
                Drawable weatherIconDrawable = getResources().getDrawable(resourceId);
                imageWeather.setImageDrawable(weatherIconDrawable);
            }
            Log.e("unit", prefs.getString("unit", null) + " ");
            String s;
            s = prefs.getString("unit",null);
            if(s != null  ){
                if(s.equals("c")){
                    temperature.setText(String.format("%dºC", item.getCondition().getTemperature()));

                }else {
                    temperature.setText(String.format("%dºF", item.getCondition().getTemperature()));

                }
            }else {
                temperature.setText(String.format("%dºC", item.getCondition().getTemperature()));
            }

            date.setText(String.format("%s", item.getCondition().getDate()));
            visibility.setText(String.format("%s %s", atmosphere.getVisibility(), channel.getUnits().getDistance()));
            pressure.setText(String.format("%s %s", atmosphere.getPressure(), channel.getUnits().getPressure()));
            wind.setText(String.format("%s %s", winddata.getSpeed(), channel.getUnits().getSpeed()));
            sunrise.setText(String.format("%s", astronomy.getSunrise()));
            sunset.setText(String.format("%s", astronomy.getSunset()));
            humidity.setText(String.format("%s %%", atmosphere.getHumidity()));
            description.setText(String.format("%s", item.getCondition().getDescription()));

            try {
                Parcelable listState = lvDay.onSaveInstanceState();
                daysList = new ArrayList<>();
                adapter = new ForecaseAdapter(MainActivity.this,daysList);
                lvDay.setAdapter(adapter);
                JSONArray dayArray = item.getDayJSONArray();
                String todayString = dayArray.getJSONObject(0).getString("day");
                String high_today = dayArray.getJSONObject(0).getString("high");
                String low_today = dayArray.getJSONObject(0).getString("low");

                jourSemaine.add(todayString);
                forecaseJour.add(dayArray.getJSONObject(0).getString("text"));
                hightT.add(Integer.valueOf(high_today));
                lowT.add(Integer.valueOf(low_today));

                today.setText(String.format("%s", todayString));
                forecase_high_today.setText(String.format("%s", high_today));
                forecase_low_today.setText(String.format("%s", low_today));
                for(int i=1;i<dayArray.length();i++){
                    JSONObject datalist = dayArray.getJSONObject(i);
                    day = datalist.getString("day");
                    high = datalist.getString("high");
                    low = datalist.getString("low");
                    text = datalist.getString("text");
                    code = Integer.parseInt(datalist.getString("code"));

                    //s[i] = day;
                    jourSemaine.add(day);
                    forecaseJour.add(text);
                    hightT.add(Integer.valueOf(high));
                    lowT.add(Integer.valueOf(low));
                    daysList.add(new ForecastDay(code,day,high,low,text));

                }
                adapter.notifyDataSetChanged();
                lvDay.onRestoreInstanceState(listState);
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        reloaddata();
        initEvent();


        Intent startIntent = new Intent(MainActivity.this, UpdateCityService.class);
        startService(startIntent);
        //binding la service
        bindService(startIntent, conn, Context.BIND_AUTO_CREATE);

    }

    private void initEvent() {

        menu.setOnMenuItemClickListener(new ArcMenu.OnMenuItemClickListener() {
            @Override
            public void onClick(View view, int pos) {
                if (view.getTag().equals("addcity")) {
                    Intent intent = new Intent();
                    intent.setClass(MainActivity.this, AddCity.class);
                    startActivity(intent);
                }
                if (view.getTag().equals("temperatureunit")) {
                    if (woeid != null) {
                        String s;
                        s = prefs.getString("unit", null);
                        if (s != null) {
                            if (s.equals("c")) {
                                unit = "f";
                            } else {
                                unit = "c";
                            }
                        } else {
                            unit = "f";
                        }
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("unit", unit);
                        editor.apply();
                        reloaddata();
                    } else {
                        makeCustomToast("Please add city.", 3000);
                    }

                }
                if (view.getTag().equals("settings")) {
                    Intent intent = new Intent();
                    intent.setClass(MainActivity.this, Settings.class);
                    startActivity(intent);

                }
                if (view.getTag().equals("curve")) {

                    if (woeid != null) {
                        Intent intent = new Intent();
                        intent.setClass(MainActivity.this, BarCourbeChart.class);
                        intent.putStringArrayListExtra("jourSemaine", jourSemaine);
                        intent.putStringArrayListExtra("forecaseJour", forecaseJour);
                        intent.putIntegerArrayListExtra("hightT", hightT);
                        intent.putIntegerArrayListExtra("lowT", lowT);
                        intent.putExtra("unit", unit);
                        startActivity(intent);
                    } else {
                        makeCustomToast("Please add city.",3000);
                        //Toast.makeText(MainActivity.this, "Please add city.", Toast.LENGTH_SHORT).show();
                    }

                }
                if (view.getTag().equals("exchangerate")) {

                    if (woeid != null) {
                        reloaddata();
                    } else {
                        makeCustomToast("Please add city.",3000);
                        //Toast.makeText(MainActivity.this, "Please add city.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void reloaddata(){
        woeid = prefs.getString("woeid", null);

        if (woeid != null) {
            String s;
            s = prefs.getString("unit",null);
            if(s != null){
                if(s.equals("c")){
                    unit = "c";
                    getCityWeather.refreshWeather(woeid,unit);
                }else{
                    unit = "f";
                    getCityWeather.refreshWeather(woeid,unit);
                }
            }else {
                unit = "c";
                getCityWeather.refreshWeather(woeid,unit);
            }

        }else {
            daysList = new ArrayList<>();
            adapter = new ForecaseAdapter(this,daysList);
            lvDay.setAdapter(adapter);

            for(int i=0;i<3;i++){
                code = 0;
                day = "No data";
                high = "No data";
                low = "No data";
                text = "No data";
                daysList.add(new ForecastDay(code,day,high,low,text));

            }
            adapter.notifyDataSetChanged();

        }
    }

    private void initView() {
        unit = null;
        lvDay = (ListView) findViewById(R.id.list5day);
        city = (TextView) findViewById(R.id.city);
        date = (TextView) findViewById(R.id.date);
        today = (TextView) findViewById(R.id.today);
        forecase_high_today = (TextView) findViewById(R.id.forecase_high_today);
        forecase_low_today = (TextView) findViewById(R.id.forecase_low_today);
        imageWeather = (ImageView) findViewById(R.id.imageWeather);
        temperature = (TextView) findViewById(R.id.temperature);
        description = (TextView) findViewById(R.id.description);
        sunrise = (TextView) findViewById(R.id.sunrise);
        sunset = (TextView) findViewById(R.id.sunset);
        humidity = (TextView) findViewById(R.id.humidity);
        wind = (TextView) findViewById(R.id.wind);
        visibility = (TextView) findViewById(R.id.visibility);
        pressure = (TextView) findViewById(R.id.pressure);
        menu = (ArcMenu) findViewById(R.id.menu);
        getCityWeather = new GetCityWeather(MainActivity.this);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);

    }

    ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            UpdateCityService.Binder binder = (UpdateCityService.Binder) service;
            binder.getService().setCallback(new UpdateCityService.Callback() {
                @Override
                public void DataChange(Channel result) {
                    Message msg = new Message();
                    msg.obj = result;
                    updateHandler.sendMessage(msg);
                    //Log.e("5s","5s!!!!!!");
                }
            });
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    public void serviceSuccess(Channel channel) {

        Message msg = new Message();
        msg.obj = channel;
        updateHandler.sendMessage(msg);
    }

    @Override
    public void serviceFailure(Exception exception) {
        makeCustomToast(exception.getMessage(),3000);
        //Toast.makeText(this, exception.getMessage(), Toast.LENGTH_LONG).show();
    }

    public void makeCustomToast(String text,int duration) {

        View layout = getLayoutInflater().inflate(R.layout.my_toast,
                (ViewGroup) findViewById(R.id.my_toast_layout));
        // set a message
        TextView toastText = (TextView) layout.findViewById(R.id.toasttext);
        toastText.setText(text);
        //transparant
        layout.getBackground().setAlpha(235);
        // Toast...
        Toast toast = new Toast(this);
        toast.setGravity(Gravity.CENTER, 0, 0);

        toast.setDuration(duration);
        toast.setView(layout);
        toast.show();
    }


    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Intent startIntent = new Intent(MainActivity.this, UpdateCityService.class);
        unbindService(conn);
        stopService(startIntent);
        super.onDestroy();
    }
}
