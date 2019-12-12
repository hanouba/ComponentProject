package com.hansen.hansensdk.okhttp.response;

import android.os.Handler;
import android.os.Looper;

import com.hansen.hansensdk.adutil.ResponseEntityToModule;
import com.hansen.hansensdk.okhttp.exception.OkHttpException;
import com.hansen.hansensdk.okhttp.listener.DisposeDataHandle;
import com.hansen.hansensdk.okhttp.listener.DisposeDataListener;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * @author HanN on 2019/12/4 16:01
 * @email: 1356548475@qq.com
 * @project componet
 * @description: 专门处理json的回调响应
 * @updateuser:
 * @updatedata: 2019/12/4 16:01
 * @updateremark:
 * @version: 2.1.67
 */
public class CommonJsonCallback implements Callback {
    //与服务器返回的字段的一个对应关系
    protected final String RESULT_CODE = "ecode"; // 有返回则对于http请求来说是成功的，但还有可能是业务逻辑上的错误
    protected final int RESULT_CODE_VALUE = 0;
    protected final String ERROR_MSG = "emsg";
    protected final String EMPTY_MSG = "";
    protected final String COOKIE_STORE = "Set-Cookie"; // decide the server it
    /**
     * 自定义类型的
     */
    protected final int NETWORK_ERROR = -1; // the network relative error
    protected final int JSON_ERROR = -2; // the JSON relative error
    protected final int OTHER_ERROR = -3; // the unknow error

    /**
     * 将其他线程的数据转发到UI线程
     */
    private Handler mDeliveryHandler;
    private DisposeDataListener mListener;
    private Class<?> mClass;

    public CommonJsonCallback(DisposeDataHandle handle) {
        //初始化mDeliveryHandler
        this.mDeliveryHandler = new Handler(Looper.getMainLooper());
        this.mListener = handle.mListener;
        this.mClass = handle.mClass;
    }

    @Override
    public void onFailure(@NotNull Call call, @NotNull final IOException ioException) {
        /**
         * 非ui线程 转发到主线程
         */
        mDeliveryHandler.post(new Runnable() {
            @Override
            public void run() {
                mListener.onFailure(new OkHttpException(NETWORK_ERROR,ioException));
            }
        });
    }

    @Override
    public void onResponse(@NotNull Call call, @NotNull final Response response) throws IOException {
        final String result = response.body().string();
        mDeliveryHandler.post(new Runnable() {
            @Override
            public void run() {
                handlerResponse(result);
            }
        });
    }

    private void handlerResponse(Object responseObj) {
        //为了保证代码的健壮性
        if (responseObj == null && responseObj.toString().trim().equals("")) {
            mListener.onFailure(new OkHttpException(NETWORK_ERROR,EMPTY_MSG));
            return;
        }

        try {
            JSONObject result = new JSONObject(responseObj.toString());

            if (result.has(RESULT_CODE)) {
                if (result.getInt(RESULT_CODE) == RESULT_CODE_VALUE) {
                    if (mClass == null) {
                        mListener.onSuccess(responseObj);
                    }else {
                        //需要将json转化
                        Object object = ResponseEntityToModule.parseJsonObjectToModule(result,mClass);
//                        Gson gson = new Gson();
//                        Object object = gson.fromJson(responseObj.toString(), mClass);
                        if (object != null) {
                            mListener.onSuccess(object);
                        }else {
                            mListener.onFailure(new OkHttpException(JSON_ERROR,EMPTY_MSG));
                        }
                    }
                }else {
                    //将服务器返回给我们的异常回调到应用层去处理
                    mListener.onFailure(new OkHttpException(OTHER_ERROR,RESULT_CODE));
                }
            }
        } catch (JSONException e) {
            mListener.onFailure(new OkHttpException(OTHER_ERROR,e.getMessage()));
            e.printStackTrace();
        }
    }
}
