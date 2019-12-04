package com.hansen.hansensdk.okhttp.listener;

/**
 * @author HanN on 2019/12/4 15:58
 * @email: 1356548475@qq.com
 * @project componet
 * @description: 自定义事件监听 避免OKhttp resonance被修改
 *          方便扩展
 * @updateuser:
 * @updatedata: 2019/12/4 15:58
 * @updateremark:
 * @version: 2.1.67
 */
public interface DisposeDataListener {
    /**
     * 请求成功
     * @param responseObj
     */
    public void onSuccess(Object responseObj);

    /**
     * 请求失败
     * @param responseObj
     */
    public void onFailure(Object responseObj);

}
