package com.hansen.componet.view.fragment.home;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.hansen.componet.R;
import com.hansen.componet.adapter.CourseAdapter;
import com.hansen.componet.module.recommand.BaseRecommandModel;
import com.hansen.componet.network.http.RequestCenter;
import com.hansen.componet.view.fragment.BaseFragment;
import com.hansen.hansensdk.okhttp.listener.DisposeDataListener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @author HanN on 2019/12/3 17:13
 * @email: 1356548475@qq.com
 * @project componet
 * @description:
 * @updateuser:
 * @updatedata: 2019/12/3 17:13
 * @updateremark:
 * @version: 2.1.67
 */
public class HomeFragment extends BaseFragment implements View.OnClickListener, AdapterView.OnItemClickListener {
    private static final String TAG = "HomeFragmentTAG";
    /**
     * UI
     */
    private View mContentView;
    private ListView mListView;
    private TextView mQRCodeView;
    private TextView mCategoryView;
    private TextView mSearchView;
    private ImageView mLoadingView;

    /**
     * 数据
     */
    private BaseRecommandModel mBaseRecommandModel;
    private CourseAdapter mCourseAdapter;

    public HomeFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestRecommandData();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = getActivity();

        mContentView = inflater.inflate(R.layout.fragment_home_layout, container, false);
        initView();
        return mContentView;
    }

    /**
     * 发送首页列表数据请求
     */
    private void requestRecommandData() {
        RequestCenter.requestRecommandData(new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                Log.v(TAG,responseObj.toString());
                /**
                 * 获取数据后更新ui
                 */
//                mBaseRecommandModel = (BaseRecommandModel) responseObj;

                showSucessView();
            }

            @Override
            public void onFailure(Object responseObj) {

            }
        });
    }

    /**
     * 请求成功后的方法
     */
    private void showSucessView() {
        //判断数据是否为空
        if (mBaseRecommandModel.data.list != null && mBaseRecommandModel.data.list.size() > 0) {
            mLoadingView.setVisibility(View.GONE);
            mListView.setVisibility(View.VISIBLE);
            //创建adapter
            mCourseAdapter = new CourseAdapter(getContext(),mBaseRecommandModel.data.list);
            mListView.setAdapter(mCourseAdapter);
        }else {
            showErrorView();
        }
    }

    /**
     * 请求失败后的方法
     */
    private void showErrorView() {

    }

    private void initView() {
        mQRCodeView = (TextView) mContentView.findViewById(R.id.qrcode_view);
        mQRCodeView.setOnClickListener(this);
        mCategoryView = (TextView) mContentView.findViewById(R.id.category_view);
        mCategoryView.setOnClickListener(this);
        mSearchView = (TextView) mContentView.findViewById(R.id.search_view);
        mSearchView.setOnClickListener(this);
        mListView = (ListView) mContentView.findViewById(R.id.list_view);
        mListView.setOnItemClickListener(this);
        mLoadingView = (ImageView) mContentView.findViewById(R.id.loading_view);
        AnimationDrawable anim = (AnimationDrawable) mLoadingView.getDrawable();
        anim.start();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
