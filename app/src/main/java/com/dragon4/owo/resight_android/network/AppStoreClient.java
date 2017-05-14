package com.dragon4.owo.resight_android.network;

import com.dragon4.owo.resight_android.model.AppStore;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by joyeongje on 2017. 4. 6..
 */

public interface AppStoreClient {

    @GET("/app_store")
    Call<List<AppStore>> getAppList();

    @POST("/app_store")
    Call<AppStore> uploadApp(@Body AppStore appStore);

}
