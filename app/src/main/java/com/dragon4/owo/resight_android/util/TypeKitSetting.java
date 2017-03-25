package com.dragon4.owo.resight_android.util;

import android.app.Application;

import com.tsengvn.typekit.Typekit;

/**
 * Created by joyeongje on 2017. 3. 19..
 */

public class TypeKitSetting extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Typekit.getInstance()
                .addNormal(Typekit.createFromAsset(this, "fonts/NanumBarunpenR.ttf"))
                .addBold(Typekit.createFromAsset(this, "fonts/NanumBarunpenB.ttf"))
                .addItalic(Typekit.createFromAsset(this, "fonts/NanumBarunpenB.ttf"))
                .addBoldItalic(Typekit.createFromAsset(this, "fonts/NanumBarunpenB.ttf"));
    }
}
