package com.dragon4.owo.resight_android.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.dragon4.owo.resight_android.model.HandMotion;
import com.dragon4.owo.resight_android.R;
import com.dragon4.owo.resight_android.view.Activity.HandMotionSelectActivity;
import com.dragon4.owo.resight_android.util.MessageEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

/**
 * Created by young on 2017-05-02.
 */

public class HandMotionAdapter extends RecyclerView.Adapter<HandMotionAdapter.ViewHolder> {

    private ArrayList<HandMotion> mDataset;
    private Context handMotionCtx;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView  handMotionTextView;
        public ImageView handMotionImageView;
        public Button    playButton;
        public Button    selectButton;

        public ViewHolder(View view) {
            super(view);
            handMotionTextView = (TextView) view.findViewById(R.id.hand_text);
            handMotionImageView = (ImageView) view.findViewById(R.id.hand_image);
            playButton = (Button) view.findViewById(R.id.hand_start_button);
            selectButton = (Button) view.findViewById(R.id.hand_select_button);
        }

    }

    public HandMotionAdapter(ArrayList<HandMotion> myDataset) {
        mDataset = myDataset;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        handMotionCtx = parent.getContext();
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.hand_motion_card_view, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        holder.handMotionTextView.setText(mDataset.get(position).handMotionTexgt);
        holder.handMotionImageView.setImageResource(mDataset.get(position).handImg);

        holder.playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 플레이하는 동작이 들어가야됨.

            }
        });

        holder.selectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 동작이 선택되고 액티비티 나가는 동작이 들어가야됨.
                Intent selectHandMotionIntent = new Intent();
                // TODO: 2017-05-03 손 선택한거 동작 처리마저해야됨
                selectHandMotionIntent.putExtra("handCode",mDataset.get(position).handCode);
                selectHandMotionIntent.putExtra("handImage",mDataset.get(position).handImg);
                EventBus.getDefault().post(new MessageEvent("Hello everyone!"));
                ((HandMotionSelectActivity)handMotionCtx).setResult(RESULT_OK,selectHandMotionIntent);
                ((HandMotionSelectActivity)handMotionCtx).finish();


            }
        });

    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

}






