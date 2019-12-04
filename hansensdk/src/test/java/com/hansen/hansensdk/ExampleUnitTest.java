package com.hansen.hansensdk;

import com.hansen.hansensdk.okhttp.CommonOkHttpClient;
import com.hansen.hansensdk.okhttp.listener.DisposeDataHandle;
import com.hansen.hansensdk.okhttp.listener.DisposeDataListener;
import com.hansen.hansensdk.okhttp.request.CommonRequest;
import com.hansen.hansensdk.okhttp.response.CommonJsonCallback;

import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test

    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);

        CommonOkHttpClient.sendRequest(CommonRequest.createGetReequest(
                "http:www.baidu.com",null),new CommonJsonCallback(
                        new DisposeDataHandle(new DisposeDataListener() {
                            @Override
                            public void onSuccess(Object responseObj) {

                            }

                            @Override
                            public void onFailure(Object responseObj) {

                            }
                        })
                )
        ));
    }
}