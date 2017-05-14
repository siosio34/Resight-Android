package com.dragon4.owo.resight_android.view.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.dragon4.owo.resight_android.R;
import com.dragon4.owo.resight_android.view.Activity.HandMotionSelectActivity;
import com.dragon4.owo.resight_android.util.ActivityResultEvent;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

public class TrainModeFragment extends Fragment {

    private ViewGroup rootView;
    private RadarChart mSensorChart;

    private final int REQUEST_HAND_MOTOTION = 9997;
    private ImageView imageView;

    public TrainModeFragment() {

}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("TestModeFragment", "OncreateView 호출됨");
        rootView = (ViewGroup) getActivity().getLayoutInflater().inflate(R.layout.fragment_traing_mode, container, false);
        initRaderChart();
        registHandSelectButton();
        EventBus.getDefault().register(this);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("ㅇD","START");
    }

    @Override
    public void onDestroy() {
      //  EventBus.getDefault().unregister(this);
        Log.d("ㅇD","onDestroy");
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Subscribe
    public void framgmentOnActivityResult(ActivityResultEvent activityResultEvent) {
        int requestCode = activityResultEvent.getRequestCode();
        int resultCode = activityResultEvent.getResultCode();
        Intent data = activityResultEvent.getData();

        if (requestCode == REQUEST_HAND_MOTOTION) {
            if (resultCode == Activity.RESULT_OK) {
                String test = data.getStringExtra("handCode");
                int imageID = data.getIntExtra("handImage",R.drawable.icon_hand00_open);
                imageView.setImageResource(imageID);
            }
        }
    }

  //  @Subscribe
  //  public void onMessageEvent(ActivityResultEvent event) {
  //      Log.d("ㅇd",String.valueOf(event.getRequestCode()));
  //      //Toast.makeText(getActivity(), event.message, Toast.LENGTH_SHORT).show();
  //  }

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
        imageView = (ImageView) rootView.findViewById(R.id.motion_setting_button);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TrainMode","클릭됨");
                Intent intent = new Intent(getActivity(), HandMotionSelectActivity.class);
                getActivity().startActivityForResult(intent,REQUEST_HAND_MOTOTION);
            }
        });
    }

    // This method will be called when a SomeOtherEvent is posted
  //  @Subscribe
  //  public void handleSomethingElse(SomeOtherEvent event) {
  //      doSomethingWith(event);
  //  }



}
