package com.hansen.hansensdk.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.SurfaceTexture;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
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
import com.hansen.hansensdk.adutil.Utils;
import com.hansen.hansensdk.constant.SDKConstant;
import com.hansen.hansensdk.core.AdParameters;

import java.io.IOException;

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
public class MyVideoView extends RelativeLayout implements View.OnClickListener, TextureView.SurfaceTextureListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnInfoListener, MediaPlayer.OnErrorListener {
    private static final String TAG = "MyVideoView";
    //    自定义view所在的上级viewgroup 这个容器是用来显示视频的某个RelativeLayout
    private ViewGroup mParentContainer;
    private AudioManager audioManager; //音量控制
    //事件类型
    private static final int TIME_MSG = 0x01;
    private static final int TIME_INVAL = 1000;
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

    //加载失败重试次数
    private static final int LOAD_TOTAL_COUNT = 3;
    private int mCurrentCount;
    private MediaPlayer mediaPlayer;
    private ADFrameImageLoadListener mFrameImageLoadListener;
    private ADVideoPlayerListener mAVideoPlayerListener;
    private String mFramUrl; //某一帧的画面地址
    private String mUrl;
    private boolean canPlay;
    private boolean mIsRealPause;
    private boolean mIsComplete;
    private boolean isMute;//是否静音
    public void setmAVideoPlayerListener(ADVideoPlayerListener mAVideoPlayerListener) {
        this.mAVideoPlayerListener = mAVideoPlayerListener;
    }

    /**
     * 每隔1秒通知实现者
     */
    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case TIME_MSG:
                    if (isPlaying()) {
                        //还可以在这里更新progressbar
                        //LogUtils.i(TAG, "TIME_MSG");
                        mAVideoPlayerListener.onBufferUpdate(getCurrentPosition());
                        sendEmptyMessageDelayed(TIME_MSG, TIME_INVAL);
                    }
                    break;
            }
        }
    };

    /**
     * 获取当前帧位置
     * @return
     */
    private int getCurrentPosition() {
        if (mediaPlayer != null) {
          return   mediaPlayer.getCurrentPosition();
        }
        return 0;
    }

    private boolean isPlaying() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            return true;

        }
        return false;
    }

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
                if (Utils.getVisiblePercent(mParentContainer) > SDKConstant.VIDEO_SCREEN_PERCENT) {
                    resume();
                    //通知点击事件
                    this.mAVideoPlayerListener.onClickPlay();
                }
            }else {
                load();
            }
        } else if (v == this.mFullView) {
            this.mAVideoPlayerListener.onClickFullScreenBtn();
        } else if (v == this.mVideoView) {
            this.mAVideoPlayerListener.onClickVideo();
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
        setCurrentPlayState(STATE_IDLE);
        //检查播放器状态

        try {
            checkMediaPlayer();
            mute(true);
            mediaPlayer.setDataSource(this.mUrl);

        } catch (IOException e) {

            stop();
        }

    }

    /**
     * 设置播放地址
     * @param url
     */
    public void setDataSource(String url) {
        this.mUrl = url;
    }

    /**
     * 声音设置 是否静音
     * @param b
     */
    private void mute(boolean b) {
        isMute = b;
        if (mediaPlayer != null && this.audioManager != null) {
            float  volume = isMute ? 0.0f : 1.0f;
            mediaPlayer.setVolume(volume,volume);
        }
    }

    private synchronized void checkMediaPlayer() {
        if (mediaPlayer == null) {
            mediaPlayer = createMediaPlayer();
        }
    }

    /**
     * 创建mediaplayer
     * @return
     */
    private MediaPlayer createMediaPlayer() {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.reset();
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.setOnInfoListener(this);
        mediaPlayer.setOnErrorListener(this);
        return mediaPlayer;
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

    /**
     * 跳转到指定节点
     * @param postion
     */
    public void seekAndResume(int postion) {
        if (mediaPlayer != null) {
            showPauseView(true) ;
            entryResumeState();
            mediaPlayer.seekTo(postion);
            mediaPlayer.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
                @Override
                public void onSeekComplete(MediaPlayer mp) {
                    //开始播放
                    mediaPlayer.start();
                    //通知
                    mHandler.sendEmptyMessage(TIME_MSG);
                }
            });
        }
    }

    /**
     * 进入播放状态时更新
     */
    public void entryResumeState() {
        canPlay = true;
        setCurrentPlayState(STATE_PLAYING);
        setIsRealPause(false);
        setIsComplete(false);
    }

    /**
     *是否是真的暂停
     * @param isRealPause
     */
    public void setIsRealPause(boolean isRealPause) {
        this.mIsRealPause = isRealPause;
    }

    /**
     * 是否完成
     * @param isComplete
     */
    public void setIsComplete(boolean isComplete) {
        mIsComplete = isComplete;
    }
    /**
     * 设置当前播放状态
     * @param state
     */
    private void setCurrentPlayState(int state) {
        playState = state;
    }

    /**
     *显示暂停
     * @param show
     */
    private void showPauseView(boolean show) {
        mFullView.setVisibility(show ? VISIBLE : GONE);
        mPlayButton.setVisibility(show ? GONE : VISIBLE);
        mLoadingView.clearAnimation();
        mLoadingView.setVisibility(GONE);
        if (!show) {
            mErrorView.setVisibility(VISIBLE);
            loadErrorImage();
        }else {
            mErrorView.setVisibility(GONE);
        }
    }

    /**
     * 异步加载定帧动画
     */
    private void loadErrorImage() {
        if (mFrameImageLoadListener != null) {
            mFrameImageLoadListener.onStartFrameLoad(mFramUrl, new ImageLoaderListener() {
                @Override
                public void onLoadingComplete(Bitmap loadedImage) {
                    if (loadedImage != null) {
                        mErrorView.setScaleType(ImageView.ScaleType.FIT_XY);
                        mErrorView.setImageBitmap(loadedImage);
                    }else {
                        mErrorView.setScaleType(ImageView.ScaleType.FIT_XY);
                        mErrorView.setImageResource(R.drawable.xadsdk_img_error);
                    }
                }
            });
        }
    }

    /**
     * 1 显示播放界面
     * 2 重新给mediaplayer 赋值 监听后的
     *  3 设置bufferup监听
     *  4 广告加载成功监听
     *  5 判断是否是自动播放
     * @param mp
     */
    @Override
    public void onPrepared(MediaPlayer mp) {
        showPlayView();
        mediaPlayer = mp;
        if (mediaPlayer != null) {
            mediaPlayer.setOnBufferingUpdateListener(this);
            mAVideoPlayerListener.onAdVideoLoadSuccess();
            //根据用户设置的信息判断是否能改自动播放
            if (Utils.canAutoPlay(getContext(), AdParameters.getCurrentSetting()) &&
            Utils.getVisiblePercent(mParentContainer) > SDKConstant.VIDEO_SCREEN_PERCENT) {
                //为什么是暂停状态啊  播放前的状态是暂停的 视频资源是暂停状态
                setCurrentPlayState(STATE_PAUSING);
                resume();
            }else {
                setCurrentPlayState(STATE_PLAYING);
                pause();
            }
        }
    }

    /**
     * 只有在播放状态才会暂停
     */
    private void pause() {
        if (this.playState != STATE_PLAYING) {
            return;
        }

        setCurrentPlayState(STATE_PAUSING);
        if (isPlaying()) {
            //暂停播放
            mediaPlayer.pause();
            //如果本身就不能播放 直接回到播放起点
            if (!this.canPlay) {
                this.mediaPlayer.seekTo(0);
            }
        }
        //为啥不显示暂停
        this.showPauseView(false);
        mHandler.removeCallbacksAndMessages(null);
    }

    /**
     *  只有在暂停状态才会执行这个方法
     *  就是在播放钱的一个状态
     *  和activity的生命周期中的onresume方法执行条件基本一样 遮挡或者隐藏了之类的
     *
     */
    private void resume() {
        if (this.playState != STATE_PAUSING) {
           return;
        }

        if ( ! isPlaying()) {
            entryResumeState();
            mediaPlayer.setOnSeekCompleteListener(null);
            mediaPlayer.start();
            mHandler.sendEmptyMessage(TIME_MSG);
            showPauseView(true);
        }else {
            //如果在播放就不显示暂停按钮 这种情况就不需要显示暂停按钮 任务操作的需要显示暂停按钮
            showPauseView(false);
        }
    }

    /**
     * 加载播放界面
     *
     * 1停止加载动画
     * 2 隐藏不需要显示的按钮
     */
    private void showPlayView() {
        mLoadingView.clearAnimation();
        mLoadingView.setVisibility(GONE);
        mPlayButton.setVisibility(GONE);
        mErrorView.setVisibility(GONE);
    }

    /**
     * 缓存更新监听
     * @param mp
     * @param percent
     */
    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {

    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if (mAVideoPlayerListener != null) {
            mAVideoPlayerListener.onAdVideoLoadComplete();

        }
        playBack();
        setIsComplete(true);
        setIsRealPause(true);
    }
    /**
     * 播放完成后不销毁 而是将播放流跳转到0 暂停状态下次播放不需要耗费流量
     */
    private void playBack() {
        setCurrentPlayState(STATE_PAUSING);
        mHandler.removeCallbacksAndMessages(null);
        if (mediaPlayer != null) {
            mediaPlayer.setOnSeekCompleteListener(null);
            mediaPlayer.seekTo(0);
            mediaPlayer.pause();
        }
        this.showPauseView(false);
    }

    /**
     * 为啥返回true
     * @param mp
     * @param what
     * @param extra
     * @return
     */
    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        return true;
    }

    /**
     * 播放异常
     * @param mp
     * @param what
     * @param extra
     * @return
     */
    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        this.playState = STATE_ERROR;
        mediaPlayer = mp;
        if (mediaPlayer != null) {
            mediaPlayer.reset();
            if (mCurrentCount >= LOAD_TOTAL_COUNT) {
                //重试后还是失败就不显示暂停按钮?
                if (this.mAVideoPlayerListener != null) {
                    mAVideoPlayerListener.onAdVideoLoadComplete();
                }
            }
        }
        this.stop();
        return true;
    }

    /**
     * 停止
     */
    private void stop() {
        if (mediaPlayer != null) {
            this.mediaPlayer.reset();
            this.mediaPlayer.setOnSeekCompleteListener(null);
            this.mediaPlayer.stop();
            this.mediaPlayer.release();
            this.mediaPlayer = null;
        }

        mHandler.removeCallbacksAndMessages(null);
        setCurrentPlayState(STATE_IDLE);
        if (mCurrentCount < LOAD_TOTAL_COUNT) {
            mCurrentCount += 1;
            load();
        } else {
            showPauseView(false);
        }
    }

    /**
     * 广告定帧动画加载监听
     */
    public interface  ADFrameImageLoadListener {
        /**
         * 开始加载定帧动画
         * @param url
         * @param listener
         */
        void onStartFrameLoad(String url, ImageLoaderListener listener);
    }

    public void setFragmeImageLoadListener(ADFrameImageLoadListener adFrameImageLoadListener) {
        this.mFrameImageLoadListener = adFrameImageLoadListener;
    }
    /**
     * 图片加载状态监听
     */
    public interface ImageLoaderListener {
        /**
         * 如果图片下载不成功，传null
         *
         * @param loadedImage
         */
        void onLoadingComplete(Bitmap loadedImage);
    }


    /**
     * 供slot层来实现具体点击逻辑,具体逻辑还会变，
     * 如果对UI的点击没有具体监测的话可以不回调
     */
    public interface ADVideoPlayerListener{
        public void onBufferUpdate(int time);
        public void onAdVideoLoadSuccess();
        public void onAdVideoLoadComplete();
        public void onAdVideoLoadFailed();
        public void onClickPlay();
        public void onClickFullScreenBtn();
        public void onClickVideo();

    }

}
