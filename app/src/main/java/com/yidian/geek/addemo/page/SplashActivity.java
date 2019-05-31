package com.yidian.geek.addemo.page;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.yidian.adsdk.core.splash.SplashAD;
import com.yidian.adsdk.core.splash.SplashADListener;
import com.yidian.adsdkdemo.R;
import com.yidian.geek.addemo.helper.ToastUtils;
import com.yidian.newssdk.utils.ThreadUtils;


public class SplashActivity extends Activity implements SplashADListener {

    private static final String TAG = SplashActivity.class.getSimpleName();
    public boolean canJump = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ThreadUtils.postDelayed2UI(new Runnable() {
            @Override
            public void run() {
                new SplashAD.Builder()
                        .setActivity(SplashActivity.this)
                        .setAdContainerRes(R.id.adContainer)
                        .setSplashlogo(R.mipmap.ic_launcher)
                        .setSplashADListener(SplashActivity.this)
                        .show();
            }
        }, 1000L);


    }

    @Override
    public void finish() {
        super.finish();
    }

    @Override
    public void onADDismiss() {
        ToastUtils.showShort(this, "onADDismiss");
        next();
    }

    @Override
    public void onADPresent() {
        ToastUtils.showShort(this, "onADPresent");
    }

    @Override
    public void onADScreenClick() {
        ToastUtils.showShort(this, "onADScreenClick");
    }

    @Override
    public void onADFail() {
        ToastUtils.showShort(this, "onADFail");
        startActivity(new Intent(this, NaviHomeActivity.class));
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (canJump) {
            next();
        }
        canJump = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        canJump = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void next() {
        if (canJump) {
            this.startActivity(new Intent(this, NaviHomeActivity.class));
            this.finish();
        } else {
            canJump = true;
        }
    }

}
