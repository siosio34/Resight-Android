package com.dragon4.owo.resight_android.view.Fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import com.dragon4.owo.resight_android.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by joyeongje on 2017. 3. 19..
 */

public class HandMotionFragment extends Fragment {

    private GridView handMotionGridView;
    private List<HandMotion> handMotions;

    private Context handMotionContext;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        handMotionContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) getActivity().getLayoutInflater().inflate(R.layout.fragment_hand_motion,container,false);
        initializeHandMotion();
        handMotionGridView = (GridView) rootView.findViewById(R.id.hand_motion_gridView);
        handMotionGridView.setAdapter(new HandMotionGridViewAdpater());

        return rootView;
    }

    private class HandMotion {

        private Bitmap handMotionImage;
        private String handMotionName;

        public HandMotion(Bitmap handMotionImage, String handMotionName) {
            this.handMotionImage = handMotionImage;
            this.handMotionName = handMotionName;
        }

        public String getHandMotionName() {
            return handMotionName;
        }

        public Bitmap getHandMotionImage() {
            return handMotionImage;
        }
    }

    private void initializeHandMotion() {

        handMotions = new ArrayList<>();

        handMotions.add(new HandMotion(BitmapFactory.decodeResource(handMotionContext.getResources(),
                R.drawable.icon_hand00_open),"손 펴기"));

        handMotions.add(new HandMotion(BitmapFactory.decodeResource(handMotionContext.getResources(),
                R.drawable.icon_hand01_closed),"주먹 쥐기"));

        handMotions.add(new HandMotion(BitmapFactory.decodeResource(handMotionContext.getResources(),
                R.drawable.icon_hand02_finger_closed),"모든 손굽히기"));

        handMotions.add(new HandMotion(BitmapFactory.decodeResource(handMotionContext.getResources(),
                R.drawable.icon_hand03),"손 오므리기"));

        handMotions.add(new HandMotion(BitmapFactory.decodeResource(handMotionContext.getResources(),
                R.drawable.icon_hand04),"새끼 손 펴기"));

        handMotions.add(new HandMotion(BitmapFactory.decodeResource(handMotionContext.getResources(),
                R.drawable.icon_hand05),"검지 올리기"));

        handMotions.add(new HandMotion(BitmapFactory.decodeResource(handMotionContext.getResources(),
                R.drawable.icon_hand06),"집게 손펴기"));

        handMotions.add(new HandMotion(BitmapFactory.decodeResource(handMotionContext.getResources(),
                R.drawable.icon_hand07),"약지에 닿기"));

        handMotions.add(new HandMotion(BitmapFactory.decodeResource(handMotionContext.getResources(),
                R.drawable.icon_hand08),"브이하기"));

    }

    public class HandMotionGridViewAdpater extends BaseAdapter {
        LayoutInflater inflater;

        public HandMotionGridViewAdpater() {
            inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        @Override
        public int getCount() {
            return handMotions.size();
        }

        @Override
        public Object getItem(int position) {
            return handMotions.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.hand_motion_gridview_item, parent, false);
            }

            Log.d("핸드모션 리스트",String.valueOf(handMotions.size()));
            final HandMotion handMotionInfo = handMotions.get(position);

            ImageView handImageView = (ImageView) convertView.findViewById(R.id.hand_motion_imageView);
            handImageView.setImageBitmap(handMotionInfo.getHandMotionImage());
            TextView handTextView = (TextView) convertView.findViewById(R.id.hand_motion_textView);
            handTextView.setText(handMotionInfo.getHandMotionName());

            return convertView;
        }
    }




}
