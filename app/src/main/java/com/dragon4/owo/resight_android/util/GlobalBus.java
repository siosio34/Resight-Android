package com.dragon4.owo.resight_android.util;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by young on 2017-05-03.
 */

public class GlobalBus {
    private static EventBus sBus;
    public static EventBus getBus() {
        if (sBus == null)
            sBus = EventBus.getDefault();
        return sBus;
    }
}
