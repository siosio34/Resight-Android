package com.dragon4.owo.resight_android.model;

/**
 * Created by joyeongje on 2017. 4. 6..
 */

public class AppStore {

    private int id;
    private String appName;
    private String appStoreUrl;
    private String appImageUrl;

    public AppStore(int id, String appName, String appStoreUrl, String appImageUrl) {

        this.id = id;
        this.appName= appName;
        this.appStoreUrl= appStoreUrl;
        this.appImageUrl= appImageUrl;
    }

    public int getId() {
        return id;
    }

    public String getAppName() {
        return appName;
    }

    public String getAppStoreUrl() {
        return appStoreUrl;
    }

    public String getAppImageUrl() {
        return appImageUrl;
    }
}
