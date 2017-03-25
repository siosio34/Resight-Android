package com.dragon4.owo.resight_android.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.dragon4.owo.resight_android.R;

/**
 * Created by joyeongje on 2017. 3. 19..
 */

public class MonitoringFragment extends Fragment {


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("test", "OncreateView 호출됨");

        ViewGroup rootView = (ViewGroup) getActivity().getLayoutInflater().inflate(R.layout.fragment_monitoring,container,false);
        Button button = (Button)rootView.findViewById(R.id.test_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return rootView;
    }






}
