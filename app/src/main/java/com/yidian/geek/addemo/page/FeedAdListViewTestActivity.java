package com.yidian.geek.addemo.page;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yidian.adsdk.core.feedad.base.IYdAdDislike;
import com.yidian.adsdk.core.feedad.base.IYdNativeAd;
import com.yidian.adsdk.core.feedad.core.YdAdEngine;
import com.yidian.adsdk.core.feedad.core.YdAdRequest;
import com.yidian.adsdk.core.feedad.core.YdNativeExpressAd;
import com.yidian.adsdkdemo.R;
import com.yidian.geek.addemo.view.ILoadMoreListener;
import com.yidian.geek.addemo.view.LoadMoreListView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhangzhun
 * @data 2019/10/15
 */
public class FeedAdListViewTestActivity extends FragmentActivity implements View.OnClickListener {

    private static final int ITEM_COUNT = 30;
    private Button mLoadBtn;
    private LoadMoreListView mFeedAdListView;
    private List<YdNativeExpressAd> mNativeExpressAdList;
    private FeedAdAdapter mFeedAdAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_ad_listview_test);
        mLoadBtn = findViewById(R.id.btnAdload);
        mLoadBtn.setOnClickListener(this);
        mFeedAdListView = findViewById(R.id.feedAdListView);
        mNativeExpressAdList = new ArrayList<>();
        mFeedAdAdapter = new FeedAdAdapter(this,mNativeExpressAdList);
        mFeedAdListView.setAdapter(mFeedAdAdapter);
        mFeedAdListView.setLoadMoreListener(new ILoadMoreListener() {
            @Override
            public void onLoadMore() {
                loadFeedAD();
            }
        });
    }

    @Override
    public void onClick(View v) {
        loadFeedAD();
    }

    private void loadFeedAD() {
        YdAdRequest ydAdRequest = YdAdRequest.newBuilder()
                .reqestAdCount(2)
                .build();
        new YdAdEngine(this).loadNativeExpressAd(ydAdRequest, new IYdNativeAd.OnLoadNativeExpressAdListener() {
            @Override
            public void onError(int errorCode, String errorMessage) {
                if (mFeedAdListView != null) {
                    mFeedAdListView.setLoadingFinish();
                }
            }

            @Override
            public void onNativeExpressAdLoad(List<YdNativeExpressAd> nativeExpressAdList) {
                if (nativeExpressAdList != null && nativeExpressAdList.size() > 0) {
                    if (mFeedAdListView != null) {
                        mFeedAdListView.setLoadingFinish();
                    }
                    for (int i = 0; i < ITEM_COUNT; i++) {
                        mNativeExpressAdList.add(null);
                    }
                    int count = mNativeExpressAdList.size();

                    for (YdNativeExpressAd ad : nativeExpressAdList) {
                        int random = (int) (Math.random() * ITEM_COUNT) + count - ITEM_COUNT;
                        mNativeExpressAdList.set(random, ad);
                    }
                    mFeedAdAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(FeedAdListViewTestActivity.this, "广告数据返回为空", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public static class FeedAdAdapter extends BaseAdapter {

        private static final int ADTYPE_AD = 0;
        private static final int ADTYPE_NORMAL = 1;

        private Context mContext;
        private List<YdNativeExpressAd> mNativeExpressAdList;


        public FeedAdAdapter(Context context, List<YdNativeExpressAd> data) {
            this.mContext = context;
            this.mNativeExpressAdList = data;
        }

        @Override
        public int getCount() {
            return mNativeExpressAdList.size();
        }

        @Override
        public YdNativeExpressAd getItem(int position) {
            return mNativeExpressAdList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            YdNativeExpressAd ad = getItem(position);
            switch (getItemViewType(position)) {
                case ADTYPE_AD:
                    return getADView(convertView, parent, ad, this);
                default:
                    return getNormalView(convertView, parent, position);
            }
        }

        @Override
        public int getItemViewType(int position) {
            YdNativeExpressAd ad = getItem(position);
            if (ad == null) {
                return ADTYPE_NORMAL;
            } else {
                return ADTYPE_AD;
            }
        }

        @Override
        public int getViewTypeCount() {
            return 2;
        }

        public List<YdNativeExpressAd> getData() {
            return mNativeExpressAdList;
        }


        private View getADView(View convertView, ViewGroup parent, @NonNull final YdNativeExpressAd nativeExpressAd, final FeedAdAdapter feedAdAdapter) {
            final AdViewHolder adViewHolder;
            try {
                if (convertView == null) {
                    convertView = LayoutInflater.from(mContext).inflate(R.layout.cardview_ad, parent, false);
                    adViewHolder = new AdViewHolder();
                    adViewHolder.adContainer = (FrameLayout) convertView.findViewById(R.id.nativeContainer);
                    convertView.setTag(adViewHolder);
                } else {
                    adViewHolder = (AdViewHolder) convertView.getTag();
                }

                final View finalConvertView = convertView;
                nativeExpressAd.setDislikeCallback(new IYdAdDislike.DislikeInteractionCallback() {
                    @Override
                    public void onSelected() {
                        Toast.makeText(finalConvertView.getContext(), "点击负反馈", Toast.LENGTH_SHORT).show();
                        feedAdAdapter.getData().remove(nativeExpressAd);
                        notifyDataSetChanged();
                    }
                });
                nativeExpressAd.setOnShowAdViewListener(new IYdNativeAd.OnShowAdViewListener() {

                    @Override
                    public void onAdClicked(View view, int type) {
                        Toast.makeText(finalConvertView.getContext(), "点击广告", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onAdShow(View view, int type) {
                        Toast.makeText(finalConvertView.getContext(), "广告展示成功", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onAdShowFail(String msg, int code) {
                        Toast.makeText(finalConvertView.getContext(), "广告展示失败", Toast.LENGTH_SHORT).show();
                    }
                });

                nativeExpressAd.setOnDownloadListener(new IYdNativeAd.OnDownloadListener() {

                    @Override
                    public void onDownloadProgress(String packageName, int downloadProgress) {

                    }

                    @Override
                    public void onDownloadPaused(String packageName) {
                        Toast.makeText(finalConvertView.getContext(), "暂停下载：" + packageName, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onDownloadFailed(String packageName) {
                        Toast.makeText(finalConvertView.getContext(), "下载失败：" + packageName, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onDownloadFinished(String packageName) {
                        Toast.makeText(finalConvertView.getContext(), "下载完成：" + packageName, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onInstalled(String packageName) {
                        Toast.makeText(finalConvertView.getContext(), "安装成功：" + packageName, Toast.LENGTH_SHORT).show();
                    }
                });

                if (adViewHolder.adContainer != null) {
                    //获取视频播放view,该view SDK内部渲染，在媒体平台可配置视频是否自动播放等设置。
                    View video = nativeExpressAd.getAdView();
                    //请务必调用此方法，feedAdAdapter
                    nativeExpressAd.onbind(feedAdAdapter);
                    if (video != null) {
                        cleanParent(video);
                        adViewHolder.adContainer.removeAllViews();
                        adViewHolder.adContainer.addView(video);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return convertView;
        }


        private View getNormalView(View convertView, ViewGroup parent, int position) {
            NormalViewHolder normalViewHolder;
            if (convertView == null) {
                normalViewHolder = new NormalViewHolder();
                convertView = LayoutInflater.from(mContext).inflate(R.layout.cardview_normal, parent, false);
                normalViewHolder.text = convertView.findViewById(R.id.text);
                convertView.setTag(normalViewHolder);
            } else {
                normalViewHolder = (NormalViewHolder) convertView.getTag();
            }
            normalViewHolder.text.setText("ListView item " + position);
            return convertView;
        }

        private static class AdViewHolder {
            FrameLayout adContainer;
        }

        private static class NormalViewHolder {
            TextView text;
        }
    }

    private static void cleanParent(View view) {
        if (view.getParent() == null) {
            return;
        }
        if (view.getParent() instanceof FrameLayout) {
            ((FrameLayout) view.getParent()).removeAllViews();
        } else if (view.getParent() instanceof LinearLayout) {
            ((LinearLayout) view.getParent()).removeAllViews();
        } else if (view.getParent() instanceof RelativeLayout) {
            ((RelativeLayout) view.getParent()).removeAllViews();
        }
    }
}
