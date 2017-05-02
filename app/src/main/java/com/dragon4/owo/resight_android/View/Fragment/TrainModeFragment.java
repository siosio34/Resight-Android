package com.dragon4.owo.resight_android.View.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.dragon4.owo.resight_android.R;
import com.dragon4.owo.resight_android.View.Activity.HandMotionSelectActivity;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet;
import com.github.mikephil.charting.renderer.RadarChartRenderer;

import java.util.ArrayList;

public class TrainModeFragment extends Fragment {

    private ViewGroup rootView;
    private RadarChart mSensorChart;

    private final int REQUEST_HAND_MOTOTION = 9997;


    public TrainModeFragment() {

}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("TestModeFragment", "OncreateView 호출됨");
        rootView = (ViewGroup) getActivity().getLayoutInflater().inflate(R.layout.fragment_traing_mode, container, false);
        initRaderChart();
        registHandSelectButton();
        return rootView;
    }

    void initRaderChart() {
        mSensorChart =(RadarChart) rootView.findViewById(R.id.sensor_rader_chartview);


        mSensorChart.setBackgroundColor(Color.rgb(60, 65, 82));


        mSensorChart.getDescription().setEnabled(false);


        mSensorChart.setWebLineWidth(1f);
        mSensorChart.setWebColor(Color.LTGRAY);
        mSensorChart.setWebLineWidthInner(1f);
        mSensorChart.setWebColorInner(Color.LTGRAY);
        mSensorChart.setWebAlpha(100);

        setData();

        mSensorChart.animateXY(
                1400, 1400,
                Easing.EasingOption.EaseInOutQuad,
                Easing.EasingOption.EaseInOutQuad);

        XAxis xAxis = mSensorChart.getXAxis();
        xAxis.setTextSize(9f);
        xAxis.setYOffset(0f);
        xAxis.setXOffset(0f);

        xAxis.setValueFormatter(new IAxisValueFormatter() {

            private String[] mActivities = new String[]{"센서.01", "센서.02", "센서.03", "센서.04", "센서.05","센서.06"};

            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return mActivities[(int) value % mActivities.length];
            }
        });
        xAxis.setTextColor(Color.WHITE);

        YAxis yAxis = mSensorChart.getYAxis();

        yAxis.setLabelCount(6, false);
        yAxis.setTextSize(9f);
        yAxis.setAxisMinimum(0f);
        yAxis.setAxisMaximum(80f);
        yAxis.setDrawLabels(false);

        Legend l = mSensorChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setFormSize(16);
        l.setEnabled(false);

        l.setXEntrySpace(7f);
        l.setYEntrySpace(10f);
        l.setTextColor(Color.WHITE);

    }

    public void setData() {
        float mult = 80;
        float min = 20;
        int cnt = 6;

        ArrayList<RadarEntry> entries1 = new ArrayList<RadarEntry>();

        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.
        for (int i = 0; i < cnt; i++) {
            float val1 = (float) (Math.random() * mult) + min;
            entries1.add(new RadarEntry(val1));
        }

        RadarDataSet set1 = new RadarDataSet(entries1,"Sensor 데이터");

        set1.setColor(Color.rgb(103, 110, 129));
        set1.setFillColor(Color.rgb(103, 110, 129));
        set1.setDrawFilled(true);
        set1.setFillAlpha(180);
        set1.setLineWidth(2f);
        set1.setDrawHighlightCircleEnabled(true);
        set1.setDrawHighlightIndicators(false);


        RadarData data = new RadarData(set1);
        data.setValueTextSize(8f);
        data.setDrawValues(false);
        data.setValueTextColor(Color.WHITE);

        mSensorChart.setData(data);
        mSensorChart.invalidate();
    }

    private void registHandSelectButton() {
        ImageView imageView = (ImageView) rootView.findViewById(R.id.motion_setting_button);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TrainMode","클릭됨");
                Intent intent = new Intent(getActivity(), HandMotionSelectActivity.class);
               startActivityForResult(intent,REQUEST_HAND_MOTOTION);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("codetest", "들어오니");
        if (requestCode == REQUEST_HAND_MOTOTION) {
            Log.d("codetest", "들어오느니");
            if (resultCode == Activity.RESULT_OK) {
                Log.d("codetest", "들어오느니리이");
                String test = data.getStringExtra("handCode");
                Log.d("codetest", test);
            }
        }
    }



}
