package com.dragon4.owo.resight_android.view.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.dragon4.owo.resight_android.model.AppStore;
import com.dragon4.owo.resight_android.R;
import com.dragon4.owo.resight_android.network.AppStoreClient;
import com.dragon4.owo.resight_android.network.ServiceGenerator;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by joyeongje on 2017. 3. 19..
 */

public class MarketFragment extends Fragment {

    private GridView marketAppGridView;
    private List<MarketApp> marketApps;
    private PackageManager packageManager;
    private Context marketContext;
    private AppStoreClient client;

    private static final String TAG = "MarketFragment";


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

        client = ServiceGenerator.createService(AppStoreClient.class);

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

            // 스토어 앱 정보 가져오기
            Call<List<AppStore>> listCall = client.getAppList();
            listCall.enqueue(new Callback<List<AppStore>>() {
                @Override
                public void onResponse(Call<List<AppStore>> call, Response<List<AppStore>> response) {
                    Log.d(TAG,response.body().toString());
                    Log.d(TAG,"Success AppStoreData to Server");
                }

                @Override
                public void onFailure(Call<List<AppStore>> call, Throwable t) {
                    Log.d(TAG,"Get AppStoreData from Server");
                }
            });

            // 스토어에 앱 업로드 하기
            Call<AppStore> uploadAppCall = client.uploadApp(new AppStore(1,"1","2","3"));
            uploadAppCall.enqueue(new Callback<AppStore>() {
                @Override
                public void onResponse(Call<AppStore> call, Response<AppStore> response) {

                    Log.d(TAG,response.body().toString());
                    Log.d(TAG,"Success AppStoreData to Server");
                }

                @Override
                public void onFailure(Call<AppStore> call, Throwable t) {
                    Log.d(TAG,"Post AppStoreData to Server");
                }
            });


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

           // http://lh5.ggpht.com/jZ8XCjpCQWWZ5GLhbjRAufsw3JXePHUJVfEvMH3D055ghq0dyiSP3YxfSc_czPhtCLSO=w300-rw
            return convertView;
        }
    }

    private class MarketApp {
        private String appText;
        private String appMarketurl;
        private String appUrlImage;


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
