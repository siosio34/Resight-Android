package com.dragon4.owo.resight_android.Fragment;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
 * Created by joyeongje on 2017. 3. 19..
 */

public class MarketFragment extends Fragment {

    private GridView marketAppGridView;
    private List<ResolveInfo> appsInfo;
    private PackageManager packageManager;
    private Context marketContext;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        marketContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        packageManager = marketContext.getPackageManager();
        appsInfo = packageManager.queryIntentActivities(intent,0);

        ViewGroup rootView = (ViewGroup) getActivity().getLayoutInflater().inflate(R.layout.fragment_market,container,false);
        marketAppGridView = (GridView) rootView.findViewById(R.id.market_grideview);
        marketAppGridView.setAdapter(new MarketGridViewAdpater());

        return rootView;
    }


    public class MarketGridViewAdpater extends BaseAdapter {
        LayoutInflater inflater;

        public MarketGridViewAdpater() {
            inflater = (LayoutInflater) marketContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

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
                convertView = inflater.inflate(R.layout.market_gridview_item, parent, false);
            }
            final ResolveInfo info = appsInfo.get(position);
            ImageView appImageView = (ImageView) convertView.findViewById(R.id.market_item_imageView);
            appImageView.setBackground(info.activityInfo.loadIcon(packageManager));
            appImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_RUN);
                    intent.setComponent(new ComponentName(info.activityInfo.packageName,info.activityInfo.name));
                    marketContext.startActivity(intent);
                }
            });
            TextView appTextView = (TextView) convertView.findViewById(R.id.market_item_textView);
            appTextView.setText(info.activityInfo.loadLabel(packageManager));
            return convertView;
        }
    }
}
