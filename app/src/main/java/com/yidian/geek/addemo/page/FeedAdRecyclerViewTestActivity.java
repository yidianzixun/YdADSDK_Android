package com.yidian.geek.addemo.page;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.yidian.adsdk.core.feedad.base.IYdAdDislike;
import com.yidian.adsdk.core.feedad.base.IYdNativeAd;
import com.yidian.adsdk.core.feedad.core.YdAdEngine;
import com.yidian.adsdk.core.feedad.core.YdAdRequest;
import com.yidian.adsdk.core.feedad.core.YdNativeExpressAd;
import com.yidian.adsdkdemo.R;
import com.yidian.geek.addemo.view.ILoadMoreListener;
import com.yidian.geek.addemo.view.LoadMoreRecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhangzhun
 * @data 2019/10/12
 */
public class FeedAdRecyclerViewTestActivity extends FragmentActivity implements View.OnClickListener {

    private static final int ITEM_COUNT = 30;
    private Button mLoadBtn;
    private LoadMoreRecyclerView mFeedAdRecyclerView;
    private List<YdNativeExpressAd> mNativeExpressAdList;
    private FeedAdAdapter feedAdAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_ad_recyclerview_test);
        mLoadBtn = findViewById(R.id.btnAdload);
        mLoadBtn.setOnClickListener(this);
        mFeedAdRecyclerView = findViewById(R.id.feedAdRecyclerView);
        mFeedAdRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mNativeExpressAdList = new ArrayList<>();
        feedAdAdapter = new FeedAdAdapter(this, mNativeExpressAdList);
        mFeedAdRecyclerView.setAdapter(feedAdAdapter);
        mFeedAdRecyclerView.setLoadMoreListener(new ILoadMoreListener() {
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
                if (mFeedAdRecyclerView != null) {
                    mFeedAdRecyclerView.setLoadingFinish();
                }
            }

            @Override
            public void onNativeExpressAdLoad(List<YdNativeExpressAd> nativeExpressAdList) {
                if (nativeExpressAdList != null && nativeExpressAdList.size() > 0) {
                    if (mFeedAdRecyclerView != null) {
                        mFeedAdRecyclerView.setLoadingFinish();
                    }
                    for (int i = 0; i < ITEM_COUNT; i++) {
                        mNativeExpressAdList.add(null);
                    }
                    int count = mNativeExpressAdList.size();

                    for (YdNativeExpressAd ad : nativeExpressAdList) {
                        int random = (int) (Math.random() * ITEM_COUNT) + count - ITEM_COUNT;
                        mNativeExpressAdList.set(random, ad);
                    }
                    feedAdAdapter.notifyDataSetChanged();
                    for (YdNativeExpressAd nativeExpressAd : nativeExpressAdList) {
                        nativeExpressAd.setOnShowAdViewListener(new IYdNativeAd.OnShowAdViewListener() {

                            @Override
                            public void onAdClicked(View view, int type) {
                                Toast.makeText(FeedAdRecyclerViewTestActivity.this, "点击广告", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onAdShow(View view, int type) {
                                Toast.makeText(FeedAdRecyclerViewTestActivity.this, "广告展示成功", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onAdShowFail(String msg, int code) {
                                Toast.makeText(FeedAdRecyclerViewTestActivity.this, "广告展示失败", Toast.LENGTH_SHORT).show();
                            }
                        });

                        nativeExpressAd.setOnDownloadListener(new IYdNativeAd.OnDownloadListener() {

                            @Override
                            public void onDownloadProgress(String packageName, int downloadProgress) {
                            }

                            @Override
                            public void onDownloadPaused(String packageName) {
                                Toast.makeText(FeedAdRecyclerViewTestActivity.this, "暂停下载：" + packageName, Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onDownloadFailed(String packageName) {
                                Toast.makeText(FeedAdRecyclerViewTestActivity.this, "下载失败：" + packageName, Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onDownloadFinished(String packageName) {
                                Toast.makeText(FeedAdRecyclerViewTestActivity.this, "下载完成：" + packageName, Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onInstalled(String packageName) {
                                Toast.makeText(FeedAdRecyclerViewTestActivity.this, "安装成功：" + packageName, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                } else {
                    Toast.makeText(FeedAdRecyclerViewTestActivity.this, "广告数据返回为空", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public static class FeedAdAdapter extends RecyclerView.Adapter {

        private static final int ADTYPE_LOAD_MORE = -1;
        private static final int ADTYPE_VIDEO = 1;
        private static final int ADTYPE_NORMAL = 2;

        private Context mContext;
        private List<YdNativeExpressAd> mNativeExpressAdList;


        public FeedAdAdapter(Context context, List<YdNativeExpressAd> data) {
            this.mContext = context;
            this.mNativeExpressAdList = data;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            RecyclerView.ViewHolder holder = null;
            switch (viewType) {
                case ADTYPE_VIDEO:
                    return new AdViewHolder(LayoutInflater.from(mContext).inflate(R.layout.cardview_ad, parent, false), this);
                default:
                    return new NormalViewHolder(LayoutInflater.from(mContext).inflate(R.layout.cardview_normal, parent, false));
            }
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof AdViewHolder) {
                ((AdViewHolder) holder).onBind(mNativeExpressAdList.get(position));
            }
        }

        @Override
        public int getItemViewType(int position) {
            if (mNativeExpressAdList != null) {
                int count = mNativeExpressAdList.size();

                if (position >= count) {
                    return ADTYPE_LOAD_MORE;
                } else {
                    YdNativeExpressAd ad = mNativeExpressAdList.get(position);
                    if (ad == null) {
                        return ADTYPE_NORMAL;
                    } else {
                        return ADTYPE_VIDEO;
                    }
                }
            }
            return super.getItemViewType(position);
        }

        @Override
        public int getItemCount() {
            return mNativeExpressAdList.size();
        }

        public List<YdNativeExpressAd> getData() {
            return mNativeExpressAdList;
        }
    }

    private static class NormalViewHolder extends RecyclerView.ViewHolder {

        public NormalViewHolder(View itemView) {
            super(itemView);

        }
    }

    private static class AdViewHolder extends RecyclerView.ViewHolder {


        private FrameLayout nativeContainer;
        private FeedAdAdapter adapter;

        public AdViewHolder(View itemView, FeedAdAdapter adapter) {
            super(itemView);
            this.adapter = adapter;
            nativeContainer = itemView.findViewById(R.id.nativeContainer);
        }

        public void onBind(YdNativeExpressAd nativeExpressAd) {
            nativeExpressAd.setDislikeCallback(new IYdAdDislike.DislikeInteractionCallback() {
                @Override
                public void onSelected() {
                    Toast.makeText(nativeContainer.getContext(), "点击负反馈", Toast.LENGTH_SHORT).show();
                    adapter.getData().remove(getAdapterPosition());
                    adapter.notifyItemRemoved(getAdapterPosition());
                }
            });
            //请务必调用此方法，不然影响广告收益
            nativeExpressAd.onbind(adapter);
            View video = nativeExpressAd.getAdView();
            if (video != null) {
                cleanParent(video);
                nativeContainer.removeAllViews();
                nativeContainer.addView(video);
            }
        }


        private void cleanParent(View view) {
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



}
