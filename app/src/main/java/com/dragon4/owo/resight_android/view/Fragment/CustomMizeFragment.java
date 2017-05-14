package com.dragon4.owo.resight_android.view.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.dragon4.owo.resight_android.R;

/**
 * Created by joyeongje on 2017. 3. 19..
 */

public class CustomMizeFragment extends Fragment {

    Fragment fragment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_customize,container,false);
        registFloatingButton(rootView);
        return rootView;
    }

    private void registFloatingButton(ViewGroup rootView) {
        ImageView customFloatingButton;
        customFloatingButton = (ImageView)rootView.findViewById(R.id.icon_cutomize_app_addbutton);
        fragment = new CustomMizeAppSelectFragment();
        customFloatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                transaction.add(R.id.custom_mize_fragment_layout, fragment).commit();
            }
        });
    }


}
