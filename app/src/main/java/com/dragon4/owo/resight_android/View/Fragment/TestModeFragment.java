package com.dragon4.owo.resight_android.View.Fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.dragon4.owo.resight_android.Model.SensorData;
import com.dragon4.owo.resight_android.R;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

/**
 * Created by joyeongje on 2017. 3. 19..
 */

public class TestModeFragment extends Fragment {

    private final Handler mHanler = new Handler();
    private Runnable mTimer;
    private double xValue = 0d;
    private GraphView[] graphViewArrays;
    private TextView[] textViews;
    private LineGraphSeries<DataPoint>[] graphSeriesArrays;

    int randNum;

    private String deviceID;
    private SensorData sensorData;

    private ViewGroup rootView;

    public TestModeFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("TestModeFragment", "OncreateView 호출됨");
        rootView = (ViewGroup) getActivity().getLayoutInflater().inflate(R.layout.fragment_testing_mode, container, false);
        //GraphView graphView = (GraphView) rootView.findViewById(R.id.sensor_graph0);
        initGraph();
        initTextView();
        return rootView;
    }

    private void initGraph() {
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

    private void initTextView() {
        textViews = new TextView[6];

        textViews[0] = (TextView) rootView.findViewById(R.id.sensor1);
        textViews[1] = (TextView) rootView.findViewById(R.id.sensor2);
        textViews[2] = (TextView) rootView.findViewById(R.id.sensor3);
        textViews[3] = (TextView) rootView.findViewById(R.id.sensor4);
        textViews[4] = (TextView) rootView.findViewById(R.id.sensor5);
        textViews[5] = (TextView) rootView.findViewById(R.id.sensor6);

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
                    textViews[i].setText(i + "번 : " + randNum);
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





}
