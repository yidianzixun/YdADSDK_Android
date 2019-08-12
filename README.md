# 一点资讯开屏广告SDK-Android

<p align="center">
   <a href="https://jcenter.bintray.com/com/yidian/android/adsdk/1.0.0/">
    <img src="https://img.shields.io/badge/Jcenter-v1.0.0-brightgreen.svg?style=flat-square" alt="Latest Stable Version" />

  </a>
  <a href="https://developer.android.com/about/versions/android-4.1.html">
    <img src="https://img.shields.io/badge/API-16%2B-blue.svg?style=flat-square" alt="Min Sdk Version" />
  </a>
</p>



## 阅读对象
本文档面向所有使用一点资讯开屏SDK的开发、测试人员等, 要求读者具有一定的Android编程开发经验。

<p>
	<img src="http://si1.go2yd.com/get-image/0XNCGH6mRRA" width="70%" height="70%"/>
</p>


## 1.产品概述

一点资讯开放平台Android SDK是一点资讯Android开发团队推出的Android平台上的开屏广告开发集成包(SDK)，为Android开发者提供简单、快捷的接口，帮助开发者快速实现Android平台上的开屏。

### 1.1 Demo
下载地址：[点击下载](https://download.yidianzixun.com/android/adsdk-demo1.0.0_85cd28b.apk)


## 2.SDK 功能说明

- [1] 提供完整开屏展示逻辑
- [2] 一键接入，简单快捷

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
    implementation 'com.yidian.android:adsdk:1.0.0'
}
```
**备注**：开放平台SDK一直在更新迭代，为了稳定性及新特性，请开发者依赖最新版本，查看各个版本更新信息请点击[版本更新记录](https://github.com/yidianzixun/YdSplashADSDK_Android/releases)

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
         .setAppId(BuildConfig.AD_APP_ID)
         .setAppKey(BuildConfig.AD_APP_KEY)
         .setContext(getApplicationContext())
         .setDebugEnabled(BuildConfig.DEBUG)
         .build();
```
您需要在SDK初始化代码中传入APP_ID、APP_KEY。

### 3.3 SDK接入使用

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



## 4.其他
### 4.1 声明必要权限
客户端需要在AndroidManifest.xml文件中增加SDK所需要的访问网络等权限，代码示例：

``` xml
    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.READ_PHONE_STATE" />
```

### 4.2 关于混淆

SDK提供时已经通过consumerProguardFile 方式提供混淆文件，所以客户端不需要重新keep防止混淆

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
