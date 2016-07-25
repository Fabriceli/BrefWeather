package com.li.fabrice.brefweather.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.li.fabrice.brefweather.R;
import com.li.fabrice.brefweather.util.MyMarkerView;
import com.li.fabrice.brefweather.util.MyValueFormatter;

import java.util.ArrayList;

public class BarCourbeChart extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{
    private SwipeRefreshLayout swipeRefreshLayout;
    private BarChart barChart;
    private LineChart lineChart;
    private MyMarkerView mv;
    private String unit;
    private ArrayList<String> jourSemaine;
    private ArrayList<String> forecaseJour;
    private ArrayList<Integer> hightT;
    private ArrayList<Integer> lowT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_courbe_chart);

        initView();
        Intent intent =getIntent();
        jourSemaine = intent.getStringArrayListExtra("jourSemaine");
        //Log.e("jourSemaine", String.valueOf(jourSemaine)+"");
        forecaseJour = intent.getStringArrayListExtra("forecaseJour");
        hightT = intent.getIntegerArrayListExtra("hightT");
        lowT = intent.getIntegerArrayListExtra("lowT");
        unit = intent.getStringExtra("unit");
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light, android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        roadData();
    }

    private void initView(){
        jourSemaine = new ArrayList<>();
        forecaseJour = new ArrayList<>();
        hightT = new ArrayList<>();
        lowT = new ArrayList<>();
        unit = null;
        mv = new MyMarkerView(this, R.layout.marker_view);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe);
        barChart = (BarChart) findViewById(R.id.barchart);
        lineChart = (LineChart) findViewById(R.id.linechart);
    }


    private void roadData(){
        int N;
        if(hightT.size() != 0){
            N = hightT.size();
            LineData mLineData = makeLineData(N,lineChart);
            configChartAxis(mLineData,lineChart);
            BarChartjours(N, barChart);
            configChartAxis(barChart);
        }

    }

    /**
     *
     * Bar chart
     *
     * */
    private void configChartAxis(BarChart barChart){


        barChart.animateY(3000); // Exécuter immédiatement animation, Y

        barChart.setDescription("");//ne affiche pas les mots en bas à droite.
        barChart.setNoDataTextDescription("0_o");

        BarData barData =barChart.getBarData();
        barData.setDrawValues(false);
        /*barData.setValueTextSize(15);
        barData.setValueFormatter(new ValueFormatter() {
            //nouvelle format de bardata
            @Override
            public String getFormattedValue(float v, Entry entry, int i, ViewPortHandler viewPortHandler) {
                DecimalFormat decimalFormat = new DecimalFormat("");
                BigDecimal val = new BigDecimal(v).setScale(0, BigDecimal.ROUND_HALF_UP);//Arrondi au pair le plus proche
                if(unit.equals("c")){
                    return decimalFormat.format(val) + "°C";
                }else {
                    return decimalFormat.format(val) + "°F";
                }

            }

        });*/
        //background est transparant
        barChart.setGridBackgroundColor(0x70FFFFFF);
        barChart.setBackgroundColor(Color.WHITE);
        barChart.setDragEnabled(true);
        barChart.setTouchEnabled(true);
        barChart.setScaleEnabled(false);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setDrawGridLines(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);//les coordonnées de X affichent ci-dessous

        YAxis leftYAxis = barChart.getAxisLeft();
        if(unit.equals("c")){
            leftYAxis.setValueFormatter(new MyValueFormatter("T"));//Y ajoute le symbole de Celsius
        }else {
            leftYAxis.setValueFormatter(new MyValueFormatter("F"));//Y ajoute le symbole de Degré Fahrenheit
        }
        YAxis rightYAxis = barChart.getAxisRight();
        rightYAxis.setEnabled(false);//ne affiche pas les coordonnées de Y droite.

    }

    @SuppressWarnings("deprecation")
    public void BarChartjours(int N,BarChart barChart){


        barChart.setMarkerView(mv);
        mv.setxValuesArray(forecaseJour);

        ArrayList<BarEntry> yVals1 = new ArrayList<>();
        ArrayList<BarEntry> yVals2 = new ArrayList<>();

        for (int i = 0; i < N; i++) {
            yVals1.add(new BarEntry(lowT.get(i), i));
        }
        for (int i = 0; i < N; i++) {
            yVals2.add(new BarEntry(hightT.get(i), i));
        }


        BarDataSet set1 = new BarDataSet(yVals1, getString(R.string.min));
        set1.setColor(getResources().getColor(R.color.color_min));
        BarDataSet set2 = new BarDataSet(yVals2, getString(R.string.max));
        set2.setColor(getResources().getColor(R.color.color_max));

        ArrayList<BarDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);
        dataSets.add(set2);

        BarData data = new BarData(jourSemaine, dataSets);

        // add space between the dataset groups in percent of bar-width
        data.setGroupSpace(80f);

        barChart.setData(data);
        barChart.invalidate();
    }


    /**
     *
     *       LineChart
     *
     * */
    @SuppressWarnings("deprecation")
    private LineData makeLineData(int count,LineChart lineChart) {

        // Les données de l'axe Y
        ArrayList<Entry> yH = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Entry entry = new Entry(hightT.get(i), i);
            yH.add(entry);
        }
        ArrayList<Entry> yL = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Entry entry = new Entry(lowT.get(i), i);
            yL.add(entry);
        }
        lineChart.setMarkerView(mv);
        mv.setxValuesArray(forecaseJour);

        // l'ensemble des données de l'axe X
        LineDataSet mLineDataSetH = new LineDataSet(yH, "Temperature");
        LineDataSet mLineDataSetL = new LineDataSet(yL, "Temperature");

        // la configuration de la line
        mLineDataSetL.setLineWidth(3.0f);
        mLineDataSetL.setCircleSize(4.0f);
        mLineDataSetH.setLineWidth(3.0f);
        mLineDataSetH.setCircleSize(4.0f);
        mLineDataSetL.setColor(getResources().getColor(R.color.color_min));
        mLineDataSetL.setCircleColor(getResources().getColor(R.color.color_min));
        mLineDataSetH.setColor(getResources().getColor(R.color.color_max));
        mLineDataSetH.setCircleColor(getResources().getColor(R.color.color_max));

        mLineDataSetL.setDrawHighlightIndicators(false);
        mLineDataSetL.setHighLightColor(Color.CYAN);
        mLineDataSetH.setDrawHighlightIndicators(false);
        mLineDataSetH.setHighLightColor(Color.CYAN);

        // remplis le zone de sous la courbe, vert, demi-transparence
        mLineDataSetL.setDrawFilled(true);
        mLineDataSetL.setFillAlpha(20);
        mLineDataSetL.setFillColor(getResources().getColor(R.color.color_min));
        mLineDataSetH.setDrawFilled(true);
        mLineDataSetH.setFillAlpha(20);
        mLineDataSetH.setFillColor(getResources().getColor(R.color.color_max));

        mLineDataSetL.setCircleColorHole(Color.WHITE);
        ArrayList<LineDataSet> mLineDataSets = new ArrayList<>();
        mLineDataSets.add(mLineDataSetL);
        mLineDataSets.add(mLineDataSetH);
        LineData mLineData = new LineData(jourSemaine, mLineDataSets);

        // ne affiche pas des donnees sur la courbe
        mLineData.setDrawValues(false);

        return mLineData;

    }
    private void configChartAxis(LineData lineData, LineChart lineChart){
        lineChart.setDrawBorders(false);
        lineChart.animateY(3000); // Exécuter immédiatement animation, Y
        lineChart.setDescription("");//ne affiche pas les mots en bas à droite.
        lineChart.setNoDataTextDescription("0_o");
        lineChart.setDrawGridBackground(false);
        lineChart.setPinchZoom(false);
        lineChart.setBackgroundColor(Color.WHITE);
        //transparence définition
        lineChart.setGridBackgroundColor(0x70FFFFFF);
        lineChart.setDragEnabled(true);
        lineChart.setTouchEnabled(true);
        lineChart.setScaleEnabled(false);
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setDrawGridLines(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);//les coordonnées de X affichent ci-dessous
        YAxis leftYAxis = lineChart.getAxisLeft();
        if(unit.equals("c")){
            leftYAxis.setValueFormatter(new MyValueFormatter("T"));//Y ajoute le symbole de Celsius
        }else {
            leftYAxis.setValueFormatter(new MyValueFormatter("F"));//Y ajoute le symbole de Degré Fahrenheit
        }

        YAxis rightYAxis = lineChart.getAxisRight();
        rightYAxis.setEnabled(false);//ne affiche pas les coordonnées de Y droite.
        lineChart.setData(lineData);
        Legend mLegend = lineChart.getLegend();
        mLegend.setEnabled(false);
        lineChart.animateX(2000);

    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        ( new Handler()).postDelayed(new Runnable() {
            @Override
            public void run() {
                roadData();
                swipeRefreshLayout.setRefreshing(false);

            }
        }, 3000);
    }
}
