# 一点资讯广告SDK-Android

<p align="center">
   <a href="https://jcenter.bintray.com/com/yidian/android/adsdk/1.2.1/">
    <img src="https://img.shields.io/badge/Jcenter-v1.2.1-brightgreen.svg?style=flat-square" alt="Latest Stable Version" />

  </a>
  <a href="https://developer.android.com/about/versions/android-4.1.html">
    <img src="https://img.shields.io/badge/API-16%2B-blue.svg?style=flat-square" alt="Min Sdk Version" />
  </a>
</p>



## 阅读对象
本文档面向所有使用一点资讯广告SDK的开发、测试人员等, 要求读者具有一定的Android编程开发经验。

<p>
	<img src="http://si1.go2yd.com/get-image/0XNCGH6mRRA" width="40%" height="40%"/>
		<img src="http://si1.go2yd.com/get-image/0bURVug6eOW" width="40%" height="40%"/>

</p>


## 1.产品概述

一点资讯Android 广告SDK是一点资讯Android开发团队推出的Android平台上的广告开发集成包(SDK)，为Android开发者提供简单、快捷的接口，帮助开发者快速实现Android平台上的开屏。

### 1.1 Demo
下载地址：[点击下载](https://download.yidianzixun.com/android/x/adsdk-demo1.2.1_20fd1b5.apk)


## 2.SDK 功能说明

- [1] 提供完整开屏广告
- [2] 提供完整信息流广告，大图、组图、视频、下载类广告

## 3.SDK使用

### 3.1 Download

#### jcenter
* 1.先在工程目录下的build.gradle的repositories添加：

``` gradle
 buildscript {

    repositories {
        google()
        jcenter()
        maven { url "https://jitpack.io" }
        maven { url "https://dl.bintray.com/yidian-android/open_android_sdk/" }
    }
    dependencies {
        classpath 'com.android.tools.build:gradle:3.0.0'
        classpath 'org.greenrobot:greendao-gradle-plugin:3.2.2' // 一定要添加，greendao使用
    }
}
```

**备注**：SDK使用到了greendao数据库框架，开发者需要在dependencies中进行配置

* 2.然后在app的build.gradle的denpendencies中添加：


``` gradle
dependencies {
   //必选项
    implementation "org.greenrobot:greendao:3.2.2"
    implementation("com.android.support:recyclerview-v7:${rootProject.ext.supportVersion}")
    implementation 'com.yidian.android:adsdk:1.2.0'
     implementation "pl.droidsonroids.gif:android-gif-drawable:${rootProject.ext.gifDrawable}"
}
```
**备注**：广告SDK长期持续更新迭代，为了稳定性及新特性，请开发者依赖最新版本，查看各个版本更新信息请点击[版本更新记录](https://github.com/yidianzixun/YdADSDK_Android/releases)

### 3.2 SDK初始化
com.yidian.adsdk.AdSDK.java 这是SDK的配置入口类，目前对外提供了响应的配置方法，开发者可以通过配置

private String mAppKey;
SDK初始化所需要的APP_KEY

private String mAppId;
SDK初始化所需的APP_ID

private boolean debug;
是否开启DEBUG模式，开启Debug模式后会输出更多的log信息。

在自定义Application的onCreate中添加如下代码，初始化我们的SDK。由于您的应用可能不止一个进程，建议只在主进程初始化我们的SDK。
SDK初始化示例：

``` java
    new AdSDK.Builder()
                .setAppId(BuildConfig.AD_APP_ID)//申请的appid
                .setAppKey(BuildConfig.AD_APP_KEY)//申请的appkey
                .setContext(getApplicationContext())
                .setDebugEnabled(BuildConfig.DEBUG)//是否开启debug日志
                .setCustomMediaPlayer(new YdMediaIjkPlayer())//默认使用系统播放器，这里可以设置使用IjkPlayer（加载信息流广告时使用）
                .build();
```
您需要在SDK初始化代码中传入APP_ID、APP_KEY。

### 3.3 SDK接入使用

#### 3.3.1 开屏广告接入

接入方需要在闪屏Activity的主线程中，调用如下代码完成开屏SDK的接入：

``` java
new SplashAD.Builder()
            .setActivity(this)
            .setAdContainerRes(R.id.adContainer)
            .setSplashlogo(R.mipmap.ic_launcher)
            .setSplashADListener(this)
            .show();

```

| 方法    | 描述|
| :---: | :---:|
| setActivity（Activity activity） | 设置Context|
| setAdContainerRes(@IdRes int adContainer)  | 设置开屏广告的容器 |
| setSplashADListener(SplashADListener splashADListener)  | 设置开屏SDK的回调接口 |
| setSplashlogo(@DrawableRes int splashlogo) | 设置需要展示的logo  |
| setBottomViewDismiss(boolean bottomViewDismiss) | 设置是否需要隐藏底栏  |

SplashAdListener接口方法如下：

``` java
//1.1.0以下版本
public interface SplashADListener {

    /**
     * 广告消失的时候回调该方法：
     * 1、跳过广告
     * 2、点击广告进入广告落地页且从落地页back回去
     */
    void onADDismiss();

    /**
     * 广告展示时回调
     */
    void onADPresent();

    /**
     * 点击开屏广告时回调
     */
    void onADScreenClick();

    /**
     * 广告展示失败时回调
     */
    void onADFail();
}
```

``` java
//1.1.0及以上版本
public interface SplashADListener {

    /**
     * 广告展示时间结束
     */
    void onADTimeOver();

    /**
     * 点击跳过按钮
     */
    void onADSkip();

    /**
     * 广告展示时回调
     */
    void onADPresent();

    /**
     * 点击开屏广告时回调
     */
    void onADScreenClick();

    /**
     * 广告展示失败时回调
     */
    void onADFail();
}
```

#### 3.3.2 信息流广告接入


开发者在合适的代码位置进行信息流广告请求，一般在自己的请求逻辑中添加：

``` java
        YdAdRequest ydAdRequest = YdAdRequest.newBuilder()
                .reqestAdCount(2)
                .build();
        new YdAdEngine(this).loadNativeExpressAd(ydAdRequest, new IYdNativeAd.OnLoadNativeExpressAdListener() {

            @Override
            public void onError(int errorCode, String errorMessage) {
                //信息流广告请求失败
            }

            @Override
            public void onNativeExpressAdLoad(List<YdNativeExpressAd> nativeExpressAdList) {
               //返回信息流广告列表
        });
```

YdNativeExpressAd为信息流广告的基本单位，YdNativeExpressAd继承自接口IYdNativeAd，IYdNativeAd类如下：

``` java
public interface IYdNativeAd {

    /**
     * 返回广告VIEW
     * @return
     */
    View getAdView();

    /**
     * 返回广告模板号
     * @return
     */
    int getAdTemplateId();

    /**
     * 设置信息流广告负反馈接口
     * @param dislikeCallback
     */
    void setDislikeCallback(IYdAdDislike.DislikeInteractionCallback dislikeCallback);

    /**
     * 设置信息流广告回调接口，如点击、展示、展示失败
     * @param onShowAdViewListener
     */
    void setOnShowAdViewListener(OnShowAdViewListener onShowAdViewListener);

     /**
      * 设置信息流广告的下载回调接口
      * @param onDownloadListener
      */
    void setOnDownloadListener(OnDownloadListener onDownloadListener);
}

```

为返回的YdNativeExpressAd可以设置相关回调接口，如负反馈、下载、展示等。

#### 接入步骤

1、为广告设置展示类回调接口

``` java
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
```

2、为广告设置负反馈接口

``` java
nativeExpressAd.setDislikeCallback(new IYdAdDislike.DislikeInteractionCallback() {
                @Override
                public void onSelected() {
                    Toast.makeText(nativeContainer.getContext(), "点击负反馈", Toast.LENGTH_SHORT).show();
                    adapter.getData().remove(getAdapterPosition());
                    adapter.notifyItemRemoved(getAdapterPosition());
                }
            });
```

3、为下载类广告设置下载回调接口

``` java
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
```

4、在合适的位置添加广告，如ListView、RecyclerView、ViewGroup等，如在ViewHolder的onBind方法中，可以参考如下代码:

``` java
 public void onBind(YdNativeExpressAd nativeExpressAd) {
           //绑定adapter
            nativeExpressAd.onbind(adapter);
            View video = nativeExpressAd.getAdView();
            if (video != null) {
                if (video.getParent() == null) {
                    nativeContainer.removeAllViews();
                    //请务必调用此方法，不然影响广告收益
            		 nativeExpressAd.onbind(adapter);
                    //添加广告View
                    nativeContainer.addView(video);
                }
            }
        }
```

具体的实现可以参考demo中的FeedAdRecyclerViewTestActivity、FeedAdListViewTestActivity、FeedAdTestActivity。

## 4.其他
### 4.1 声明必要权限
客户端需要在AndroidManifest.xml文件中增加SDK所需要的访问网络等权限，代码示例：

``` xml
 <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.READ_PHONE_STATE" />
    <uses-permission android:name="android.permission.REQUEST_INSTALL_PACKAGES" />
```

### 4.2 关于混淆

确保不要混淆SDK的代码：

``` java
-keep class com.yidian.adsdk.** {*;}
```

## 5.反馈与建议
### 5.1 反馈模板  

| 类型    | 描述|
| :---: | :---:| 
| SDK版本 | v1.0.0|
| 设备型号  | 小米 8  |
| OS版本  | Android 8.0.0 |
| 问题描述  | 描述问题出现的现象  |
| 操作描述  | 描述经过如何操作出现上述问题                     |
| 额外附件   | 文本形式控制台log、crash报告、其他辅助信息（界面截屏或录像等） |
