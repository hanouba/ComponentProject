package com.hansen.componet.application;

import android.app.Application;

/**
 * @author HanN on 2019/12/3 16:38
 * @email: 1356548475@qq.com
 * @project componet
 * @description: 程序入口 第三方模块初始化 3提供上下文环境
 * @updateuser:
 * @updatedata: 2019/12/3 16:38
 * @updateremark:
 * @version: 2.1.67
 */
public class HanApplication extends Application {
    private static HanApplication mApplication = null;

    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = this;
    }

    public static HanApplication getInstance() {
        return mApplication;
    }

}
