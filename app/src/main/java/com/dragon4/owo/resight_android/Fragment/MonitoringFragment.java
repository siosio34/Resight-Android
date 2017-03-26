package com.dragon4.owo.resight_android.Fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dragon4.owo.resight_android.Blooth.BluetoothCommunication;
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
    private double xValue = 5d;
    private LineGraphSeries<DataPoint> mSeries;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("test", "OncreateView 호출됨");
        ViewGroup rootView = (ViewGroup) getActivity().getLayoutInflater().inflate(R.layout.fragment_monitoring,container,false);
        GraphView graphView = (GraphView) rootView.findViewById(R.id.sensor_graph0);

        initGraph(graphView);
        return rootView;
    }

    private void initGraph(GraphView graphView) {
        graphView.getViewport().setMinY(0);
        graphView.getViewport().setMaxY(100);
        graphView.getViewport().setYAxisBoundsManual(true);
        graphView.getViewport().setMinX(0);
        graphView.getViewport().setMaxX(40);
        graphView.getViewport().setXAxisBoundsManual(true);
        graphView.getGridLabelRenderer().setHorizontalLabelsVisible(false);
        mSeries = new LineGraphSeries<>();
        graphView.addSeries(mSeries);

    }

    public void onResume() {
        super.onResume();

        mTimer = new Runnable() {
            @Override
            public void run() {
                if(xValue == 40) {
                    xValue = 0 ;
                    mSeries.resetData(new DataPoint[] {
                            new DataPoint(xValue,BluetoothCommunication.sensorsData[0])
                    });
                }
                xValue += 1d;
                mSeries.appendData(new DataPoint(xValue, BluetoothCommunication.sensorsData[0]), false, 40);
                mHanler.postDelayed(this,50); // 0.5초후에 그래프 갱신
            }
        };
        mHanler.postDelayed(mTimer,700); // 0.7초후에 타이머 재시작.
    }

    public void onPause() {
        super.onPause();
        mHanler.removeCallbacks(mTimer);
    }
}
