package com.dragon4.owo.resight_android.Fragment;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
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
    private Context customContext;

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
        customAppGridView = (GridView) rootView.findViewById(R.id.custom_grideview);
        customAppGridView.setAdapter(new CustomGridViewAdpater());

        return rootView;
    }

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
                    //customContext.startService(new Intent(customContext,OnTopActivityService.class));
                    // 이거활성화시 좌표 받아올수 있다
                    Intent intent = new Intent(Intent.ACTION_RUN);
                    intent.setComponent(new ComponentName(info.activityInfo.packageName,info.activityInfo.name));
                    customContext.startActivity(intent);
                }
            });
            TextView appTextView = (TextView) convertView.findViewById(R.id.custom_category_item_textView);
            appTextView.setText(info.activityInfo.loadLabel(packageManager));
            return convertView;
        }



    }



}
