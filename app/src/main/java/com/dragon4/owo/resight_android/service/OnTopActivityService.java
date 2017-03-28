package com.dragon4.owo.resight_android.service;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.dragon4.owo.resight_android.Activity.CustomizeDialogActivity;

public class OnTopActivityService extends Service {

    private WindowManager.LayoutParams mParams;
    private WindowManager mWindowManager;
    private Context appCtx;


    private float START_X, START_Y;
    private int PREV_X, PREV_Y;


    private OnTouchListener mViewTouchListener = new OnTouchListener() {
        @Override public boolean onTouch(View v, MotionEvent event) {
            switch(event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    START_X = event.getRawX();
                    START_Y = event.getRawY();
                    PREV_X = mParams.x;
                    PREV_Y = mParams.y;

                    Log.d("눌린좌표",START_X + " : "+ START_Y);
                    Log.d("눌린좌표2",PREV_X + " : "+ PREV_Y);

                    Intent popupIntent = new Intent(appCtx, CustomizeDialogActivity.class);
                    PendingIntent pi = PendingIntent.getActivity(appCtx, 0, popupIntent, PendingIntent.FLAG_ONE_SHOT);

                    try{
                        pi.send();
                    }
                    catch(Exception e){
                        Toast.makeText(appCtx, e.toString(), Toast.LENGTH_LONG);
                    }

                    break;

            }

            return true;
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();

        appCtx = getApplicationContext();

        //최상위 윈도우에 넣기 위한 설정

        TextView mPopupView = new TextView(this);
        mPopupView.setTextColor(Color.GREEN);
        mPopupView.setText("");
        mPopupView.setGravity(Gravity.CENTER);
        mPopupView.setOnTouchListener(mViewTouchListener);              //팝업뷰에 터치 리스너 등록

        mParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE); //윈도 매니저
        mWindowManager.addView(mPopupView ,mParams);

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
