package com.dragon4.owo.resight_android.View.Fragment;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.dragon4.owo.resight_android.R;

/**
 * Created by young on 2017-05-01.
 */

public class TestTrainModeMainFragment extends Fragment {

    ViewGroup rootView;

    final TestModeFragment testFragment = new TestModeFragment();
    final TrainModeFragment trainFragment = new TrainModeFragment();

    public TestTrainModeMainFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("test", "OncreateView 호출됨");
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_test_train_mode_main, container, false);
        getChildFragmentManager().beginTransaction().replace(R.id.test_train_mode_fragment_container,testFragment).commit();
        registButtonEvent();
        return rootView;
    }

    private void registButtonEvent() {
        final Button testModeButton = (Button)rootView.findViewById(R.id.test_mode_button);
        final Button trainModeButton = (Button) rootView.findViewById(R.id.train_mode_button);
        final View testModeBottomView = rootView.findViewById(R.id.test_mode_button_bottom);
        final View trainModeBottomView = rootView.findViewById(R.id.train_mode_button_bottom);



        testModeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testModeBottomView.setBackgroundColor(Color.parseColor("#595959"));
                trainModeBottomView.setBackgroundColor(Color.parseColor("#ACACAC"));
                testModeButton.setTextColor(Color.parseColor("#595959"));
                trainModeButton.setTextColor(Color.parseColor("#ACACAC"));
                getChildFragmentManager().beginTransaction().replace(R.id.test_train_mode_fragment_container,testFragment).commit();

                //FragmentTransaction transaction
            }
        });
        trainModeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testModeBottomView.setBackgroundColor(Color.parseColor("#ACACAC"));
                trainModeBottomView.setBackgroundColor(Color.parseColor("#595959"));
                testModeButton.setTextColor(Color.parseColor("#ACACAC"));
                trainModeButton.setTextColor(Color.parseColor("#595959"));
                getChildFragmentManager().beginTransaction().replace(R.id.test_train_mode_fragment_container,trainFragment).commit();
            }
        });
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_resight_main, menu);
    }
}
