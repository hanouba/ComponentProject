package com.hansen.hansensdk.widget;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioManager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.hansen.hansensdk.R;
import com.hansen.hansensdk.adutil.LogUtils;
import com.hansen.hansensdk.constant.SDKConstant;

/**
 * @author HanN on 2019/12/11 16:04
 * @email: 1356548475@qq.com
 * @project componet
 * @description: 自定义视频播放view
 * @updateuser:
 * @updatedata: 2019/12/11 16:04
 * @updateremark:
 * @version: 2.1.67
 */
public class MyVideoView extends RelativeLayout implements View.OnClickListener, TextureView.SurfaceTextureListener {
    private static final String TAG = "MyVideoView";
    //    自定义view所在的上级viewgroup 这个容器是用来显示视频的某个RelativeLayout
    private ViewGroup mParentContainer;
    private AudioManager audioManager; //音量控制

    //屏幕宽度
    private int mScreenWidth;
    private int mVideoViewHeight;

    //    ui
    private RelativeLayout mPlayerView;
    private TextureView mVideoView;
    private ImageView mFullView;
    private ImageView mErrorView;
    private ImageView mLoadingView;
    private Button mPlayButton;
    //
    //状态
    private static final int STATE_ERROR = -1;
    private static final int STATE_IDLE = 0;
    private static final int STATE_PLAYING = 1;
    private static final int STATE_PAUSING = 2;
    private int playState = STATE_IDLE;

    public MyVideoView(Context context) {
        this(context, null);
    }

    public MyVideoView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //初始化音量管理工具
        audioManager = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
        initData();
        initView();
    }

    private void initView() {
        LayoutInflater layoutInflater = LayoutInflater.from(this.getContext());
        mPlayerView = (RelativeLayout) layoutInflater.inflate(R.layout.xadsdk_video_player_my, this);
        mVideoView = mPlayerView.findViewById(R.id.ttv_video);
        mVideoView.setOnClickListener(this);
        mVideoView.setKeepScreenOn(true);
        mVideoView.setSurfaceTextureListener(this);

        initSmallWindowState();
    }

    /**
     * 小窗口状态
     */
    private void initSmallWindowState() {
        mFullView = mPlayerView.findViewById(R.id.xadsdk_full_view);
        mErrorView = mPlayerView.findViewById(R.id.iv_framing_view);
        mLoadingView = mPlayerView.findViewById(R.id.xadsdk_view_loading);
        mPlayButton = mPlayerView.findViewById(R.id.xadsdk_play_btn);

        mFullView.setOnClickListener(this);
        mPlayButton.setOnClickListener(this);
    }

    /**
     * 设置视频播放器宽高
     */
    private void initData() {
        //获取屏幕宽度作为宽度
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(dm);
        mScreenWidth = dm.widthPixels;
        //根据比例16/9设置高度
        mVideoViewHeight = (int) (mScreenWidth * SDKConstant.VIDEO_HEIGHT_PERCENT);
    }

    @Override
    public void onClick(View v) {
        if (v == this.mPlayButton) {
            if (this.playState == STATE_PAUSING) {

            }else {
                load();
            }
        } else if (v == this.mFullView) {

        } else if (v == this.mVideoView) {

        }
    }

    /**
     * 加载播放器
     */
    private void load() {
//        播放状态必须是初始话状态
        if (this.playState != STATE_IDLE) {
            return;
        }
//        先显示加载动画
        showLoadingView();
    }

    /**
     * 显示加载动画
     */
    private void showLoadingView() {
        mLoadingView.setVisibility(VISIBLE);
        mFullView.setVisibility(GONE);
        mPlayButton.setVisibility(GONE);
        mErrorView.setVisibility(GONE);
        //显示动画
        AnimationDrawable anim = (AnimationDrawable) mLoadingView.getBackground();
        anim.start();

    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        LogUtils.d(TAG, "onSurfaceTextureAvailable" + width + "-height-" + height);
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
        LogUtils.d(TAG, "onSurfaceTextureSizeChanged" + width + "-height-" + height);
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        LogUtils.d(TAG, "onSurfaceTextureDestroyed");
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {
        LogUtils.d(TAG, "onSurfaceTextureUpdated");
    }
}
