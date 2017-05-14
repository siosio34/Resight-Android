package com.dragon4.owo.resight_android.model;

/**
 * Created by joyeongje on 2017. 3. 27..
 */

public class SensorData {

    private int action_code;
    private String wear_site;
    private int sensor1;
    private int sensor2;
    private int sensor3;
    private int sensor4;
    private int sensor5;
    private int sensor6;

    public SensorData(int action_code,String wear_site,int sensor1,int sensor2,int sensor3,int sensor4,int sensor5,int sensor6) {
        this.action_code = action_code;
        this.wear_site = wear_site;
        this.sensor1 = sensor1;
        this.sensor2 = sensor2;
        this.sensor3 = sensor3;
        this.sensor4 = sensor4;
        this.sensor5 = sensor5;
        this.sensor6 = sensor6;
    }

    public int getAction_code() {
        return action_code;
    }

    public String getWear_site() {
        return wear_site;
    }

    public int getSensor1() {
        return sensor1;
    }

    public int getSensor2() {
        return sensor2;
    }

    public int getSensor3() {
        return sensor3;
    }

    public int getSensor4() {
        return sensor4;
    }

    public int getSensor5() {
        return sensor5;
    }

    public int getSensor6() {
        return sensor6;
    }
}

