package com.dragon4.owo.resight_android.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class MuscleSensorData {

    @SerializedName("userId")
    @Expose
    private String userId;
    @SerializedName("motionCode")
    @Expose
    private String motionCode;
    @SerializedName("Muscledatas")
    @Expose
    private List<Muscledata> muscledatas = null;

    /**
     * No args constructor for use in serialization
     *
     */
    public MuscleSensorData() {
    }

    /**
     *
     * @param muscledatas
     * @param userId
     * @param motionCode
     */
    public MuscleSensorData(String userId, String motionCode, List<Muscledata> muscledatas) {
        super();
        this.userId = userId;
        this.motionCode = motionCode;
        this.muscledatas = muscledatas;
    }

    public MuscleSensorData(String userId, String motionCode, ArrayList<Muscledata> sensorsArrayList) {

    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMotionCode() {
        return motionCode;
    }

    public void setMotionCode(String motionCode) {
        this.motionCode = motionCode;
    }

    public List<Muscledata> getMuscledatas() {
        return muscledatas;
    }

    public void setMuscledatas(List<Muscledata> muscledatas) {
        this.muscledatas = muscledatas;
    }

}