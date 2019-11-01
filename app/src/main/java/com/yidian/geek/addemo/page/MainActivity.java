package com.yidian.geek.addemo.page;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.yidian.adsdk.AdSDK;
import com.yidian.adsdkdemo.R;

/**
 * @author zhangzhun
 * @data 2019/10/14
 */
public class MainActivity extends Activity implements View.OnClickListener {

    private TextView mVersionTxt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.splashBtn).setOnClickListener(this);
        findViewById(R.id.feedRecyclerViewBtn).setOnClickListener(this);
        findViewById(R.id.feedViewBtn).setOnClickListener(this);
        findViewById(R.id.feedListiewBtn).setOnClickListener(this);
        mVersionTxt = findViewById(R.id.adVersion);
        mVersionTxt.setText("SDK Version:" + AdSDK.getInstance().getVersion());
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();

        switch (v.getId()) {
            case R.id.splashBtn:
                intent.setClass(this, SplashADTestActivity.class);
                break;
            case R.id.feedViewBtn:
                intent.setClass(this, FeedAdTestActivity.class);
                break;
            case R.id.feedRecyclerViewBtn:
                intent.setClass(this, FeedAdRecyclerViewTestActivity.class);
                break;
            case R.id.feedListiewBtn:
                intent.setClass(this, FeedAdListViewTestActivity.class);
                break;
            default:
                break;
        }

        startActivity(intent);
    }
}
