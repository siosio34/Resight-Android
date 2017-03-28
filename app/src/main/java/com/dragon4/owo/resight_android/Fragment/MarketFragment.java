package com.dragon4.owo.resight_android.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by joyeongje on 2017. 3. 19..
 */

public class MarketFragment extends Fragment {

    private GridView marketAppGridView;
    private List<MarketApp> marketApps;
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

        ViewGroup rootView = (ViewGroup) getActivity().getLayoutInflater().inflate(R.layout.fragment_market,container,false);
        marketApps = new ArrayList<>();
        marketApps.add(new MarketApp("https://play.google.com/store/apps/details?id=com.google.android.youtube","http://lh5.ggpht.com/jZ8XCjpCQWWZ5GLhbjRAufsw3JXePHUJVfEvMH3D055ghq0dyiSP3YxfSc_czPhtCLSO=w300-rw","youtube"));
        marketAppGridView = (GridView) rootView.findViewById(R.id.market_grideview);
        marketAppGridView.setAdapter(new MarketGridViewAdpater());

        return rootView;
    }


    private class MarketGridViewAdpater extends BaseAdapter {
        LayoutInflater inflater;

        private MarketGridViewAdpater() {
            inflater = (LayoutInflater) marketContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        }
        @Override
        public int getCount() {
            return marketApps.size();
        }

        @Override
        public Object getItem(int position) {
            return marketApps.get(position);
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
            final MarketApp info = marketApps.get(position);
            ImageView appImageView = (ImageView) convertView.findViewById(R.id.market_item_imageView);
            Picasso.with(marketContext).load(info.getAppUrlImage()).into(appImageView);
            appImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 웹 페이지 연결되게
                    String url = info.getAppMarketurl();
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                    marketContext.startActivity(intent);
                }
            });
            TextView appTextView = (TextView) convertView.findViewById(R.id.market_item_textView);
            appTextView.setText(info.appText);

            http://lh5.ggpht.com/jZ8XCjpCQWWZ5GLhbjRAufsw3JXePHUJVfEvMH3D055ghq0dyiSP3YxfSc_czPhtCLSO=w300-rw
            return convertView;
        }
    }

    private class MarketApp {
        private String appMarketurl;
        private String appUrlImage;
        private String appText;

        public MarketApp(String appMarketurl, String appUrlImage, String appText) {
            this.appMarketurl = appMarketurl;
            this.appUrlImage = appUrlImage;
            this.appText = appText;
        }


        public String getAppMarketurl() {
            return appMarketurl;
        }

        public String getAppUrlImage() {
            return appUrlImage;
        }

        public String getAppText() {
            return appText;
        }
    }


}
