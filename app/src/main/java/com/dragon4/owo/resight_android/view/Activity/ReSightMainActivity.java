package com.dragon4.owo.resight_android.view.Activity;

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
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.dragon4.owo.resight_android.model.ResightBluetoothDevice;
import com.dragon4.owo.resight_android.view.Fragment.TestTrainModeMainFragment;
import com.dragon4.owo.resight_android.util.ActivityResultEvent;
import com.dragon4.owo.resight_android.util.BluetoothConstants;
import com.dragon4.owo.resight_android.util.BluetoothHandService;
import com.dragon4.owo.resight_android.util.BluetoothSensorService;
import com.dragon4.owo.resight_android.view.Fragment.CustomMizeFragment;
import com.dragon4.owo.resight_android.view.Fragment.MarketFragment;
import com.dragon4.owo.resight_android.R;
import com.tsengvn.typekit.TypekitContextWrapper;

import org.greenrobot.eventbus.EventBus;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class ReSightMainActivity extends AppCompatActivity implements BottomNavigationBar.OnTabSelectedListener {

    private static final int REQUEST_CONNECT_DEVICE = 8009;
    private static final int REQUEST_SETTING = 8010;
    private static final int REQUEST_ENABLE_BT = 8011;

    public static final String TOAST = "toast";
    public static final String DEVICE_NAME = "device_name";
    private static final String TAG = "ReSightMainActivity";
    private static final String TAG2 = "HnadHandler";

    int lastSelectedPosition = 0;
    private BottomNavigationBar bottomNavigationBar;

    private String mConnectedDeviceName = null;
    private BluetoothAdapter mBluetoothAdapter = null;

    private TestTrainModeMainFragment monitoringFragment;
    private CustomMizeFragment customizeFragment;
    private MarketFragment     marketFragment;

    private BluetoothHandService mHandService = null;
    private BluetoothSensorService mSensorService = null;

    Toolbar toolbar;

    private ImageView sensorMiniIcon;
    private ImageView handMiniIcon;

    private Realm mRealm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resight_main);
        initToolbar();
        initBottomNavigationBar();
        initFragment();
        initBluetooth();
        initRealmDB();
    }

    @Override
    protected void onStart() {
        super.onStart();


    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG,"onResume 호출");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRealm.close();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        sensorMiniIcon = (ImageView) findViewById(R.id.sensor_mini_icon);
        sensorMiniIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connectSensorDevice();
                // TODO: 2017-05-08 이미지 바뀌는것도 구현하면된다.
            }
        });

        handMiniIcon   = (ImageView) findViewById(R.id.hand_mini_icon);
        handMiniIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connectHandDevice();
                // TODO: 2017-05-08 이미지 바뀌는것도 구현하면된다.
            }
        });

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

    // Bottom Navigation Activity
    @Override
    public void onTabSelected(int position) {
        Fragment currentSelectedFragment = null;
        switch (position) {
            case 0:
                currentSelectedFragment = monitoringFragment;
                bluetoothTest();
                BluetoothSensorService.isSaveMode = true;
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

            //  String temp = "a";
            //  int randNum = (int)(Math.random() * 100);
            //  String b = randNum > 50 ? "a" : "b";
            //  sendMessage(b);
            //  byte[] buff = {(byte) 0xFF, (byte) 0xFF, (byte) 0x02, (byte) 0x11, (byte) 0xFE, (byte) 0xFE};
            //  sendMessage2(buff);
            byte[] buff2 = {(byte) 0x11};
            sendMessageToSensor(buff2);
        }
    }



    @Override
    public void onTabUnselected(int position) {

    }

    @Override
    public void onTabReselected(int position) {

    }

    private void initFragment() {
        monitoringFragment = new TestTrainModeMainFragment();
        customizeFragment = new CustomMizeFragment();
        marketFragment = new MarketFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.container,monitoringFragment).commit();
    }

    private void initBluetooth() {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
            // Otherwise, setup the chat session
        }

        if(mSensorService == null) {
            mSensorService = new BluetoothSensorService(this, mSensorHandler);
            Log.d(TAG, "mSensorService 생성됨.");
        }

         if (mHandService == null ) {
             mHandService = new BluetoothHandService(this, mHandHandler);
             Log.d(TAG,"mHandService 생성됨.");
        }
    }

    private void initRealmDB() {
        Realm.init(getApplicationContext());
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().build();
        mRealm = Realm.getInstance(realmConfiguration);
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
            Intent settingMovieIntent = new Intent(ReSightMainActivity.this, SettingActivity.class);
            startActivity(settingMovieIntent);
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
                            sensorMiniIcon.setImageResource(R.drawable.icon_bleon_sensor_on);
                            break;
                        case BluetoothSensorService.STATE_CONNECTING:
                            break;
                        case BluetoothSensorService.STATE_LISTEN:
                        case BluetoothSensorService.STATE_NONE:
                            sensorMiniIcon.setImageResource(R.drawable.icon_bleoff_sensor2);

                            // TODO: 2017-05-07 여기서 사용 안된다는 이미지바꿔야함.
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
                            handMiniIcon.setImageResource(R.drawable.icon_bleon_handon);
                            break;
                        case BluetoothSensorService.STATE_CONNECTING:
                            break;
                        case BluetoothSensorService.STATE_LISTEN:
                        case BluetoothSensorService.STATE_NONE:
                            handMiniIcon.setImageResource(R.drawable.icon_bleoff_hand2);
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
                    //Log.d("readBuf", readBuf[0])
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

   private void sendMessageToHand(String message) {

       // Check that there's actually something to send
       if (message.length() > 0) {

           // Get the message bytes and tell the BluetoothHandService to write
           byte[] send = message.getBytes();
           mHandService.write(send);

           // Reset out string buffer to zero and clear the edit text field
           //mOutStringBuffer.setLength(0);
      //     mOutHandBuffer.setLength(0);
       }
   }

    private void sendMessageToSensor(byte[] message) {
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


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult " + resultCode);
        EventBus.getDefault().post(new ActivityResultEvent(requestCode, resultCode, data));

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

        String name =  data.getExtras()
                .getString(BluetoothSearchActivity.EXTRA_DEVICE_NAME);

        String address = data.getExtras()
                .getString(BluetoothSearchActivity.EXTRA_DEVICE_ADDRESS);

        Toast.makeText(this, address, Toast.LENGTH_SHORT).show();
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);

        if(name.contains("RESIGHT")) {
            mHandService.connect(device,secure);
            saveResightBluetoothAddress("hand",name, address);
        } else if(name.contains("GOLFIT")) {
            mSensorService.connect(device,secure);
            saveResightBluetoothAddress("sensor",name, address);
        } else {
            Toast.makeText(this, "올바른 블루투스 기기에 연결해주십시오.", Toast.LENGTH_LONG).show();
        }

    }

   private void saveResightBluetoothAddress(final String sensorType, final String name, final String address) {
       final ResightBluetoothDevice resightBluetoohhDevice = new ResightBluetoothDevice();
       resightBluetoohhDevice.setSensorType(sensorType);
       resightBluetoohhDevice.setName(name);
       resightBluetoohhDevice.setDeviceAddress(address);

       mRealm.executeTransaction(new Realm.Transaction() {
           @Override
           public void execute(Realm realm) {
               realm.copyToRealmOrUpdate(resightBluetoohhDevice);
           }
       });
   }

   private void connectSensorDevice() {
       ResightBluetoothDevice realmResults = mRealm.where(ResightBluetoothDevice.class)
               .contains("sensorType","sensor").findFirst();

       if(realmResults != null) {
           BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(realmResults.getDeviceAddress());
           mSensorService.connect(device, true);
       }

   }
   private void connectHandDevice() {
       ResightBluetoothDevice realmResults = mRealm.where(ResightBluetoothDevice.class)
               .contains("sensorType","hand").findFirst();

       if(realmResults != null) {
           BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(realmResults.getDeviceAddress());
           mHandService.connect(device, true);
       }

   }
}
