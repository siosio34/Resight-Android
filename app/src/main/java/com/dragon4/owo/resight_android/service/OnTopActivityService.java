package com.dragon4.owo.resight_android.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dragon4.owo.resight_android.R;
import com.dragon4.owo.resight_android.View.Fragment.CustomMizeAppSelectFragment;

import static android.view.Gravity.CENTER;

public class OnTopActivityService extends Service {

    private WindowManager.LayoutParams mParams;
    private WindowManager mWindowManager;
    private Context appCtx;

    TextView textView;
    RelativeLayout toplayout;
    View mPopupView;
    private boolean touchFlag = false;

    private float START_X, START_Y;

    private OnTouchListener mViewTouchListener = new OnTouchListener() {
        @Override public boolean onTouch(View v, MotionEvent event) {
            switch(event.getAction()) {
                case MotionEvent.ACTION_DOWN:

                    START_X = event.getRawX();
                    START_Y = event.getRawY();
                    Log.d("눌린좌표",START_X + " : "+ START_Y);

                    appCtx.stopService(new Intent(appCtx,OnTopActivityService.class));
                   // stopService(new Intent(appCtx, OnTopActivityService.class));

                    //mWindowManager.updateViewLayout();

                   // Intent popupIntent = new Intent(appCtx, CustomizeDialogActivity.class);
                   // PendingIntent pi = PendingIntent.getActivity(appCtx, 0, popupIntent, PendingIntent.FLAG_ONE_SHOT);
                   // try{
                   //     pi.send();
                   // }
                   // catch(Exception e){
                   //     Toast.makeText(appCtx, e.toString(), Toast.LENGTH_LONG);
                   // }

                    break;

            }

            return true;
        }
    };


    @Override
    public void onCreate() {
        super.onCreate();

        appCtx = CustomMizeAppSelectFragment.customContext;

        mParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);


       // LayoutInflater mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
     //   mPopupView = mInflater.inflate(R.layout.activity_customize_dialog,null);

        // 텍스트뷰에 리스너 달고 팝업뷰에서는 버튼 클릭이벤트를 받아보자
        //mPopupView.setOnTouchListener(mViewTouchListener); //팝업뷰에 터치 리스너 등록

        DisplayMetrics displayMetrics = getApplicationContext().getResources().getDisplayMetrics();
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;
        toplayout = new RelativeLayout(this);
        RelativeLayout.LayoutParams topLayoutParams = new RelativeLayout.LayoutParams(width, height);
        toplayout.setLayoutParams(topLayoutParams);
        toplayout.setGravity(Gravity.CENTER);
        toplayout.setOnTouchListener(mViewTouchListener);

        textView = new TextView(this);
        textView.setTextSize(16);
        textView.setText("의수동작과 매칭할 화면버튼클릭");
        textView.setAlpha(0.5f);
        textView.setWidth(1200);
        textView.setHeight(188);
        textView.setTextColor(Color.parseColor("#EF8A25"));
        textView.setBackgroundColor(Color.parseColor("#4A2626"));
        textView.setGravity(Gravity.CENTER);


        toplayout.addView(textView);

        //textView.setGravity();

        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE); //윈도 매니저
        mWindowManager.addView(toplayout ,mParams);

    }

    public void handbuttonClick(View view) {
        // TODO: 2017. 4. 4. 여기에 다이얼로그 관련 코드 작성하면 됩니다
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (textView != null) {
            ((WindowManager) getSystemService(WINDOW_SERVICE)).removeView(toplayout);
            textView = null;
        }
    }

}
