package com.example.vvxc.guangzhoubus;

import android.app.Application;

import com.baidu.apistore.sdk.ApiStoreSDK;

/**
 * Created by vvxc on 2016/5/16.
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        // TODO 您的其他初始化流程
        ApiStoreSDK.init(this, Constants.APPKEY);
        super.onCreate();
    }
}
