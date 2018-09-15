package com.team.elysium.moneyfit;

import android.app.Application;

import com.bumptech.glide.Glide;
import com.kakao.auth.KakaoSDK;
import com.team.elysium.moneyfit.adapter.KakaoSDKAdapter;

/**
 * Created by sh on 2018-07-07.
 */
public class App extends Application {

    private static volatile App instance = null;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        KakaoSDK.init(new KakaoSDKAdapter());
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Glide.get(this).clearMemory();
    }
    @Override public void onTrimMemory(int level) { super.onTrimMemory(level); Glide.get(this).trimMemory(level); }

    public static App getGlobalApplicationContext() {
        return instance;
    }
}