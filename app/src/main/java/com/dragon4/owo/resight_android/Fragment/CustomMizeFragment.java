package com.dragon4.owo.resight_android.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dragon4.owo.resight_android.R;

/**
 * Created by joyeongje on 2017. 3. 19..
 */

public class CustomMizeFragment extends Fragment {

    private Fragment fragment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("test", "OncreateView 호출됨");
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_customize,container,false);

        registerFloatingButton(rootView);
        return rootView;
    }

    private void registerFloatingButton(ViewGroup rootView) {
        FloatingActionButton customFloatingButton;
        customFloatingButton = (FloatingActionButton)rootView.findViewById(R.id.custom_add_button);
        customFloatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Toast.makeText(getActivity(),"CustomMizeFragmen float button",Toast.LENGTH_SHORT).show();
                customAppSelectedFragment();
            }
        });
    }

    private void customAppSelectedFragment() {
        fragment = new CustomMizeAppSelectFragment();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.add(R.id.custom_mize_fragment_layout,fragment).commit();
    }

}
