package com.dragon4.owo.resight_android.Activity;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.dragon4.owo.resight_android.Blooth.BluetoothCommunication;
import com.dragon4.owo.resight_android.Fragment.CustomMizeFragment;
import com.dragon4.owo.resight_android.Fragment.HandMotionFragment;
import com.dragon4.owo.resight_android.Fragment.MarketFragment;
import com.dragon4.owo.resight_android.Fragment.MonitoringFragment;
import com.dragon4.owo.resight_android.R;
import com.tsengvn.typekit.TypekitContextWrapper;

public class ReSightMainActivity extends AppCompatActivity implements BottomNavigationBar.OnTabSelectedListener {

    private static final int REQUEST_CONNECT_DEVICE = 8009;

    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;

    public static final String TOAST = "toast";
    public static final String DEVICE_NAME = "device_name";
    private static final String TAG = "ReSightMainActivity";

    int lastSelectedPosition = 0;
    BottomNavigationBar bottomNavigationBar;

    private String mConnectedDeviceName = null;
    private BluetoothAdapter mBluetoothAdapter = null;
    private BluetoothCommunication mChatService = null;
    private StringBuffer mOutStringBuffer;

    private MonitoringFragment monitoringFragment;
    private HandMotionFragment handMotionFragment;
    private CustomMizeFragment customizeFragment;
    private MarketFragment     marketFragment;
    Toolbar toolbar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resight_main);

        initToolbar();
        initBottomNavigationBar();
        initFragment();

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // If the adapter is null, then Bluetooth is not supported
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        mChatService = new BluetoothCommunication(this, mHandler);
        mOutStringBuffer = new StringBuffer("");

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void initBottomNavigationBar() {
        bottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottom_navigation_bar);
        bottomNavigationBar
                .addItem(new BottomNavigationItem(R.drawable.icon_monitor, "모니터링").setActiveColorResource(R.color.colorBottomNavigation))
                .addItem(new BottomNavigationItem(R.drawable.icon_hand, "손 동작").setActiveColorResource(R.color.colorBottomNavigation))
                .addItem(new BottomNavigationItem(R.drawable.icon_setting, "커스터마이징").setActiveColorResource(R.color.colorBottomNavigation))
                .addItem(new BottomNavigationItem(R.drawable.icon_market, "마켓").setActiveColorResource(R.color.colorBottomNavigation))
                .setFirstSelectedPosition(lastSelectedPosition > 3 ? 3 : lastSelectedPosition)
                .initialise();
        bottomNavigationBar.setTabSelectedListener(this);
    }

    private void initFragment() {

        monitoringFragment = new MonitoringFragment();
        handMotionFragment = new HandMotionFragment();
        customizeFragment = new CustomMizeFragment();
        marketFragment = new MarketFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.container,monitoringFragment).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_resight_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_blooth) {
            Intent bloothMoveIntent = new Intent(ReSightMainActivity.this, BluetoothSearchActivity.class);
            startActivityForResult(bloothMoveIntent, REQUEST_CONNECT_DEVICE);
            return true;
        } else if(id == R.id.action_settings) {
            return true; //
        }

        return super.onOptionsItemSelected(item);
    }

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Log.d(TAG,String.valueOf(msg.what));
            switch (msg.what) {
                case MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case BluetoothCommunication.STATE_CONNECTED:
                            break;
                        case BluetoothCommunication.STATE_CONNECTING:
                            break;
                        case BluetoothCommunication.STATE_LISTEN:
                        case BluetoothCommunication.STATE_NONE:
                            break;
                    }
                    break;
                case MESSAGE_WRITE:
                    byte[] writeBuf = (byte[]) msg.obj;
                    String writeMessage = new String(writeBuf);
                    Log.d(TAG,writeMessage);
                    break;
                case MESSAGE_READ:
                    byte[] readBuf = (byte[]) msg.obj;
                    String readMessage = new String(readBuf, 0, msg.arg1);
                    Log.d(TAG,readMessage);
                    break;
                case MESSAGE_DEVICE_NAME:
                    // save the connected device's name
                    mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
                    Log.d(TAG,mConnectedDeviceName);
                    Toast.makeText(getApplicationContext(), "Connected to "
                            + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
                    break;
                case MESSAGE_TOAST:
                    Toast.makeText(getApplicationContext(), msg.getData().getString(TOAST),
                            Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    private void sendMessage(String message) {
        // Check that we're actually connected before trying anything
        if (mChatService.getState() != BluetoothCommunication.STATE_CONNECTED) {
            Toast.makeText(this, "블루투스가 연결되어 있지 않습니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check that there's actually something to send
        if (message.length() > 0) {
            // Get the message bytes and tell the BluetoothChatService to write
            byte[] send = message.getBytes();
            mChatService.write(send);
            Log.d("여기에들어옴","test");

            // Reset out string buffer to zero and clear the edit text field
            mOutStringBuffer.setLength(0);
            //     mOutEditText.setText(mOutStringBuffer);
        }
    }

    private void sendMessage2(byte[] message) {
        // Check that we're actually connected before trying anything
        if (mChatService.getState() != BluetoothCommunication.STATE_CONNECTED) {
            Toast.makeText(this, "블루투스가 연결되어 있지 않습니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check that there's actually something to send
        if (message.length > 0) {
            // Get the message bytes and tell the BluetoothChatService to write
            mChatService.write(message);

            // Reset out string buffer to zero and clear the edit text field
            mOutStringBuffer.setLength(0);
            //     mOutEditText.setText(mOutStringBuffer);
        }
    }


    // Bottom Navigation Activity


    @Override
    public void onTabSelected(int position) {
        Fragment currentSelectedFragment = null;
        switch (position) {
            case 0:
                currentSelectedFragment = monitoringFragment;
                byte[] buff = {(byte)0xFF, (byte)0xFF, (byte)0x02, (byte)0x10, (byte)0xFE, (byte)0xFE};
                sendMessage2(buff);
                break;
            case 1:
                currentSelectedFragment = handMotionFragment;
                break;
            case 2:
                currentSelectedFragment = customizeFragment;
                break;
            case 3:
                currentSelectedFragment = marketFragment;
                break;
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.container,currentSelectedFragment).commit();
    }

    @Override
    public void onTabUnselected(int position) {

    }

    @Override
    public void onTabReselected(int position) {

        if(position == 0) {
            byte[] buff = {(byte) 0xFF, (byte) 0xFF, (byte) 0x02, (byte) 0x10, (byte) 0xFE, (byte) 0xFE};
            sendMessage2(buff);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult " + resultCode);

        switch (requestCode) {
            case REQUEST_CONNECT_DEVICE:
                if(resultCode == Activity.RESULT_OK) {
                    connectDevice(data,true);
                }
        }
    }

    private void connectDevice(Intent data, boolean secure) {

        String address = data.getExtras()
                .getString(BluetoothSearchActivity.EXTRA_DEVICE_ADDRESS);

        Toast.makeText(this, address, Toast.LENGTH_SHORT).show();

        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        mChatService.connect(device, secure);

    }

}
