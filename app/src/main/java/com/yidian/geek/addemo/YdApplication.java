package com.yidian.geek.addemo;

import android.app.Application;
import android.content.Context;
import com.umeng.analytics.MobclickAgent;
import com.yidian.adsdk.AdSDK;
import com.yidian.adsdkdemo.BuildConfig;
import com.yidian.newssdk.NewsFeedsSDK;

/**
 * Created by chenyichang on 2018/5/18.
 */

public class YdApplication extends Application {

    private static final String TAG = YdApplication.class.getSimpleName();

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        MobclickAgent.UMAnalyticsConfig config = new MobclickAgent.UMAnalyticsConfig(
                this,
                BuildConfig.UMENG_KEY,
                BuildConfig.CHANNEL,
                MobclickAgent.EScenarioType.E_UM_NORMAL);

        MobclickAgent.startWithConfigure(config);
        MobclickAgent.setCatchUncaughtExceptions(true);
        MobclickAgent.setDebugMode(BuildConfig.DEBUG);
        MobclickAgent.openActivityDurationTrack(false);  //禁止默认的页面统计


        /**
         * 初始化SDK
         */
        new AdSDK.Builder()
                .setAppId(BuildConfig.AD_APP_ID)
                .setAppKey(BuildConfig.AD_APP_KEY)
                .setContext(getApplicationContext())
                .setDebugEnabled(false)
                .build();

        new NewsFeedsSDK.Builder()
                .setAppId(BuildConfig.GEEK_APP_ID)
                .setAppKey(BuildConfig.GEEK_APP_KEY)
                .setContext(getApplicationContext())
                .setDebugEnabled(BuildConfig.DEBUG)
                .build();

    }
}
