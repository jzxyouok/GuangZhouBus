package com.example.vvxc.guangzhoubus.myApi;

import com.baidu.apistore.sdk.ApiCallBack;
import com.baidu.apistore.sdk.ApiStoreSDK;
import com.baidu.apistore.sdk.network.Parameters;
import com.example.vvxc.guangzhoubus.Constants;

import java.net.ContentHandler;

/**
 * Created by vvxc on 2016/5/20.
 */
public class MyApi extends ApiStoreSDK {
    public static void  getBusData(String bus,String direction,ApiCallBack apiCallBack){
        Parameters para = new Parameters();
        para.put("city", "广州");
        para.put("bus", bus);
        para.put("direction", direction);
        ApiStoreSDK.execute(Constants.URL,
                ApiStoreSDK.GET,
                para,
                apiCallBack);
    }
}
