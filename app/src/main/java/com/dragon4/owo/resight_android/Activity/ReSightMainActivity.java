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
import com.dragon4.owo.resight_android.util.BluetoothConstants;
import com.dragon4.owo.resight_android.util.BluetoothHandService;
import com.dragon4.owo.resight_android.util.BluetoothSensorService;
import com.dragon4.owo.resight_android.Fragment.CustomMizeFragment;
import com.dragon4.owo.resight_android.Fragment.MarketFragment;
import com.dragon4.owo.resight_android.Fragment.MonitoringFragment;
import com.dragon4.owo.resight_android.R;
import com.tsengvn.typekit.TypekitContextWrapper;

public class ReSightMainActivity extends AppCompatActivity implements BottomNavigationBar.OnTabSelectedListener {

    private static final int REQUEST_CONNECT_DEVICE = 8009;
    private static final int REQUEST_SETTING = 8010;
    private static final int REQUEST_ENABLE_BT = 8011;


    public static final String TOAST = "toast";
    public static final String DEVICE_NAME = "device_name";
    private static final String TAG = "ReSightMainActivity";
    private static final String TAG2 = "HnadHandler";

    int lastSelectedPosition = 0;
    BottomNavigationBar bottomNavigationBar;

    private String mConnectedDeviceName = null;
    private BluetoothAdapter mBluetoothAdapter = null;

    private MonitoringFragment monitoringFragment;
    private CustomMizeFragment customizeFragment;
    private MarketFragment     marketFragment;

    private BluetoothHandService mHandService = null;
    private BluetoothSensorService mSensorService = null;

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
    }

    @Override
    public void onStart() {
        super.onStart();
        // If BT is not on, request that it be enabled.
        // setupChat() will then be called during onActivityResult
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
            // Otherwise, setup the chat session
        } else if ( mSensorService == null) {
          //  mHandService = new BluetoothHandService(getApplicationContext(), mSensorHandler);
            mSensorService = new BluetoothSensorService(this, mSensorHandler);
        } else if (mHandService == null) {
            mHandService = new BluetoothHandService(this, mHandHandler);
        }
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
                .addItem(new BottomNavigationItem(R.drawable.icon_setting, "커스터마이징").setActiveColorResource(R.color.colorBottomNavigation))
                .addItem(new BottomNavigationItem(R.drawable.icon_market, "마켓").setActiveColorResource(R.color.colorBottomNavigation))
                .setFirstSelectedPosition(lastSelectedPosition > 2 ? 2 : lastSelectedPosition)
                .initialise();
        bottomNavigationBar.setTabSelectedListener(this);
    }

    private void initFragment() {
        monitoringFragment = new MonitoringFragment();
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

        if (id == R.id.action_bluetooth) {
            Intent bluetoothMoveIntent = new Intent(ReSightMainActivity.this, BluetoothSearchActivity.class);
            startActivityForResult(bluetoothMoveIntent, REQUEST_CONNECT_DEVICE);
            return true;
        } else if(id == R.id.action_settings) {
            // 셋팅 관련 내용,
            return true; //
        }

        return super.onOptionsItemSelected(item);
    }

    private final Handler mSensorHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Context context = getApplicationContext();
            switch (msg.what) {
                case BluetoothConstants.MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case BluetoothSensorService.STATE_CONNECTED:
                            break;
                        case BluetoothSensorService.STATE_CONNECTING:
                            break;
                        case BluetoothSensorService.STATE_LISTEN:
                        case BluetoothSensorService.STATE_NONE:
                            break;
                    }
                    break;
                case BluetoothConstants.MESSAGE_WRITE:
                    byte[] writeBuf = (byte[]) msg.obj;
                    // construct a string from the buffer
                    String writeMessage = new String(writeBuf);
                    Log.d(TAG, writeMessage);
                    break;
                case BluetoothConstants.MESSAGE_READ:
                    byte[] readBuf = (byte[]) msg.obj;
                    // construct a string from the valid bytes in the buffer
                    String readMessage = new String(readBuf, 0, msg.arg1);
                    Log.d(TAG, readMessage);
                    break;
                case BluetoothConstants.MESSAGE_DEVICE_NAME:
                    // save the connected device's name
                    mConnectedDeviceName = msg.getData().getString(BluetoothConstants.DEVICE_NAME);
                    if (null != context) {
                        Toast.makeText(context, "Connected to "
                                + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
                    }
                    break;
                case BluetoothConstants.MESSAGE_TOAST:
                    if (null != context) {
                        Toast.makeText(context, msg.getData().getString(BluetoothConstants.TOAST),
                                Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };

    private final Handler mHandHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Context context = getApplicationContext();
            switch (msg.what) {
                case BluetoothConstants.MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case BluetoothSensorService.STATE_CONNECTED:
                            break;
                        case BluetoothSensorService.STATE_CONNECTING:
                            break;
                        case BluetoothSensorService.STATE_LISTEN:
                        case BluetoothSensorService.STATE_NONE:
                            break;
                    }
                    break;
                case BluetoothConstants.MESSAGE_WRITE:
                    byte[] writeBuf = (byte[]) msg.obj;
                    // construct a string from the buffer
                    String writeMessage = new String(writeBuf);
                    Log.d(TAG, writeMessage);
                    break;
                case BluetoothConstants.MESSAGE_READ:
                    byte[] readBuf = (byte[]) msg.obj;
                    // construct a string from the valid bytes in the buffer
                    String readMessage = new String(readBuf, 0, msg.arg1);
                    Log.d(TAG, readMessage);
                    break;
                case BluetoothConstants.MESSAGE_DEVICE_NAME:
                    // save the connected device's name
                    mConnectedDeviceName = msg.getData().getString(BluetoothConstants.DEVICE_NAME);
                    if (null != context ) {
                        Toast.makeText(context, "Connected to "
                                + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
                    }
                    break;
                case BluetoothConstants.MESSAGE_TOAST:
                    if (null != context) {
                        Toast.makeText(context, msg.getData().getString(BluetoothConstants.TOAST),
                                Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };

   //   private void sendMessage(String message) {
   //       // Check that we're actually connected before trying anything
   //       if (mChatService.getState() != BluetoothSensorService.STATE_CONNECTED) {
   //           Toast.makeText(this, "블루투스가 연결되어 있지 않습니다.", Toast.LENGTH_SHORT).show();
   //           return;
   //       }
////
   //       // Check that there's actually something to send
   //       if (message.length() > 0) {
   //           // Get the message bytes and tell the BluetoothChatService to write
   //           byte[] send = message.getBytes();
   //           mChatService.write(send);
   //           Log.d("여기에들어옴","test");
////
   //           // Reset out string buffer to zero and clear the edit text field
   //           mOutStringBuffer.setLength(0);
   //           //     mOutEditText.setText(mOutStringBuffer);
   //       }
   //   }
////

   private void sendMessage(String message) {

       // Check that there's actually something to send
       if (message.length() > 0) {
           Log.d("여기로도","들어온다");
           // Get the message bytes and tell the BluetoothHandService to write
           byte[] send = message.getBytes();
           mHandService.write(send);
           // Reset out string buffer to zero and clear the edit text field
           //mOutStringBuffer.setLength(0);
      //     mOutHandBuffer.setLength(0);
       }
   }

    private void sendMessage2(byte[] message) {
         // Check that we're actually connected before trying anything
      //   if (mChatService.getState() != BluetoothSensorService.STATE_CONNECTED) {
      //       Toast.makeText(this, "블루투스가 연결되어 있지 않습니다.", Toast.LENGTH_SHORT).show();
      //       return;
      //   }

         // Check that there's actually something to send
         if (message.length > 0) {
             // Get the message bytes and tell the BluetoothChatService to write
             mSensorService.write(message);
             // Reset out string buffer to zero and clear the edit text field
         }
     }
    // Bottom Navigation Activity
    @Override
    public void onTabSelected(int position) {
        Fragment currentSelectedFragment = null;
        switch (position) {
            case 0:
                currentSelectedFragment = monitoringFragment;
                bluetoothTest();
                break;
            case 1:
                currentSelectedFragment = customizeFragment;
                break;
            case 2:
                currentSelectedFragment = marketFragment;
                break;
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.container,currentSelectedFragment).commit();
    }

    private void bluetoothTest() {
        if(mHandService != null && mSensorService != null) {
            String temp = "a";
            int randNum = (int)(Math.random() * 100);
            String b = randNum > 50 ? "a" : "b";
            Log.d("여기로ss",b);
            sendMessage(b);

            Log.d("여기로","들어오나");

            byte[] buff = {(byte) 0xFF, (byte) 0xFF, (byte) 0x02, (byte) 0x10, (byte) 0xFE, (byte) 0xFE};
            sendMessage2(buff);
        }
    }

    @Override
    public void onTabUnselected(int position) {

    }

    @Override
    public void onTabReselected(int position) {

      //  if(position == 0) {
      //      byte[] buff = {(byte) 0xFF, (byte) 0xFF, (byte) 0x02, (byte) 0x10, (byte) 0xFE, (byte) 0xFE};
      //      sendMessage2(buff);
      //  }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult " + resultCode);

        switch (requestCode) {
            case REQUEST_CONNECT_DEVICE:
                if(resultCode == Activity.RESULT_OK) {
                    connectDevice(data,true);
                }
                break;
            case REQUEST_SETTING:

                if(resultCode == Activity.RESULT_OK) {
                    // 셋팅 관련 내용
                }
                break;

        }
    }

    private void connectDevice(Intent data, boolean secure) {

        String address = data.getExtras()
                .getString(BluetoothSearchActivity.EXTRA_DEVICE_ADDRESS);

        String name =  data.getExtras()
                .getString(BluetoothSearchActivity.EXTRA_DEVICE_NAME);

        Toast.makeText(this, address, Toast.LENGTH_SHORT).show();
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);

        if(name.contains("RESIGHT")) {
           mHandService.connect(device,secure);
        } else if(name.contains("GOLFIT")) {
            mSensorService.connect(device,secure);
        } else {
            Toast.makeText(this,"올바른 블루투스 기기에 연결해주십시오.",Toast.LENGTH_LONG).show();
        }
     //   mChatService.connect(device, secure);

    }

}
