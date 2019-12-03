package com.hansen.componet.constant;

import android.Manifest;
import android.os.Environment;

/**
 * @author HanN on 2019/12/3 17:21
 * @email: 1356548475@qq.com
 * @project componet
 * @description: 常量
 * @updateuser:
 * @updatedata: 2019/12/3 17:21
 * @updateremark:
 * @version: 2.1.67
 */
public class Constant {
    /**
     * 权限常量相关
     */
    public static final int WRITE_READ_EXTERNAL_CODE = 0x01;
    public static final String[] WRITE_READ_EXTERNAL_PERMISSION = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE};

    public static final int HARDWEAR_CAMERA_CODE = 0x02;
    public static final String[] HARDWEAR_CAMERA_PERMISSION = new String[]{Manifest.permission.CAMERA};

    //整个应用文件下载保存路径
    public static String APP_PHOTO_DIR = Environment.
            getExternalStorageState().
            concat("/imooc_business/photo/");
}
