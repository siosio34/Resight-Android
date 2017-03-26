package com.dragon4.owo.resight_android.Activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;

import com.dragon4.owo.resight_android.R;
import com.tsengvn.typekit.TypekitContextWrapper;

public class CustomizeDialogActivity extends Activity {


    private static final String TAG = "CustomizeDialogActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customize_dialog);
        requestWindowFeature(Window.FEATURE_NO_TITLE);


         //AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
         //alertDialog.setTitle("Custom Test");
//
         //alertDialog.setMessage("test").setCancelable(false)
         //        .setNeutralButton("OK", new DialogInterface.OnClickListener() {
         //            @Override
         //            public void onClick(DialogInterface dialog, int which) {
         //                stopService(getIntent());
         //                Log.i(TAG,"Click first Button");
         //                dialog.cancel();
         //                finish();
         //            }
         //        });
         //AlertDialog alertDialog1 = alertDialog.create();
         //alertDialog1.show();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    public void handButtonClick(View v) {
        switch (v.getId()) {
            case R.id.first_hand_button:
                Log.i(TAG,"Click first Button");
                finish();
                break;
            case R.id.second_hand_button:
                Log.i(TAG,"Click second_hand_button ");
                finish();
                break;
            case R.id.third_hand_button:
                Log.i(TAG,"Click third_hand_button");
                finish();
                break;
        }
    }
}
