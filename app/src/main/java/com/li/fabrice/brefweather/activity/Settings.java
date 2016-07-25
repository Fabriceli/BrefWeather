package com.li.fabrice.brefweather.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioButton;

import com.li.fabrice.brefweather.R;

public class Settings extends AppCompatActivity{
    private SharedPreferences prefs;
    private RadioButton btn_2h,btn_4h,btn_8h,btn_12h;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        btn_2h = (RadioButton) findViewById(R.id.btn_2h);
        btn_4h = (RadioButton) findViewById(R.id.btn_4h);
        btn_8h = (RadioButton) findViewById(R.id.btn_8h);
        btn_12h = (RadioButton) findViewById(R.id.btn_12h);
        prefs = PreferenceManager.getDefaultSharedPreferences(Settings.this);
        int i = prefs.getInt("inverval",2);
        if(i==2){
            btn_2h.setChecked(true);
        }else if(i==4) {
            btn_4h.setChecked(true);
        }else if(i==8) {
            btn_8h.setChecked(true);
        }else if(i==12) {
            btn_12h.setChecked(true);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor e = prefs.edit();
        if(btn_2h.isChecked()){
            e.putInt("inverval",2);
            e.apply();
        }else if(btn_4h.isChecked()){
            e.putInt("inverval",4);
            e.apply();
        }else if(btn_8h.isChecked()){
            e.putInt("inverval",8);
            e.apply();
        }else if(btn_12h.isChecked()){
            e.putInt("inverval",12);
            e.apply();
        }


    }

    public void onRadioButtonClicked(View view){
        prefs = PreferenceManager.getDefaultSharedPreferences(Settings.this);
        boolean checked = ((RadioButton) view).isChecked();

        switch(view.getId()) {
            case R.id.btn_2h:
                if (checked){

                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putInt("inverval", 2);
                    editor.apply();
                }
                break;
            case R.id.btn_4h:
                if (checked){

                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putInt("inverval", 4);
                    editor.apply();
                }
                break;
            case R.id.btn_8h:
                if (checked){

                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putInt("inverval", 8);
                    editor.apply();
                }
                break;
            case R.id.btn_12h:
                if (checked){
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putInt("inverval", 12);
                    editor.apply();
                }
                break;
        }
    }

}
