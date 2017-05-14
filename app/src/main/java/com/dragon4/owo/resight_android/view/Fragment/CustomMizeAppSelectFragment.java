package com.dragon4.owo.resight_android.view.Fragment;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.dragon4.owo.resight_android.R;

import java.util.List;

/**
 * Created by joyeongje on 2017. 3. 22..
 */

public class CustomMizeAppSelectFragment extends Fragment {

    private GridView customAppGridView;
    private List<ResolveInfo> appsInfo;
    private PackageManager packageManager;
    public static Context customContext;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        customContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        getApplicaitonList();

        ViewGroup rootView = (ViewGroup) getActivity().getLayoutInflater().inflate(R.layout.fragment_customize_app_select,container,false);
        rootView.setOnTouchListener(Touchest);
        customAppGridView = (GridView) rootView.findViewById(R.id.custom_grideview);
        customAppGridView.setAdapter(new CustomGridViewAdpater());

        return rootView;
    }

    private View.OnTouchListener Touchest = new View.OnTouchListener(){

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            Log.d("클릭","클릭클릭");
            float START_X, START_Y;
            START_X = event.getRawX();
            START_Y = event.getRawY();
            Log.d("눌린좌표",START_X + " : "+ START_Y);
            return false;
        }
    };

    private void getApplicaitonList() {
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        packageManager = customContext.getPackageManager();
        appsInfo = packageManager.queryIntentActivities(intent,0);
    }


    private class CustomGridViewAdpater extends BaseAdapter {
        LayoutInflater inflater;

        private CustomGridViewAdpater() {
            inflater = (LayoutInflater) customContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return appsInfo.size();
        }

        @Override
        public Object getItem(int position) {
            return appsInfo.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.customize_gridview_item, parent, false);
            }
            final ResolveInfo info = appsInfo.get(position);
            ImageView appImageView = (ImageView) convertView.findViewById(R.id.custom_category_item_imageView);
            appImageView.setBackground(info.activityInfo.loadIcon(packageManager));
            appImageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            // 이거활성화시 좌표 받아올수 있다
                            Intent intent = new Intent(Intent.ACTION_RUN);
                            intent.setComponent(new ComponentName(info.activityInfo.packageName,info.activityInfo.name));
                            customContext.startActivity(intent);

                            ActivityManager activityManager = (ActivityManager)customContext.getSystemService(customContext.ACTIVITY_SERVICE);
                            List<ActivityManager.RunningTaskInfo> info;
                            info = activityManager.getRunningTasks(1);
                            ActivityManager.RunningTaskInfo runningTaskInfo = info.get(0);
                            String curActivityName = runningTaskInfo.topActivity.getClassName();
                            Log.d("탑 액티비티", curActivityName);
                   // Intent serviceIntent = new Intent(customContext,OnTopActivityService.class);
                   // customContext.startService(serviceIntent);
                  //  customContext.stopService(serviceIntent);


                   // ConstraintLayout constraintLayout = new ConstraintLayout(this);

                   // LayoutInflater mInflater = (LayoutInflater) customContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                   // mInflater.inflate(R.layout.activity_customize_dialog,null);

                }
            });
            TextView appTextView = (TextView) convertView.findViewById(R.id.custom_category_item_textView);
            appTextView.setText(info.activityInfo.loadLabel(packageManager));
            return convertView;
        }



    }



}
