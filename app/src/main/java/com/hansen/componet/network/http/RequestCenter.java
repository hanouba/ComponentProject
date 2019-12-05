package com.hansen.componet.network.http;

import com.hansen.hansensdk.okhttp.CommonOkHttpClient;
import com.hansen.hansensdk.okhttp.listener.DisposeDataHandle;
import com.hansen.hansensdk.okhttp.listener.DisposeDataListener;
import com.hansen.hansensdk.okhttp.request.CommonRequest;
import com.hansen.hansensdk.okhttp.request.RequestParams;

/**
 * @author HanN on 2019/12/5 11:00
 * @email: 1356548475@qq.com
 * @project componet
 * @description: 存放应用中所以的请求
 * @updateuser:
 * @updatedata: 2019/12/5 11:00
 * @updateremark:
 * @version: 2.1.67
 */
public class RequestCenter {
    //    根据参数发送所有post
    private static void postRequest(String url, RequestParams params, DisposeDataListener listener, Class<?> clazz) {
        CommonOkHttpClient.get(CommonRequest.createGetRequest(url, params), new DisposeDataHandle(listener, clazz));
    }

    /**
     * 正真的发送我们的首页请求
     *
     * @param listener
     */
    public static void requestRecommandData(DisposeDataListener listener) {
        RequestCenter.postRequest(HttpContants.HOME_RECOMMAND, null, listener, null);
    }
}
