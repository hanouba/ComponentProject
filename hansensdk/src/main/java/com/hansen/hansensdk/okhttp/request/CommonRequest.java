package com.hansen.hansensdk.okhttp.request;

import android.icu.util.ULocale;

import java.util.Map;

import okhttp3.FormBody;
import okhttp3.Request;

/**
 * @author HanN on 2019/12/4 13:51
 * @email: 1356548475@qq.com
 * @project componet
 * @description: 接收参数, 为我们生产Request对象
 * @updateuser:
 * @updatedata: 2019/12/4 13:51
 * @updateremark:
 * @version: 2.1.67
 */
public class CommonRequest {
    /**
     * @param url
     * @param params
     * @return 返回一个创建好的request对象
     */
    public static Request createPostRequest(String url, RequestParams params) {
        //构建者模式
        FormBody.Builder mFormBodyBuild = new FormBody.Builder();
        if (params != null) {
            for (Map.Entry<String, String> entry : params.urlParams.entrySet()) {
                //将请求参数遍历添加到我们的请求构建类中
                mFormBodyBuild.add(entry.getKey(), entry.getValue());
            }
        }
        //通过请求构建类的build方法获取到正真的请求体对象
        FormBody mFormBody = mFormBodyBuild.build();

        return new Request.Builder().url(url).post(mFormBody).build();
    }


    /**
     * @param url
     * @param params
     * @return 通过传入的参数, 返回一个get类型的请求
     */
    public static Request createGetReequest(String url, RequestParams params) {
        StringBuilder urlStringBuild = new StringBuilder(url).append("?");
        if (params != null) {
            for (Map.Entry<String, String> entry : params.urlParams.entrySet()) {
                //将请求参数遍历添加到我们的请求构建类中
                urlStringBuild.append(entry.getKey()).append("=")
                        .append(entry.getValue())
                        .append("&");
            }

        }
        return new Request.Builder().url(urlStringBuild.substring(0, urlStringBuild.length() - 1))
                .get().build();
    }
}
