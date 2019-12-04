package com.hansen.hansensdk.okhttp.listener;

/**
 * @author HanN on 2019/12/4 16:06
 * @email: 1356548475@qq.com
 * @project componet
 * @description: 对json数据解析
 * @updateuser:
 * @updatedata: 2019/12/4 16:06
 * @updateremark:
 * @version: 2.1.67
 */
public class DisposeDataHandle {
    public DisposeDataListener mListener = null;
    public Class<?> mClass = null;
    public String mSource = null;

    public DisposeDataHandle(DisposeDataListener mListener) {
        this.mListener = mListener;
    }

    public DisposeDataHandle(DisposeDataListener mListener, Class<?> mClass) {
        this.mListener = mListener;
        this.mClass = mClass;
    }

    public DisposeDataHandle(DisposeDataListener mListener, String mSource) {
        this.mListener = mListener;
        this.mSource = mSource;
    }
}
