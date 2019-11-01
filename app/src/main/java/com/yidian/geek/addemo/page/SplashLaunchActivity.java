package com.yidian.geek.addemo.page;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.yidian.adsdk.core.splash.SplashAD;
import com.yidian.adsdk.core.splash.SplashADListener;
import com.yidian.adsdk.utils.ThreadUtils;
import com.yidian.adsdkdemo.R;


public class SplashLaunchActivity extends Activity {

    private static final String TAG = SplashLaunchActivity.class.getSimpleName();
    public boolean canJump = false;
    private ImageView logoImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        logoImg = findViewById(R.id.logoImg);
        ThreadUtils.postDelayed2UI(new Runnable() {
            @Override
            public void run() {
                new SplashAD.Builder()
                        .setActivity(SplashLaunchActivity.this)
                        .setAdContainerRes(R.id.adContainer)
                        .setSplashlogo(R.mipmap.ic_launcher)
                        .setSplashADListener(new SplashADListener() {
                            @Override
                            public void onADTimeOver() {
                                Toast.makeText(SplashLaunchActivity.this, "回调：广告展示时间到", Toast.LENGTH_SHORT).show();
                                next();
                            }

                            @Override
                            public void onADSkip() {
                                Toast.makeText(SplashLaunchActivity.this, "回调：跳过广告", Toast.LENGTH_SHORT).show();
                                next();
                            }

                            @Override
                            public void onADPresent() {
                                logoImg.setVisibility(View.GONE);
                                Toast.makeText(SplashLaunchActivity.this, "回调：广告展示", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onADScreenClick() {
                                Toast.makeText(SplashLaunchActivity.this, "回调：广告点击", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onADFail(String errorMsg) {
                                Toast.makeText(SplashLaunchActivity.this, "回调：广告展示失败", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(SplashLaunchActivity.this, MainActivity.class));
                                finish();
                            }
                        })
                        .setBottomViewDismiss(false)
                        .show();
            }
        }, 1000L);


    }

    @Override
    public void finish() {
        super.finish();
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
            this.startActivity(new Intent(this, MainActivity.class));
            this.finish();
        } else {
            canJump = true;
        }
    }

}
