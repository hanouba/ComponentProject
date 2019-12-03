package com.hansen.componet.activity.base;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

/**
 * @author HanN on 2019/12/3 16:47
 * @email: 1356548475@qq.com
 * @project componet
 * @description:
 * @updateuser:
 * @updatedata: 2019/12/3 16:47
 * @updateremark:
 * @version: 2.1.67
 */
public abstract class BaseActivity extends AppCompatActivity {
    public String TAG="";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TAG = getComponentName().getShortClassName();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
