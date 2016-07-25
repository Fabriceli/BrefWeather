package com.li.fabrice.brefweather.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.li.fabrice.brefweather.R;
import com.li.fabrice.brefweather.adapter.CityAdapter;
import com.li.fabrice.brefweather.model.CityResult;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class AddCity extends AppCompatActivity {

    public static String YAHOO_GET_CITY_URL = "http://where.yahooapis.com/v1";
    protected ListView listViewResult;
    private EditText edtCity;
    private CityAdapter adpt;
    protected static String APPID = "[dj0yJmk9SDJKRTI4TWdmSnJSJmQ9WVdrOVVHVkllbGROTm5NbWNHbzlNQS0tJnM9Y29uc3VtZXJzZWNyZXQmeD01Mg--]";
    private List<CityResult> resultcity;

    public static final String TAG = "MyTag";
    private RequestQueue mQueue;
    private StringRequest stringRequest;

    private Handler getCityResultHandler = new Handler() {
        public void handleMessage(android.os.Message msg){
            String s = (String)msg.obj;
            try {
                resultcity = new ArrayList<>();
                adpt = new CityAdapter(AddCity.this, resultcity);
                listViewResult.setAdapter(adpt);
                //Log.e("result", result);
                XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
                parser.setInput(new StringReader(s));
                //Log.e("!!!!!", "XML Parser ok");
                int event = parser.getEventType();

                CityResult cty = null;
                String tagName ;
                String currentTag = null;

                // We start parsing the XML
                while (event != XmlPullParser.END_DOCUMENT) {
                    tagName = parser.getName();
                    //Log.e("tagName", "Tag ["+tagName+"]");

                    switch (event) {
                        case XmlPullParser.START_DOCUMENT:
                            break;
                        case XmlPullParser.START_TAG:
                            if (tagName.equals("place")) {
                                // place Tag Found so we create a new CityResult
                                cty = new CityResult();
                                //Log.e(" tagName", tagName);
                            }
                            currentTag = tagName;
                            break;
                        case XmlPullParser.TEXT:
                            if ("woeid".equals(currentTag)) {
                                cty.setWoeid(parser.getText());
                                //Log.e(" woeid", parser.getText());
                            }
                            else if ("name".equals(currentTag)){
                                cty.setCityName(parser.getText());
                                // Log.e(" cty.getCityName", cty.getCityName());
                            }
                            else if ("country".equals(currentTag)){
                                cty.setCountry(parser.getText());
                                //Log.e(" cty.getCountry()", cty.getCountry());
                            }
                            break;
                        case XmlPullParser.END_TAG:
                            if ("place".equals(tagName))
                            {
                                resultcity.add(cty);
                            }
                            break;
                    }
                    event = parser.next();
                    //Log.e("resultcity.size()", "while limian " + resultcity.size());
                }//end while

                if(resultcity.size()<=0){
                    resultcity.add(new CityResult(null,"Enter a valid city","please"));
                }
                adpt.notifyDataSetChanged();
                //Log.e("resultcity.size()", "while wan " + resultcity.size());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_city);

        listViewResult = (ListView) this.findViewById(R.id.listViewResult);
        edtCity = (EditText) findViewById(R.id.edtCity);

        edtCity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.e("beforeTextChanged", s.toString() + " ");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //Log.e("s",s.toString()+" ");
                dispalyList(s.toString()); //展示结果
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.e("afterTextChanged", s.toString() + " ");
            }
        });


        listViewResult.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CityResult result = (CityResult) parent.getItemAtPosition(position);
                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(AddCity.this);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("woeid", result.getWoeid());
                editor.putString("cityName", result.getCityName());
                editor.putString("country", result.getCountry());
                editor.apply();
                NavUtils.navigateUpFromSameTask(AddCity.this);
            }
        });
    }


    void dispalyList(String s) {
        if (s == null || s.trim().length() == 0 || s.replaceAll(" ", "%20").length()<2) {
            CityAdapter adpt = new CityAdapter(this, null);
            listViewResult.setAdapter(adpt);
        }else {
            mQueue = Volley.newRequestQueue(this);
            stringRequest = new StringRequest(Request.Method.GET, makeQueryCityURL(s), new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    Message msg = new Message();
                    msg.obj = s;
                    getCityResultHandler.sendMessage(msg);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    //显示错误信息
                    Log.e("volleyError", volleyError.toString() + " ");
                }
            });
            stringRequest.setTag(TAG);
            mQueue.add(stringRequest);
        }


    }

    /**
     *
     * URL pour cityname
     *
     * */
    private static String makeQueryCityURL(String cityName) {
        // We remove spaces in cityName
        cityName = cityName.replaceAll(" ", "%20");
        return YAHOO_GET_CITY_URL + "/places.q(" + cityName + ");count=" + 10 + "?appid=" + APPID;//10:显示城市个数
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mQueue != null) {
            mQueue.cancelAll(TAG);
        }
    }
}
