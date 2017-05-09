package com.dragon4.owo.resight_android.View.Activity;

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
import com.dragon4.owo.resight_android.View.adapter.BluetoothListAdapter;
import com.tsengvn.typekit.TypekitContextWrapper;
import com.zcw.togglebutton.ToggleButton;

import java.util.Set;


public class BluetoothSearchActivity extends AppCompatActivity {

    private static final String TAG = "BluetoothSearchActivity";

    public static final String EXTRA_DEVICE_ADDRESS = "device_address";
    public static final String EXTRA_DEVICE_NAME="device_name";

    private final BroadcastReceiver bloothReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice bluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (bluetoothDevice.getBondState() != BluetoothDevice.BOND_BONDED) {
                    mNewDevicesArrayAdapter.addBloothDeviceToList(bluetoothDevice);
                    mNewDevicesArrayAdapter.notifyDataSetChanged();
//                    Log.d("새로운 기기목록.", bluetoothDevice.getName());
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
    private BluetoothListAdapter mNewDevicesArrayAdapter;
    private BluetoothListAdapter mBondedDevicesArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blooth_search);

        initBluetooth(); // 블루투스 사용가능 여부가져옴.
        CheckBluetoothToggleButton(); // 토글 버튼
        initBluetoothDeviceList();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bluetoothAdapter != null) {
            bluetoothAdapter.cancelDiscovery();
        }
        this.unregisterReceiver(bloothReceiver);
    }

    private void initBluetooth() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            Toast.makeText(this, "이 기종은 블루투스를 지원하지 않아 Resight와 사용할 수 없습니다.", Toast.LENGTH_LONG).show();
            finish();
        }
        isOnBlooth = bluetoothAdapter.isEnabled();

        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        this.registerReceiver(bloothReceiver, filter);
      filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        this.registerReceiver(bloothReceiver, filter);
    }

    private void CheckBluetoothToggleButton() {
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
            setOnBlooth();
        } else {
            setOffBluetooth();
        }
    }

    private void setOnBlooth() {
        checkBloothButton.setToggleOn();
        initBluetooth();
        if (bluetoothAdapter != null) {
            bluetoothAdapter.enable();
            bluetoothAdapter.startDiscovery();
        }
    }

    private void setOffBluetooth() {
        checkBloothButton.setToggleOff();
        bluetoothAdapter.disable();

    }

    private void initBluetoothDeviceList() {

        mBondedDevicesArrayAdapter = new BluetoothListAdapter();
        mNewDevicesArrayAdapter = new BluetoothListAdapter();

        ListView bondedDevicesListView = (ListView) findViewById(R.id.blooth_list);
        bondedDevicesListView.setAdapter(mBondedDevicesArrayAdapter);
        bondedDevicesListView.setOnItemClickListener(mDeviceClickListener);

        ListView newDevicesListView = (ListView) findViewById(R.id.new_bluetooth_list);
        newDevicesListView.setAdapter(mNewDevicesArrayAdapter);
        newDevicesListView.setOnItemClickListener(mDeviceClickListener);

        requestRecentParingBluetoothDevice();
        requestBluetoothDeviceList();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    private void requestRecentParingBluetoothDevice() {
        TextView recentParingTextView = (TextView) findViewById(R.id.recent_paring_device_textView);
        Set<BluetoothDevice> pairededDevices = bluetoothAdapter.getBondedDevices();
        int pairingDeviceSize = pairededDevices.size();

        if (pairingDeviceSize > 0) {
            for (BluetoothDevice bluetoothDevice: pairededDevices) {
                if(bluetoothDevice.getName().contains("RESIGHT") || bluetoothDevice.getName().contains("GOLFIT")) {
                    mBondedDevicesArrayAdapter.addBloothDeviceToList(bluetoothDevice);
                    mBondedDevicesArrayAdapter.notifyDataSetChanged();
                    Log.d("페어링된 기기목록...", bluetoothDevice.getName());
                    recentParingBloothDevice = bluetoothDevice;
                }
            }
        }
        else {
            recentParingTextView.setText("최근 페어링 : 없음");
        }

        recentParingTextView.setText("최근 페어링 : " + recentParingBloothDevice.getName());
        recentParingTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create the result Intent and include the MAC address
                Intent intent = new Intent();
                intent.putExtra(EXTRA_DEVICE_ADDRESS, recentParingBloothDevice.getAddress());
                intent.putExtra(EXTRA_DEVICE_NAME,recentParingBloothDevice.getName());

                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
    }

    private void requestBluetoothDeviceList() {
        if (bluetoothAdapter.isDiscovering()) {
            bluetoothAdapter.cancelDiscovery();
        }
        bluetoothAdapter.startDiscovery();
    }

    // The on-click listener for all devices in the ListViews
    private AdapterView.OnItemClickListener mDeviceClickListener = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView parent, View v, int position, long id) {
            // Cancel discovery because it's costly and we're about to connect
            bluetoothAdapter.cancelDiscovery();

            // Get the device MAC address, which is the last 17 chars in the View
            BluetoothDevice selecedtedBloothDevice =  (BluetoothDevice)parent.getItemAtPosition(position);
            String address = selecedtedBloothDevice.getAddress();
            String name = selecedtedBloothDevice.getName();

            // Get the device MAC address, which is the last 17 chars in the View
            // TODO: 2017-04-27 이거 디버깅 나중에 해보기 // TODO: 2017-04-27 위에 코드보단 효율적으로보이는데.
            // String info = ((TextView) v).getText().toString();
            // String address = info.substring(info.length() - 17);
            // String name = info.substring(0, info.length()-18);

            // Create the result Intent and include the MAC address
            Intent intent = new Intent();
            intent.putExtra(EXTRA_DEVICE_ADDRESS, address);
            intent.putExtra(EXTRA_DEVICE_NAME,name);

            Log.d("address", address);
            setResult(Activity.RESULT_OK, intent);
            finish();
        }
    };



}
