package com.hansen.componet.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hansen.componet.R;
import com.hansen.componet.activity.base.BaseActivity;
import com.hansen.componet.view.fragment.home.HomeFragment;
import com.hansen.componet.view.fragment.message.MessageFragment;
import com.hansen.componet.view.fragment.mine.MineFragment;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

/**
 * @author HanN on 2019/12/3 16:53
 * @email: 1356548475@qq.com
 * @project componet
 * @description:
 * @updateuser:
 * @updatedata: 2019/12/3 16:53
 * @updateremark:
 * @version: 2.1.67
 */
public class HomeActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout mHomeLayout;
    private RelativeLayout mPondLayout;
    private RelativeLayout mMessageLayout;
    private RelativeLayout mMineLayout;
    private TextView mHomeView;
    private TextView mPondView;
    private TextView mMessageView;
    private TextView mMineView;

    private FragmentManager supportFragmentManager;
    private HomeFragment mHomeFragment;
    private MineFragment mMineFragment;
    private MessageFragment mMessageFragment;
    private Fragment mCurrent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initView();

        //显示首页homefragment
        mHomeFragment = new HomeFragment();
        supportFragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content_layout, mHomeFragment);
        fragmentTransaction.commit();
    }

    private void initView() {
        mHomeLayout = (RelativeLayout) findViewById(R.id.home_layout_view);
        mHomeLayout.setOnClickListener(this);
        mPondLayout = (RelativeLayout) findViewById(R.id.pond_layout_view);
        mPondLayout.setOnClickListener(this);
        mMessageLayout = (RelativeLayout) findViewById(R.id.message_layout_view);
        mMessageLayout.setOnClickListener(this);
        mMineLayout = (RelativeLayout) findViewById(R.id.mine_layout_view);
        mMineLayout.setOnClickListener(this);

        mHomeView = (TextView) findViewById(R.id.home_image_view);
        mPondView = (TextView) findViewById(R.id.fish_image_view);
        mMessageView = (TextView) findViewById(R.id.message_image_view);
        mMineView = (TextView) findViewById(R.id.mine_image_view);
        mHomeView.setBackgroundResource(R.mipmap.comui_tab_home_selected);
    }


    @Override
    public void onClick(View v) {
        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();

        switch (v.getId()) {
            case R.id.home_layout_view:
                mHomeView.setBackgroundResource(R.mipmap.comui_tab_home_selected);
                mPondView.setBackgroundResource(R.mipmap.comui_tab_pond);
                mMessageView.setBackgroundResource(R.mipmap.comui_tab_message);
                mMineView.setBackgroundResource(R.mipmap.comui_tab_person);
                //隐藏前提fragment
                hideFragment(mMessageFragment, fragmentTransaction);
                hideFragment(mMineFragment, fragmentTransaction);
                //显示HomeFragment
                if (mHomeFragment == null) {
                    mHomeFragment = new HomeFragment();
                    fragmentTransaction.add(R.id.content_layout, mHomeFragment);
                } else {
                    mCurrent = mHomeFragment;
                    fragmentTransaction.show(mHomeFragment);
                }

                break;
            case R.id.message_layout_view:
                mMessageView.setBackgroundResource(R.mipmap.comui_tab_message_selected);
                mHomeView.setBackgroundResource(R.mipmap.comui_tab_home);
                mPondView.setBackgroundResource(R.mipmap.comui_tab_pond);
                mMineView.setBackgroundResource(R.mipmap.comui_tab_person);

                //隐藏前提fragment
                hideFragment(mHomeFragment, fragmentTransaction);
                hideFragment(mMineFragment, fragmentTransaction);
                //显示HomeFragment
                if (mMessageFragment == null) {
                    mMessageFragment = new MessageFragment();
                    fragmentTransaction.add(R.id.content_layout, mMessageFragment);
                } else {
                    mCurrent = mMessageFragment;
                    fragmentTransaction.show(mMessageFragment);
                }
                break;
            case R.id.mine_layout_view:
                mMineView.setBackgroundResource(R.mipmap.comui_tab_person_selected);
                mHomeView.setBackgroundResource(R.mipmap.comui_tab_home);
                mPondView.setBackgroundResource(R.mipmap.comui_tab_pond);
                mMessageView.setBackgroundResource(R.mipmap.comui_tab_message);
                //隐藏前提fragment
                hideFragment(mHomeFragment, fragmentTransaction);
                hideFragment(mMessageFragment, fragmentTransaction);
                //显示HomeFragment
                if (mMineFragment == null) {
                    mMineFragment = new MineFragment();
                    fragmentTransaction.add(R.id.content_layout, mMineFragment);
                } else {
                    mCurrent = mMineFragment;
                    fragmentTransaction.show(mMineFragment);
                }
                break;
            default:
        }
        fragmentTransaction.commit();
    }


    /**
     * 隐藏其他fragment
     *
     * @param fragment
     * @param ft
     */
    private void hideFragment(Fragment fragment, FragmentTransaction ft) {
        if (fragment != null) {
            ft.hide(fragment);
        }
    }

}
