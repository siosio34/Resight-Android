package com.dragon4.owo.resight_android.util;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;

import com.dragon4.owo.resight_android.view.Activity.ReSightMainActivity;
import com.dragon4.owo.resight_android.model.SensorData;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;


/**
 * Created by joyeongje on 2017. 3. 18..
 */

public class BluetoothSensorService {

    ReSightMainActivity _context;

    public static int sensorsData[] = new int[6];
    private int newSensorsData[] = new int[6];
    private final int sensorNum = 6;

    // Debugging
    private static final String TAG = "BluetoothSensorService";
    private static final boolean D = true;

    // Name for the SDP record when creating server socket
    private static final String NAME_SECURE = "BluetoothChatSecure";

    // Unique UUID for this application
    private static final UUID MY_UUID_SECURE =
            UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    // Member fields
    private final BluetoothAdapter mAdapter;
    private final Handler mHandler;
    private AcceptThread mSensorSecureAcceptThread;
    private ConnectThread mSensorConnectThread;
    private ConnectedThread mSensorConnectedThread;

    private int mState;
    private int newState;

    private static BluetoothSensorService mChatService = null;

    // Constants that indicate the current connection state
    public static final int STATE_NONE = 0;       // we're doing nothing
    public static final int STATE_LISTEN = 1;     // now listening for incoming connections
    public static final int STATE_CONNECTING = 2; // now initiating an outgoing connection
    public static final int STATE_CONNECTED = 3;  // now connected to a remote device

    private SensorData sensorData;

    /**
     * Constructor. Prepares a new BluetoothChat session.
     *
     * @param context The UI Activity Context
     * @param handler A Handler to send messages back to the UI Activity
     */
    public BluetoothSensorService(Context context, Handler handler) {
        this._context = (ReSightMainActivity) context;

        mAdapter = BluetoothAdapter.getDefaultAdapter();
        mState = STATE_NONE;
        newState = mState;
        mHandler = handler;
    }

    /**
     * Set the current state of the chat connection
     *
     * @param state An integer defining the current connection state
     */
    private synchronized void setState(int state) {
        if (D) Log.d(TAG, "setState() " + mState + " -> " + state);
        mState = state;

        // Give the new state to the Handler so the UI Activity can update
        mHandler.obtainMessage(BluetoothConstants.MESSAGE_STATE_CHANGE, state, -1).sendToTarget();
    }

    /**
     * Return the current connection state.
     */
    public synchronized int getState() {
        return mState;
    }

    /**
     * Start the chat service. Specifically start AcceptThread to begin a
     * session in listening (server) mode. Called by the Activity onResume()
     */
    public synchronized void start() {
        if (D) Log.d(TAG, "start");

        // Cancel any thread attempting to make a connection
        if (mSensorConnectThread != null) {
            mSensorConnectThread.cancel();
            mSensorConnectThread = null;
        }

        // Cancel any thread currently running a connection
        if (mSensorConnectedThread != null) {
            mSensorConnectedThread.cancel();
            mSensorConnectedThread = null;
        }

        // Start the thread to listen on a BluetoothServerSocket
        if (mSensorSecureAcceptThread == null) {
            mSensorSecureAcceptThread = new AcceptThread(true);
            mSensorSecureAcceptThread.start();
        }
        setState(STATE_LISTEN);
    }

    /**
     * Start the ConnectThread to initiate a connection to a remote device.
     *
     * @param device The BluetoothDevice to connect
     * @param secure Socket Security type - Secure (true) , Insecure (false)
     */
    public synchronized void connect(BluetoothDevice device, boolean secure) {
        if (D) Log.d(TAG, "connect to: " + device);

        // Cancel any thread attempting to make a connection
        if (mState == STATE_CONNECTING) {
            if (mSensorConnectThread != null) {
                mSensorConnectThread.cancel();
                mSensorConnectThread = null;
            }
        }

        // Cancel any thread currently running a connection
        if (mSensorConnectedThread != null) {
            mSensorConnectedThread.cancel();
            mSensorConnectedThread = null;
        }

        // Start the thread to connect with the given device
        mSensorConnectThread = new ConnectThread(device, secure);
        mSensorConnectThread.start();
        setState(STATE_CONNECTING);
    }

    /**
     * Start the ConnectedThread to begin managing a Bluetooth connection
     *
     * @param socket The BluetoothSocket on which the connection was made
     * @param device The BluetoothDevice that has been connected
     */
    public synchronized void connected(BluetoothSocket socket, BluetoothDevice
            device, final String socketType) {
        if (D) Log.d(TAG, "connected, Socket Type:" + socketType);

        // Cancel the thread that completed the connection
        if (mSensorConnectThread != null) {
            mSensorConnectThread.cancel();
            mSensorConnectThread = null;
        }

        // Cancel any thread currently running a connection
        if (mSensorConnectedThread != null) {
            mSensorConnectedThread.cancel();
            mSensorConnectedThread = null;
        }

        // Cancel the accept thread because we only want to connect to one device
        if (mSensorSecureAcceptThread != null) {
            mSensorSecureAcceptThread.cancel();
            mSensorSecureAcceptThread = null;
        }

        // Start the thread to manage the connection and perform transmissions
        mSensorConnectedThread = new ConnectedThread(socket, socketType);
        mSensorConnectedThread.start();

        // Send the name of the connected device back to the UI Activity
        Message msg = mHandler.obtainMessage(BluetoothConstants.MESSAGE_DEVICE_NAME);
        Bundle bundle = new Bundle();
        bundle.putString(ReSightMainActivity.DEVICE_NAME, device.getName());
        msg.setData(bundle);
        mHandler.sendMessage(msg);

        setState(STATE_CONNECTED);
    }

    /**
     * Stop all threads
     */
    public synchronized void stop() {
        if (D) Log.d(TAG, "stop");

        if (mSensorConnectThread != null) {
            mSensorConnectThread.cancel();
            mSensorConnectThread = null;
        }

        if (mSensorConnectedThread != null) {
            mSensorConnectedThread.cancel();
            mSensorConnectedThread = null;
        }

        if (mSensorSecureAcceptThread != null) {
            mSensorSecureAcceptThread.cancel();
            mSensorSecureAcceptThread = null;
        }

        setState(STATE_NONE);
    }

    /**
     * Write to the ConnectedThread in an unsynchronized manner
     *
     * @param out The bytes to write
     * @see ConnectedThread#write(byte[])
     */
    public void write(byte[] out) {
        // Create temporary object
        ConnectedThread r;
        // Synchronize a copy of the ConnectedThread
        synchronized (this) {
            if (mState != STATE_CONNECTED) return;
            r = mSensorConnectedThread;
        }
        Log.d("byte to string", new String(out));
        Log.d("byte to hex", byteArrayToHex(out));
        // Perform the write unsynchronized
        r.write(out);
    }

    private String byteArrayToHex(byte[] a) {
        StringBuilder sb = new StringBuilder();
        for (final byte b : a)
            sb.append(String.format("%02x ", b & 0xff));
        return sb.toString();
    }

    /**
     * Indicate that the connection attempt failed and notify the UI Activity.
     */
    private void connectionFailed() {
        // Send a failure message back to the Activity
        Message msg = mHandler.obtainMessage(BluetoothConstants.MESSAGE_TOAST);
        Bundle bundle = new Bundle();
        bundle.putString(BluetoothConstants.TOAST, "Unable to connect device");
        msg.setData(bundle);
        mHandler.sendMessage(msg);

        mState = STATE_NONE;

        // Start the service over to restart listening mode
        BluetoothSensorService.this.start();
    }

    /**
     * Indicate that the connection was lost and notify the UI Activity.
     */
    private void connectionLost() {
        // Send a failure message back to the Activity
        Message msg = mHandler.obtainMessage(BluetoothConstants.MESSAGE_TOAST);
        Bundle bundle = new Bundle();
        bundle.putString(ReSightMainActivity.TOAST, "Device connection was lost");
        msg.setData(bundle);
        mHandler.sendMessage(msg);

        mState = STATE_NONE;

        // Start the service over to restart listening mode
        BluetoothSensorService.this.start();
    }

    /**
     * This thread runs while listening for incoming connections. It behaves
     * like a server-side client. It runs until a connection is accepted
     * (or until cancelled).
     */
    private class AcceptThread extends Thread {
        // The local server socket
        private final BluetoothServerSocket mmServerSocket;
        private String mSocketType;

        public AcceptThread(boolean secure) {
            BluetoothServerSocket tmp = null;
            mSocketType = secure ? "Secure" : "Insecure";

            // Create a new listening server socket
            try {
                tmp = mAdapter.listenUsingRfcommWithServiceRecord(NAME_SECURE,
                        MY_UUID_SECURE);

                // TODO: 2017-04-27 if insecure 
            } catch (IOException e) {
                Log.e(TAG, "Socket Type: " + mSocketType + "listen() failed", e);
            }
            mmServerSocket = tmp;
            mState = STATE_LISTEN;
        }

        public void run() {
            if (D) Log.d(TAG, "Socket Type: " + mSocketType +
                    "BEGIN mAcceptThread" + this);
            setName("AcceptThread" + mSocketType);

            BluetoothSocket socket = null;

            // Listen to the server socket if we're not connected
            while (mState != STATE_CONNECTED) {
                try {
                    // This is a blocking call and will only return on a
                    // successful connection or an exception
                    socket = mmServerSocket.accept();
                } catch (IOException e) {
                    Log.e(TAG, "Socket Type: " + mSocketType + "accept() failed", e);
                    break;
                }

                // If a connection was accepted
                if (socket != null) {
                    synchronized (BluetoothSensorService.this) {
                        switch (mState) {
                            case STATE_LISTEN:
                            case STATE_CONNECTING:
                                // Situation normal. Start the connected thread.
                                connected(socket, socket.getRemoteDevice(),
                                        mSocketType);
                                break;
                            case STATE_NONE:
                            case STATE_CONNECTED:
                                // Either not ready or already connected. Terminate new socket.
                                try {
                                    socket.close();
                                } catch (IOException e) {
                                    Log.e(TAG, "Could not close unwanted socket", e);
                                }
                                break;
                        }
                    }
                }
            }
            if (D) Log.i(TAG, "END mAcceptThread, socket Type: " + mSocketType);

        }

        public void cancel() {
            if (D) Log.d(TAG, "Socket Type" + mSocketType + "cancel " + this);
            try {
                mmServerSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "Socket Type" + mSocketType + "close() of server failed", e);
            }
        }
    }

    /**
     * This thread runs while attempting to make an outgoing connection
     * with a device. It runs straight through; the connection either
     * succeeds or fails.
     */
    private class ConnectThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;
        private String mSocketType;

        public ConnectThread(BluetoothDevice device, boolean secure) {
            mmDevice = device;
            BluetoothSocket tmp = null;
            mSocketType = secure ? "Secure" : "Insecure";

            // Get a BluetoothSocket for a connection with the
            // given BluetoothDevice
            try {
                if (secure) {
                    tmp = device.createRfcommSocketToServiceRecord(
                            MY_UUID_SECURE);
                }
            } catch (IOException e) {
                Log.e(TAG, "Socket Type: " + mSocketType + "create() failed", e);
            }
            mmSocket = tmp;
            mState = STATE_CONNECTING;
        }

        public void run() {
            Log.i(TAG, "BEGIN mSensorConnectThread SocketType:" + mSocketType);
            setName("ConnectThread" + mSocketType);

            // Always cancel discovery because it will slow down a connection
            mAdapter.cancelDiscovery();

            // Make a connection to the BluetoothSocket
            try {
                // This is a blocking call and will only return on a
                // successful connection or an exception
                Log.d("소켓 연결 성공", "테스트");
                mmSocket.connect();
            } catch (IOException e) {
                // Close the socket
                Log.d("소켓 연결 실패", "테스트");
                try {
                    mmSocket.close();
                } catch (IOException e2) {
                    Log.e(TAG, "unable to close() " + mSocketType +
                            " socket during connection failure", e2);
                }
                connectionFailed();
                return;
            }

            // Reset the ConnectThread because we're done
            synchronized (BluetoothSensorService.this) {
                mSensorConnectThread = null;
            }

            // Start the connected thread
            connected(mmSocket, mmDevice, mSocketType);
        }

        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "close() of connect " + mSocketType + " socket failed", e);
            }
        }
    }

    /**
     * This thread runs during a connection with a remote device.
     * It handles all incoming and outgoing transmissions.
     */
    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket, String socketType) {
            Log.d(TAG, "create ConnectedThread: " + socketType);
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the BluetoothSocket input and output streams
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();

            } catch (IOException e) {
                Log.e(TAG, "temp sockets not created", e);
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
            mState = STATE_CONNECTED;
        }

        public void run() {
            Log.i(TAG, "BEGIN mSensorConnectedThread");

            boolean firstLoadSensor = false;
            int bytes = 0;
            int availableBytes ;
            byte[] buffer = new byte[26];

            // Keep listening to the InputStream while connected
            while (mState == STATE_CONNECTED) {
                try {
                    availableBytes = mmInStream.available();
                    if(availableBytes > 26) {
                        bytes = mmInStream.read(buffer);
                        mHandler.obtainMessage(BluetoothConstants.MESSAGE_READ, bytes, -1, buffer).sendToTarget();
                        Log.d("센서 패킷 버퍼 테스트", String.valueOf(bytoHexToInteger(buffer[0]) + " " + bytoHexToInteger(buffer[1]) + " " + bytoHexToInteger(buffer[3]) + " " + bytoHexToInteger(buffer[4])));
                        Log.d("센서 패킷 버퍼 테스트", String.valueOf(bytoHexToInteger(buffer[22]) + " " + bytoHexToInteger(buffer[24]) + " " + bytoHexToInteger(buffer[25])));

                        if (((byte) buffer[0] == (byte) 0xFF && (byte) buffer[1] == (byte) 0xFF && buffer[3] == (byte) 0x11 && buffer[24] == (byte) 0xFE && (byte) buffer[25] == (byte) 0xFE)) {

                            // TODO: 2017-05-06 여기서 나온 센서 데이터 값으로 서버에서 학습시킬지 안드내 모델에서 학습시킬지 정의를 해야됨.
                            //   Log.d("센서 패킷 버퍼 테스트", String.valueOf(bytoHexToInteger(buffer[2]) + " " + bytoHexToInteger(buffer[3]) + " " + bytoHexToInteger(buffer[4]) + " " + bytoHexToInteger(buffer[16])));
                            //   Log.d("센서 패킷 버퍼 테스트2", String.valueOf(bytoHexToInteger(buffer[18]) + " " + bytoHexToInteger(buffer[20]) + " " + bytoHexToInteger(buffer[22]) + " " + bytoHexToInteger(buffer[24])));
                            //   Log.d("센서 패킷 버퍼 테스트3", String.valueOf(bytoHexToInteger(buffer[25])));

                            if (!firstLoadSensor) {
                                for (int i = 0; i < 6; i++) {
                                    sensorsData[i] = bytoHexToInteger(buffer[5 + 2 * i]);
                                }
                                firstLoadSensor = true;
                                //  System.arraycopy(newSensorsData, 0, sensorsData, 0, 6);
                                //   isForcedSensor(sensorsData, newSensorsData);
                            } else {
                                for (int i = 0; i < 6; i++) {
                                    newSensorsData[i] = bytoHexToInteger(buffer[5 + 2 * i]);
                                    //sensorsData[i] = bytoHexToInteger(buffer[5 + 2*i]); // 센서 데이터 다시 바꾸기
                                }
                                isForcedSensor(sensorsData, newSensorsData);
                                System.arraycopy(newSensorsData, 0, sensorsData, 0, 6);
                            }
                            // TODO: 2017-05-06 이거 값 처리해야된다...
                            Log.d("센서 데이터 리스트", String.valueOf(sensorsData[0] + " " + sensorsData[1] + " " + sensorsData[2] + " "
                                    + sensorsData[3] + " " + sensorsData[4] + " " + sensorsData[5]));
                        }

                    } else {
                        SystemClock.sleep(50);
                    }

                } catch (IOException e) {
                    Log.e(TAG, "disconnected", e);
                    connectionLost();
                    break;
                }

            }
        }

        //  try {
                  //      sleep(50);
                  //  } catch (InterruptedException e) {
                  //      e.printStackTrace();
                  //  }
                    // Log.d("sensor로부터 데이터를 읽어옴.",String.valueOf(bytes));
                    // byte[] buff = {(byte)0xFF, (byte)0xFF, (byte)0x02, (byte)0x11, (byte)0xFE, (byte)0xFE
                    // Log.d("sensor Data1",String.valueOf(buffer[0]));
                    // Log.d("sensor Data2",String.valueOf(buffer[1]));
                    // Log.d("sensor Data3",String.valueOf(buffer[24]));
                    // Log.d("sensor Data4",String.valueOf(buffer[25]));
                    // Log.d("0xFE",String.valueOf(sensorsData[0]));
                    // Log.d("0xFF",String.valueOf(sensorsData[1]));
                    // sensorsData[0] = (byte) 0xFE;
                    // sensorsData[1] = (byte) 0xFF;

                   // Log.d("센서 패킷 버퍼 테스트", String.valueOf(bytoHexToInteger(buffer[2]) + " " + bytoHexToInteger(buffer[3]) + " " + bytoHexToInteger(buffer[4]) + " " + bytoHexToInteger(buffer[16])));
                   // Log.d("센서 패킷 버퍼 테스트2", String.valueOf(bytoHexToInteger(buffer[18]) + " " + bytoHexToInteger(buffer[20]) + " " + bytoHexToInteger(buffer[22]) + " " + bytoHexToInteger(buffer[24])));
                   // Log.d("센서 패킷 버퍼 테스트3", String.valueOf(bytoHexToInteger(buffer[25])));
                  //  Log.d("센서 패킷 버퍼 테스트", String.valueOf(bytoHexToInteger(buffer[0]) + " " + bytoHexToInteger(buffer[1]) + " " + bytoHexToInteger(buffer[3]) + " " + bytoHexToInteger(buffer[4])));
                  ////  Log.d("센서 패킷 버퍼 테스트", String.valueOf(bytoHexToInteger(buffer[22]) + " " + bytoHexToInteger(buffer[24]) + " " + bytoHexToInteger(buffer[25])));
                  // byte id;
                  // if (((byte) buffer[0] == (byte) 0xFF && (byte) buffer[1] == (byte) 0xFF && buffer[3] == (byte) 0x11 && buffer[24] == (byte) 0xFE && (byte) buffer[25] == (byte) 0xFE)) {

                  //     // TODO: 2017-05-06 여기서 나온 센서 데이터 값으로 서버에서 학습시킬지 안드내 모델에서 학습시킬지 정의를 해야됨.
                  //      //   Log.d("센서 패킷 버퍼 테스트", String.valueOf(bytoHexToInteger(buffer[2]) + " " + bytoHexToInteger(buffer[3]) + " " + bytoHexToInteger(buffer[4]) + " " + bytoHexToInteger(buffer[16])));
                  //      //   Log.d("센서 패킷 버퍼 테스트2", String.valueOf(bytoHexToInteger(buffer[18]) + " " + bytoHexToInteger(buffer[20]) + " " + bytoHexToInteger(buffer[22]) + " " + bytoHexToInteger(buffer[24])));
                  //      //   Log.d("센서 패킷 버퍼 테스트3", String.valueOf(bytoHexToInteger(buffer[25])));

                  //     if (!firstLoadSensor) {
                  //         for (int i = 0; i < 6; i++) {
                  //             sensorsData[i] = bytoHexToInteger(buffer[5 + 2 * i]);
                  //         }
                  //         firstLoadSensor = true;
                  //         //  System.arraycopy(newSensorsData, 0, sensorsData, 0, 6);
                  //         //   isForcedSensor(sensorsData, newSensorsData);
                  //     } else {
                  //         for (int i = 0; i < 6; i++) {
                  //             newSensorsData[i] = bytoHexToInteger(buffer[5 + 2 * i]);
                  //             //sensorsData[i] = bytoHexToInteger(buffer[5 + 2*i]); // 센서 데이터 다시 바꾸기
                  //         }
                  //         isForcedSensor(sensorsData, newSensorsData);
                  //         System.arraycopy(newSensorsData, 0, sensorsData, 0, 6);
                  //     }
                  //     // TODO: 2017-05-06 이거 값 처리해야된다...
                  //     Log.d("센서 데이터 리스트", String.valueOf(sensorsData[0] + " " + sensorsData[1] + " " + sensorsData[2] + " "
                  //             + sensorsData[3] + " " + sensorsData[4] + " " + sensorsData[5]));

                  //     String deviceID = "resight01";
                        //  FirebaseDatabase database = FirebaseDatabase.getInstance();
                        //  DatabaseReference myRef = database.getReference("monitor_result");
                        //  sensorData = new SensorData(0, "ARM", sensorsData[0],sensorsData[1],sensorsData[2],sensorsData[3],sensorsData[4],sensorsData[5]);
                        //  myRef.child(deviceID).child("sensors").push().setValue(sensorData);


        private void isForcedSensor(int[] oldSensorDatas, int[] newSensorDatas) {
            // TODO: 2017-05-07 차이판별하고 그 뒤에
            double meanDistance = 0.0d;
            double meanDistanceSum = 0.0d;
            for (int i = 0; i < sensorNum; i++) {
                meanDistanceSum += Math.pow((newSensorDatas[i] - oldSensorDatas[i]), 2);
            }
            meanDistance = meanDistanceSum / (2 * sensorNum);
            Log.d("meanDistance", String.valueOf(meanDistance));

            //if(meanDistance)
            // mHandler.obtainMessage(BluetoothConstants.MESSAGE_WRITE, -1, -1, buffer)
            //         .sendToTarget();
        }

        private int bytoHexToInteger(byte hexNum) {
            StringBuilder sb = new StringBuilder(2);
            String hexDecimalNum;

            hexDecimalNum = "0" + Integer.toHexString(0xff & hexNum);
            sb.append(hexDecimalNum.substring(hexDecimalNum.length() - 2));

            return Integer.parseInt(sb.toString(), 16);
        }

        /**
         * Write to the connected OutStream.
         *
         * @param buffer The bytes to write
         */
        public void write(byte[] buffer) {
            try {
                mmOutStream.write(buffer);
                // Log.d("데이터쓴다", String.valueOf(buffer));

                // Share the sent message back to the UI Activity
                mHandler.obtainMessage(BluetoothConstants.MESSAGE_WRITE, -1, -1, buffer)
                        .sendToTarget();

            } catch (IOException e) {
                Log.e(TAG, "Exception during write", e);
            }
        }

        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "close() of connect socket failed", e);
            }
        }
    }

    /**
     * Convert 2-bytes integer to JAVA integer
     *
     * @param firstByte  first byte of 2-byte integer
     * @param secondByte second byte of 2-byte integer
     * @return Integer(JAVA default data type)
     */
    protected static int toUnsignedIntFromTwoBytes(byte firstByte, byte secondByte) {
        int result = 0;

        result |= secondByte & 0xFF;
        //result *= 0x00000100;
        result <<= 8;
        result |= firstByte & 0xFF;

        return result & 0x0000FFFF;
    }

    /**
     * Convert 2-bytes integer to JAVA integer
     *
     * @param firstByte  first byte of 2-byte integer
     * @param secondByte second byte of 2-byte integer
     * @return Integer(JAVA default data type)
     */
    protected static int toUnsignedIntFromTwoBytes2(byte secondByte, byte firstByte) {
        int result = 0;

        result |= secondByte;
        //result *= 0x00000100;
        result <<= 8;
        result |= firstByte & 0xFF;


//        result |= secondByte & 0xFF;
//        //result *= 0x00000100;
//        result <<= 8;
//        result |= firstByte & 0xFF;

        //return result & 0x0000FFFF;
        return result;
    }
}
