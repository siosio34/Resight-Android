package com.dragon4.owo.resight_android.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.dragon4.owo.resight_android.Model.SensorData;
import com.dragon4.owo.resight_android.R;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

/**
 * Created by joyeongje on 2017. 3. 19..
 */

public class MonitoringFragment extends Fragment {

    private final Handler mHanler = new Handler();
    private Runnable mTimer;
    private double xValue = 0d;
    private GraphView[] graphViewArrays;
    private LineGraphSeries<DataPoint>[] graphSeriesArrays;

    int randNum;

    private String deviceID;
    private SensorData sensorData;

    public MonitoringFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("test", "OncreateView 호출됨");
        ViewGroup rootView = (ViewGroup) getActivity().getLayoutInflater().inflate(R.layout.fragment_monitoring, container, false);
        //GraphView graphView = (GraphView) rootView.findViewById(R.id.sensor_graph0);
        initGraph(rootView);
        return rootView;
    }

    private void initGraph(ViewGroup rootView) {
        graphViewArrays = new GraphView[6];
        graphSeriesArrays = new LineGraphSeries[6];

        graphViewArrays[0] = (GraphView) rootView.findViewById(R.id.sensor_graph0);
        graphViewArrays[1] = (GraphView) rootView.findViewById(R.id.sensor_graph1);
        graphViewArrays[2] = (GraphView) rootView.findViewById(R.id.sensor_graph2);
        graphViewArrays[3] = (GraphView) rootView.findViewById(R.id.sensor_graph3);
        graphViewArrays[4] = (GraphView) rootView.findViewById(R.id.sensor_graph4);
        graphViewArrays[5] = (GraphView) rootView.findViewById(R.id.sensor_graph5);

        for (int i = 0; i < 6; i++) {
            graphViewArrays[i].getViewport().setMinY(0);
            graphViewArrays[i].getViewport().setMaxY(100);
            graphViewArrays[i].getViewport().setYAxisBoundsManual(true);

            graphViewArrays[i].getViewport().setMinX(0);
            graphViewArrays[i].getViewport().setMaxX(40);
            graphViewArrays[i].getViewport().setXAxisBoundsManual(true);
            //graphViewArrays[i].getGridLabelRenderer().setHorizontalLabelsVisible(false);
            //graphViewArrays[i].getGridLabelRenderer().setVerticalLabelsVisible(false);

            graphSeriesArrays[i] = new LineGraphSeries<>();
            graphViewArrays[i].addSeries(graphSeriesArrays[i]);
        }

    }

    public void onResume() {
        super.onResume();
        mTimer = new Runnable() {
            @Override
            public void run() {
                xValue += 1d;
                for (int i = 0; i < 6; i++) {
                    randNum = (int) ((Math.random() * 100) + 1);
                    graphSeriesArrays[i].appendData(new DataPoint(xValue, randNum), true, 40);
                }
                mHanler.postDelayed(this,50); //
            }
        };
        mHanler.postDelayed(mTimer, 1000); //
    }

    public void onPause() {
        super.onPause();
        mHanler.removeCallbacks(mTimer);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_resight_main, menu);
    }

}
