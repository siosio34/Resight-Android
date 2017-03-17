package com.dragon4.owo.resight_android.Activity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.dragon4.owo.resight_android.R;

import java.util.Set;

public class BloothSearchActivity extends AppCompatActivity {

    private static final String TAG = "BloothSearchActivity";
    //private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    // 스마트폰끼리 블루투스 프로토콜 http://dsnight.tistory.com/13
    public static final String EXTRA_DEVICE_ADDRESS = "device_address";



    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (BluetoothDevice.ACTION_FOUND.equals(action)) {

                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                    if (device.getName().contains("RESIGHT")) {
                        sendBloothDeviceAddress(device);
                    }
                }

            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) { // 검색이 끝났을때!

                // TODO: 2017. 3. 5. 끝내야하나. 재시도해야하나 결정.
                Toast.makeText(getApplicationContext(), "RESIGHT 기기가 존재하지않습니다.", Toast.LENGTH_LONG).show();
            }
        }
    };

    private BluetoothAdapter bluetoothAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blooth_search);

        //initBlooth();
        //requestOnBlooth();
//
        //// 먼저 페어링된 기기에서 리사이트를 찾음.
        //BluetoothDevice resightDevice = getRESightBloothDeviceFromBondedList();
        //if (resightDevice != null) { // 페어링된 기기와 바로 연결.
        //    sendBloothDeviceAddress(resightDevice);
        //} else {
        //    startBloothScanRecevier();
        //}
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (bluetoothAdapter != null) {
            bluetoothAdapter.cancelDiscovery();
        }

        this.unregisterReceiver(mReceiver);
    }

    private void initBlooth() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            Toast.makeText(this, "이 기종은 블루투스를 지원하지 않아 Resight와 사용할 수 없습니다.", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private void requestOnBlooth() {
        if (!bluetoothAdapter.isEnabled()) {
            bluetoothAdapter.enable(); // 블루투스 바로 연결.
        } else {
            Log.d(TAG, "already on Blooth");
        }
    }

    private BluetoothDevice getRESightBloothDeviceFromBondedList() {

        // TODO: 2017. 3. 5.  // 나중엔 디바이스 이름중에 네오팩트인거의 맥주소와 연결한다
        Set<BluetoothDevice> bondedDevices = bluetoothAdapter.getBondedDevices();
        int pairingDeviceSize = bondedDevices.size();

        BluetoothDevice resightDevice;

        if (pairingDeviceSize > 0) {
            resightDevice = getResightDeviceFromDeviceList(bondedDevices);
            if (resightDevice != null)
                return resightDevice;
        }
        return null;
    }

    private BluetoothDevice getResightDeviceFromDeviceList(Set<BluetoothDevice> bondedDevices) {

        BluetoothDevice selectedDevice = null;
        for (BluetoothDevice device : bondedDevices) {
            if (device.getName().contains("RESIGHT")) {
                selectedDevice = device;
                break;
            }
        }
        return selectedDevice;
    }

    private void sendBloothDeviceAddress(BluetoothDevice resightDevice) {


        // TODO: 2017. 3. 5. 블루투스 연결 완료 됬다는거랑 프로그레스바 텍스트뷰 업데이트.
        // 1초뒤 이동

        String address = resightDevice.getAddress();

        Intent intent = new Intent();
        intent.putExtra(EXTRA_DEVICE_ADDRESS, address);
        finish();
    }

    private void startBloothScanRecevier() {

        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        this.registerReceiver(mReceiver, filter);

        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        this.registerReceiver(mReceiver, filter);

        if (bluetoothAdapter.isDiscovering()) {
            bluetoothAdapter.cancelDiscovery();
        }

        bluetoothAdapter.startDiscovery();

    }
}
