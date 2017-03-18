package com.dragon4.owo.resight_android.Activity;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dragon4.owo.resight_android.R;
import com.dragon4.owo.resight_android.adapter.BluetoothListAdapter;
import com.zcw.togglebutton.ToggleButton;

import java.util.Set;

public class BluetoothSearchActivity extends AppCompatActivity {

    private static final String TAG = "BluetoothSearchActivity";
    //private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    // 스마트폰끼리 블루투스 프로토콜 http://dsnight.tistory.com/13
    public static final String EXTRA_DEVICE_ADDRESS = "device_address";

    private final BroadcastReceiver bloothReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice bloothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (bloothDevice.getBondState() != BluetoothDevice.BOND_BONDED) {
                    mNewDevicesArrayAdapter.addBloothDeviceToList(bloothDevice);
                    mNewDevicesArrayAdapter.notifyDataSetChanged();
                }

            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) { // 검색이 끝났을때!

                Toast.makeText(getApplicationContext(), "기기 검색 완료.", Toast.LENGTH_LONG).show();
            }
        }
    };

    private BluetoothAdapter bluetoothAdapter;
    private boolean isOnBlooth;
    private ToggleButton checkBloothButton;

    private BluetoothDevice recentParingBloothDevice;
    private TextView recentParingTextView;

    private BluetoothListAdapter mNewDevicesArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blooth_search);


        initBlooth();
        registCheckBloothButton();
        requestRecentParingBloothDevice();

        mNewDevicesArrayAdapter = new BluetoothListAdapter();
        ListView newDevicesListView = (ListView) findViewById(R.id.blooth_list);
        newDevicesListView.setAdapter(mNewDevicesArrayAdapter);
        newDevicesListView.setOnItemClickListener(mDeviceClickListener);

        requestBloothDeviceList();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (bluetoothAdapter != null) {
            bluetoothAdapter.cancelDiscovery();
        }
        this.unregisterReceiver(bloothReceiver);
    }

    // The on-click listener for all devices in the ListViews
    private AdapterView.OnItemClickListener mDeviceClickListener = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView parent, View v, int position, long id) {
            // Cancel discovery because it's costly and we're about to connect
            bluetoothAdapter.cancelDiscovery();

            // Get the device MAC address, which is the last 17 chars in the View
            BluetoothDevice selecedtedBloothDevice =  (BluetoothDevice)parent.getItemAtPosition(position);
            String address = selecedtedBloothDevice.getAddress();

            // Create the result Intent and include the MAC address
            Intent intent = new Intent();
            intent.putExtra(EXTRA_DEVICE_ADDRESS, address);

            Log.d("address", address);
            setResult(Activity.RESULT_OK, intent);
            finish();
        }
    };

    private void initBlooth() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            Toast.makeText(this, "이 기종은 블루투스를 지원하지 않아 Resight와 사용할 수 없습니다.", Toast.LENGTH_LONG).show();
            finish();
        }
        isOnBlooth = bluetoothAdapter.isEnabled();
    }

    private void registCheckBloothButton() {
        checkBloothButton = (ToggleButton) findViewById(R.id.check_blooth_button);
        viewToggleButtonState();
        checkBloothButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isOnBlooth = !isOnBlooth;
                viewToggleButtonState();
            }
        });
    }

    private void viewToggleButtonState() {
        if(isOnBlooth) {
            checkBloothButton.setToggleOn();
            bluetoothAdapter.enable();
        } else {
            checkBloothButton.setToggleOff();
            bluetoothAdapter.disable();
        }
    }

    private void requestRecentParingBloothDevice() {
        recentParingTextView = (TextView)findViewById(R.id.recent_paring_device_textView);
        Set<BluetoothDevice> pairededDevices = bluetoothAdapter.getBondedDevices();
        int pairingDeviceSize = pairededDevices.size();

        if (pairingDeviceSize > 0) {
            for (BluetoothDevice bloothDevice: pairededDevices) {
                recentParingBloothDevice = bloothDevice;
            }
            recentParingTextView.setText("최근 페어링 : " + recentParingBloothDevice.getName());
        }
        else {
            recentParingTextView.setText("최근 페어링 : 없음");
        }
    }

    private void requestBloothDeviceList() {

        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        this.registerReceiver(bloothReceiver, filter);

        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        this.registerReceiver(bloothReceiver, filter);

        if (bluetoothAdapter.isDiscovering()) {
            bluetoothAdapter.cancelDiscovery();
        }
        bluetoothAdapter.startDiscovery();
    }
}
