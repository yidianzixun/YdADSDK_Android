package com.yidian.geek.addemo.page;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.yidian.adsdk.core.feedad.base.IYdAdDislike;
import com.yidian.adsdk.core.feedad.base.IYdNativeAd;
import com.yidian.adsdk.core.feedad.core.YdAdEngine;
import com.yidian.adsdk.core.feedad.core.YdAdRequest;
import com.yidian.adsdk.core.feedad.core.YdNativeExpressAd;
import com.yidian.adsdkdemo.R;

import java.util.List;


public class FeedAdTestActivity extends FragmentActivity implements View.OnClickListener {

    private LinearLayout mAdContainer;
    private Button mAdLoadBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_ad_test);
        mAdLoadBtn = findViewById(R.id.btnAdload);
        mAdLoadBtn.setOnClickListener(this);
        mAdContainer = findViewById(R.id.nativeAdContainer);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAdload:
                loadAD();
                break;
            default:
                break;
        }
    }

    private void loadAD() {
        YdAdRequest ydAdRequest = YdAdRequest.newBuilder()
                .reqestAdCount(2)
                .build();
        new YdAdEngine(this).loadNativeExpressAd(ydAdRequest, new IYdNativeAd.OnLoadNativeExpressAdListener() {
            @Override
            public void onError(int errorCode, String errorMessage) {

            }

            @Override
            public void onNativeExpressAdLoad(List<YdNativeExpressAd> nativeExpressAdList) {
                if (nativeExpressAdList != null && nativeExpressAdList.size() > 0) {
                    for (YdNativeExpressAd nativeExpressAd : nativeExpressAdList) {
                        nativeExpressAd.setDislikeCallback(new IYdAdDislike.DislikeInteractionCallback() {
                            @Override
                            public void onSelected() {
                                Toast.makeText(FeedAdTestActivity.this, "点击负反馈", Toast.LENGTH_SHORT).show();
                            }
                        });
                        nativeExpressAd.setOnShowAdViewListener(new IYdNativeAd.OnShowAdViewListener() {

                            @Override
                            public void onAdClicked(View view, int type) {
                                Toast.makeText(FeedAdTestActivity.this, "点击广告", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onAdShow(View view, int type) {
                                Toast.makeText(FeedAdTestActivity.this, "广告展示成功", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onAdShowFail(String msg, int code) {
                                Toast.makeText(FeedAdTestActivity.this, "广告展示失败", Toast.LENGTH_SHORT).show();
                            }
                        });


                        nativeExpressAd.setOnDownloadListener(new IYdNativeAd.OnDownloadListener() {

                            @Override
                            public void onDownloadProgress(String packageName, int downloadProgress) {

                            }

                            @Override
                            public void onDownloadPaused(String packageName) {
                                Toast.makeText(FeedAdTestActivity.this, "暂停下载：" + packageName, Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onDownloadFailed(String packageName) {
                                Toast.makeText(FeedAdTestActivity.this, "下载失败：" + packageName, Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onDownloadFinished(String packageName) {
                                Toast.makeText(FeedAdTestActivity.this, "下载完成：" + packageName, Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onInstalled(String packageName) {
                                Toast.makeText(FeedAdTestActivity.this, "安装成功：" + packageName, Toast.LENGTH_SHORT).show();
                            }
                        });
                        mAdContainer.addView(nativeExpressAd.getAdView());

                    }
                } else {
                    Toast.makeText(FeedAdTestActivity.this, "广告数据返回为空", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
