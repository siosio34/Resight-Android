package com.dragon4.owo.resight_android.network;

import com.dragon4.owo.resight_android.model.MuscleSensorData;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by joyeongje on 2017. 6. 15..
 */

public interface SensorClient {

    @POST("/api/save_text")
    Call<MuscleSensorData> uploadSensorData(@Body MuscleSensorData muscleSensorData);
}
