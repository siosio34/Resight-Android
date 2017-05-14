package com.dragon4.owo.resight_android.service;

import android.app.Instrumentation;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dragon4.owo.resight_android.R;
import com.dragon4.owo.resight_android.view.Fragment.CustomMizeAppSelectFragment;

public class OnTopActivityService extends Service {

    private WindowManager.LayoutParams mParams;
    private WindowManager mWindowManager;
    private Context appCtx;

    private int count = 0;

    TextView textView;
    RelativeLayout toplayout;
    View mPopupView;
    private boolean touchFlag = false;

    private View mView;
    private int countNum = 0;

    private float START_X, START_Y;

    private OnTouchListener mViewTouchListener = new OnTouchListener() {
        @Override public boolean onTouch(View v, MotionEvent event) {
            switch(event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if(count == 6) {
                        appCtx.stopService(new Intent(appCtx,OnTopActivityService.class));
                    }
                    count++;

                    START_X = event.getRawX();
                    START_Y = event.getRawY();
                    Log.d("눌린좌표",START_X + " : "+ START_Y);
                    if(countNum == 0) {
                        //motionTest();
                        countNum++;
                    }
                  //  mWindowManager.removeView(toplayout);
                  //  mWindowManager.addView(mPopupView,mParams);

                    break;

            }

            return true;
        }
    };


    @Override
    public void onCreate() {
        super.onCreate();

        appCtx = CustomMizeAppSelectFragment.customContext;
        //motionTest();

        mParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY,
                WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                PixelFormat.TRANSLUCENT
        );


        // layout params
     //   mParams = new WindowManager.LayoutParams(
     //           WindowManager.LayoutParams.TYPE_PHONE,
     //           WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
     //           PixelFormat.TRANSLUCENT);
//
     //   // 팝업뷰
     //   LayoutInflater mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
     //   mPopupView = mInflater.inflate(R.layout.activity_customize_dialog,null);
//
     //   // 텍스트뷰에 리스너 달고 팝업뷰에서는 버튼 클릭이벤트를 받아보자
     //   //mPopupView.setOnTouchListener(mViewTouchListener); //팝업뷰에 터치 리스너 등록
//
     //   DisplayMetrics displayMetrics = getApplicationContext().getResources().getDisplayMetrics();
     //   int width = displayMetrics.widthPixels;
     //   int height = displayMetrics.heightPixels;
     //   toplayout = new RelativeLayout(this);
     //   RelativeLayout.LayoutParams topLayoutParams = new RelativeLayout.LayoutParams(width, height);
     //   toplayout.setLayoutParams(topLayoutParams);
     //   toplayout.setGravity(Gravity.CENTER);
     //   toplayout.setOnTouchListener(mViewTouchListener);
//
     //   textView = new TextView(this);
     //   textView.setTextSize(16);
     //   textView.setText("의수동작과 매칭할 화면버튼클릭");
     //   textView.setAlpha(0.5f);
     //   textView.setWidth(1200);
     //   textView.setHeight(188);
     //   textView.setTextColor(Color.parseColor("#EF8A25"));
     //   textView.setBackgroundColor(Color.parseColor("#4A2626"));
     //   textView.setGravity(Gravity.CENTER);
//
     //   toplayout.addView(textView);
//
          mView = new View(this);
      //   mView.setOnTouchListener(tempTouch);
          mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE); //윈도 매니저
          mView.setOnTouchListener(mViewTouchListener);
          mWindowManager.addView(mView,mParams);


      //  motionTest();
      //  motionTest();
     //   mWindowManager.addView(mPopupView,mParams);
       // mWindowManager.addView(toplayout ,mParams);

    }



    public void handbuttonClick(View view) {
        Log.d("클릭", "햇쪄요");
        switch (view.getId()) {
            case R.id.first_hand_button:
                Log.d("first_hand_button", "클릭");
                break;
            case R.id.second_hand_button:
                Log.d("second_hand_button", "클릭");
                break;
            case R.id.third_hand_button:
                Log.d("third_hand_button", "클릭");
                break;
        }
        mWindowManager.removeView(mPopupView);
        mWindowManager.addView(toplayout,mParams);

    }

    private void motionTest() {

        long downTime = SystemClock.uptimeMillis();
        long eventTime = SystemClock.uptimeMillis() + 100;
        float x = 77.69531f;
        float y = 615.625f;
        int metastate = 0;

        final MotionEvent motionEvent = MotionEvent.obtain(
                downTime,
                eventTime,
                MotionEvent.ACTION_DOWN,
                x,
                y,
                metastate
        );
        //((Activity)CustomMizeAppSelectFragment.customContext).getWindow().getDecorView().findViewById()dispatchTouchEvent(motionEvent);
        Log.d("여기", "클릭");
        new Thread() {
            public void run() {
                new Instrumentation().sendPointerSync(motionEvent);
            }
        }.start();

    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (toplayout != null) {
            ((WindowManager) getSystemService(WINDOW_SERVICE)).removeView(toplayout);
            toplayout = null;
        } else {
            ((WindowManager) getSystemService(WINDOW_SERVICE)).removeView(mPopupView);
            mPopupView = null;
        }
    }

}
