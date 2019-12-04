package com.hansen.hansensdk.okhttp.exception;

/**
 * @author HanN on 2019/12/4 16:13
 * @email: 1356548475@qq.com
 * @project componet
 * @description: 自定义异常放回类型 ,放回ecode emsg到业务层
 * @updateuser:
 * @updatedata: 2019/12/4 16:13
 * @updateremark:
 * @version: 2.1.67
 */
public class OkHttpException extends Exception{
    private static final long serialVersionUID = 1L;

    private int ecode;
    private Object emsg;

    public OkHttpException(int ecode, Object emsg) {
        this.ecode = ecode;
        this.emsg = emsg;
    }

    public int getEcode() {
        return ecode;
    }

    public void setEcode(int ecode) {
        this.ecode = ecode;
    }

    public Object getEmsg() {
        return emsg;
    }

    public void setEmsg(Object emsg) {
        this.emsg = emsg;
    }
}
