package com.dragon4.owo.resight_android.service;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.dragon4.owo.resight_android.Activity.CustomizeDialogActivity;

public class OnTopActivityService extends Service {

    private WindowManager.LayoutParams mParams;		//layout params ∞¥√º. ∫‰¿« ¿ßƒ° π◊ ≈©±‚∏¶ ¡ˆ¡§«œ¥¬ ∞¥√º
    private WindowManager mWindowManager;
    private Context appCtx;


    private float START_X, START_Y;							//øÚ¡˜¿Ã±‚ ¿ß«ÿ ≈Õƒ°«— Ω√¿€ ¡°
    private int PREV_X, PREV_Y;								//øÚ¡˜¿Ã±‚ ¿Ã¿¸ø° ∫‰∞° ¿ßƒ°«— ¡°
    private int MAX_X = -1, MAX_Y = -1;					//∫‰¿« ¿ßƒ° √÷¥Î ∞™

    private OnTouchListener mViewTouchListener = new OnTouchListener() {
        @Override public boolean onTouch(View v, MotionEvent event) {
            switch(event.getAction()) {
                case MotionEvent.ACTION_DOWN:				//ªÁøÎ¿⁄ ≈Õƒ° ¥ŸøÓ¿Ã∏È
                    START_X = event.getRawX();					//≈Õƒ° Ω√¿€ ¡°
                    START_Y = event.getRawY();					//≈Õƒ° Ω√¿€ ¡°
                    PREV_X = mParams.x;							//∫‰¿« Ω√¿€ ¡°
                    PREV_Y = mParams.y;							//∫‰¿« Ω√¿€ ¡°

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
                case MotionEvent.ACTION_MOVE:
                    int x = (int)(event.getRawX() - START_X);	//¿Ãµø«— ∞≈∏Æ
                    int y = (int)(event.getRawY() - START_Y);	//¿Ãµø«— ∞≈∏Æ

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
        mParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        TextView mPopupView = new TextView(this);
        mPopupView.setText("이 뷰는 항상 위에 있다.");

        mPopupView.setOnTouchListener(mViewTouchListener);              //팝업뷰에 터치 리스너 등록

        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE); //윈도 매니저
        mWindowManager.addView(mPopupView ,mParams);

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
