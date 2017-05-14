package com.dragon4.owo.resight_android.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by young on 2017-05-07.
 */

public class ResightBluetoothDevice extends RealmObject {

    // TODO: 2017-05-07 센서타입을키로 한 휴대폰에 하나의 센서와 하나의 기기등록하게한다.
    @PrimaryKey
    private String sensorType;
    private String deviceAddress;
    private String name;

    public String getDeviceAddress() {
        return deviceAddress;
    }

    public void setDeviceAddress(String deviceAddress) {
        this.deviceAddress = deviceAddress;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSensorType() {
        return sensorType;
    }

    public void setSensorType(String sensorType) {
        this.sensorType = sensorType;
    }
}
