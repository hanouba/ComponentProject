package com.hansen.componet.view.fragment.message;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hansen.componet.R;
import com.hansen.componet.view.fragment.BaseFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @author HanN on 2019/12/3 17:45
 * @email: 1356548475@qq.com
 * @project componet
 * @description:
 * @updateuser:
 * @updatedata: 2019/12/3 17:45
 * @updateremark:
 * @version: 2.1.67
 */
public class MessageFragment extends BaseFragment {
    /**
     * UI
     */
    private View mContentView;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = getActivity();
        mContentView = inflater.inflate(R.layout.fragment_message_layout, container, false);
        initView();
        return mContentView;
    }

    private void initView() {

    }
}
