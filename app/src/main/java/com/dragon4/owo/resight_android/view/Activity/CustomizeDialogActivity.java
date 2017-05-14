package com.dragon4.owo.resight_android.view.Activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.dragon4.owo.resight_android.R;
import com.tsengvn.typekit.TypekitContextWrapper;

public class CustomizeDialogActivity extends Activity {

    private static final String TAG = "CustomizeDialogActivity";
    ImageView imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_customize_dialog);
        registerButtonEvent();
    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }


    public void registerButtonEvent() {
        imageView = (ImageView)findViewById(R.id.first_hand_button);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG,"Click first_hand_Button");
            }
        });

        ImageView imageView1 = (ImageView)findViewById(R.id.second_hand_button);
        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG,"Click second_hand_button ");
            }
        });

        ImageView imageView2 = (ImageView)findViewById(R.id.third_hand_button);
        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG,"Click third_hand_button");
            }
        });
    }



}
