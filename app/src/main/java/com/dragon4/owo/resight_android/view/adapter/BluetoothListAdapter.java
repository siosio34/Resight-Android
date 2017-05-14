package com.dragon4.owo.resight_android.view.adapter;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dragon4.owo.resight_android.R;

import java.util.ArrayList;

/**
 * Created by joyeongje on 2017. 3. 18..
 */

public class BluetoothListAdapter extends BaseAdapter {

    private ArrayList<BluetoothDevice> bloothDeviceList = new ArrayList<BluetoothDevice>() ;

    @Override
    public int getCount() {
        return bloothDeviceList.size();
    }

    @Override
    public Object getItem(int position) {
        return bloothDeviceList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final Context context = parent.getContext();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.blooth_listview_item, parent, false);
        }

        TextView bloothNameTextView = (TextView) convertView.findViewById(R.id.blooth_item_textView);
        BluetoothDevice bluetoothDevice = bloothDeviceList.get(position);
        bloothNameTextView.setText(bluetoothDevice.getName() + ":" + bluetoothDevice.getAddress());

        return convertView;
    }

    public void addBloothDeviceToList(BluetoothDevice bluetoothDevice) {
        bloothDeviceList.add(bluetoothDevice);
    }

}
