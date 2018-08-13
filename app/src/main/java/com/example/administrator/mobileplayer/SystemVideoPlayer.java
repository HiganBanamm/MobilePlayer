package com.example.administrator.mobileplayer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.administrator.mobileplayer.Utils.Utils;
import com.example.administrator.mobileplayer.domain.MediaItem;

import java.util.ArrayList;

import static java.util.jar.Pack200.Unpacker.PROGRESS;

//自定义播放器
public class SystemVideoPlayer extends Activity implements View.OnClickListener {

    //视频进度的更新
    private static final int PROGRESS = 1;
    private VideoView videoView;
    //视频地址
    private Uri uri;
    private LinearLayout llTop;
    private TextView tvName;
    private ImageView ivBattery;
    private TextView tvSystemTime;
    private Button btnVoice;
    private SeekBar seekbarVioce;
    private Button btnSwichPlay;
    private LinearLayout llBottom;
    private TextView tvCurrentTime;
    private SeekBar seekbarVideo;
    private TextView tvDuration;
    private Button btnExit;
    private Button btnVideoPre;
    private Button btnVideoStartPause;
    private Button btnVideoNext;
    private Button btnVideoSwichScreen;
    private Utils utils;

    private int position;

    /**
     * Find the Views in the layout<br />
     * <br />
     * Auto-created on 2018-08-09 19:34:46 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    private void findViews() {
        setContentView(R.layout.activity_system_video_player);
        llTop = (LinearLayout) findViewById(R.id.ll_top);
        tvName = (TextView) findViewById(R.id.tv_name);
        ivBattery = (ImageView) findViewById(R.id.iv_battery);
        tvSystemTime = (TextView) findViewById(R.id.tv_system_time);
        btnVoice = (Button) findViewById(R.id.btn_voice);
        seekbarVioce = (SeekBar) findViewById(R.id.seekbar_vioce);
        btnSwichPlay = (Button) findViewById(R.id.btn_swich_play);
        llBottom = (LinearLayout) findViewById(R.id.ll_bottom);
        tvCurrentTime = (TextView) findViewById(R.id.tv_current_time);
        seekbarVideo = (SeekBar) findViewById(R.id.seekbar_video);
        tvDuration = (TextView) findViewById(R.id.tv_duration);
        btnExit = (Button) findViewById(R.id.btn_exit);
        btnVideoPre = (Button) findViewById(R.id.btn_video_pre);
        btnVideoStartPause = (Button) findViewById(R.id.btn_video_start_pause);
        btnVideoNext = (Button) findViewById(R.id.btn_video_next);
        btnVideoSwichScreen = (Button) findViewById(R.id.btn_video_swich_screen);

        btnVoice.setOnClickListener(this);
        btnSwichPlay.setOnClickListener(this);
        btnExit.setOnClickListener(this);
        btnVideoPre.setOnClickListener(this);
        btnVideoStartPause.setOnClickListener(this);
        btnVideoNext.setOnClickListener(this);
        btnVideoSwichScreen.setOnClickListener(this);
    }

    /**
     * Handle button click events<br />
     * <br />
     * Auto-created on 2018-08-09 19:34:46 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    @Override
    public void onClick(View v) {
        if (v == btnVoice) {
            // Handle clicks for btnVoice
        } else if (v == btnSwichPlay) {
            // Handle clicks for btnSwichPlay
        } else if (v == btnExit) {
            // Handle clicks for btnExit
        } else if (v == btnVideoPre) {
            // Handle clicks for btnVideoPre
            setPlayPre();
        } else if (v == btnVideoStartPause) {
            // Handle clicks for btnVideoStartPause
            if (videoView.isPlaying()) {
                //如果视频在播放，设置为暂停
                videoView.pause();
                //同时按钮状态设置为播放状态
                btnVideoStartPause.setBackgroundResource(R.drawable.btn_video_start_selector);

            } else {
                //视频播放
                videoView.start();
                //按钮状态设置为暂停
                btnVideoStartPause.setBackgroundResource(R.drawable.btn_video_pause_selector);
            }
        } else if (v == btnVideoNext) {
            // Handle clicks for btnVideoNext
            setPlayNext();
        } else if (v == btnVideoSwichScreen) {
            // Handle clicks for btnVideoSwichScreen
        }
    }

    //点击上一个的时候
    private void setPlayPre() {

        if (mediaItems != null && mediaItems.size() > 0) {
            //播放上一个
            position--;
            if (position >= 0) {
                MediaItem mediaItem = mediaItems.get(position);
                videoView.setVideoPath(mediaItem.getData());//设置播放地址，开始播放
                tvName.setText(mediaItem.getName());//设置标题

                //设置按钮的状态
                setButtonState();
            }

        }

        /*如果是第0个不需要退出
        else if (uri != null) {
            //退出播放器
            finish();
        }*/
    }

    //点击下一个的时候，调用该方法
    private void setPlayNext() {
        if (mediaItems != null && mediaItems.size() > 0) {
            //播放下一个
            position++;
            if (position < mediaItems.size()) {
                MediaItem mediaItem = mediaItems.get(position);
                videoView.setVideoPath(mediaItem.getData());//设置播放地址，开始播放
                tvName.setText(mediaItem.getName());//设置标题


                //当播放到最后一个视频以后，设置提示并且按钮变灰
                if (position == mediaItems.size() - 1) {
                    Toast.makeText(SystemVideoPlayer.this, "已经播放到最后一个视频了", Toast.LENGTH_SHORT).show();
                }

                //设置按钮的状态
                setButtonState();


            }else {
                finish();//当播放到最后一个时，退出播放器
            }

        } else if (uri != null) {
            //退出播放器
            finish();
        }
    }

    //设置上一个和下一个按钮的状态
    private void setButtonState() {
        //有播放列表的情况
        if (mediaItems != null && mediaItems.size() < 0) {
            if (position == 0) {//如果是第一个视频
                btnVideoPre.setEnabled(false);
                btnVideoPre.setBackgroundResource(R.drawable.video_next_gray);

            } else if (position == mediaItems.size() - 1) { //如果是最后一个视频
                btnVideoNext.setEnabled(false); //首先不可点击
                btnVideoNext.setBackgroundResource(R.drawable.btn_next_gray);
            } else {//如果视频前后还有视频
                btnVideoNext.setEnabled(true);
                btnVideoNext.setBackgroundResource(R.drawable.btn_video_next_select);
                btnVideoPre.setEnabled(true);
                btnVideoPre.setBackgroundResource(R.drawable.btn_video_pre_select);
            }

        } else if (uri != null) {

            btnVideoNext.setEnabled(false); //设置不可点击
            btnVideoNext.setBackgroundResource(R.drawable.btn_next_gray);
            btnVideoPre.setEnabled(false);
            btnVideoPre.setBackgroundResource(R.drawable.video_next_gray);


        } else {
            Toast.makeText(this, "没有播放地址", Toast.LENGTH_SHORT).show();
        }
    }

    /*视频的SeekBar更新
    1.视频的总时长和SeekBar的setMax(总时长)
        注意：准备好了的回调后
    2.实例化Handler，每秒得到当前视频的播放进度，SeekBar.setProgress(当前进度)
        */

   /* SeekBar的拖拽
     1.视频的总时长和SeekBar的setMax(总时长)
    注意：准备好了的回调后
     2.设置SeekBar状态变化的监听*/


    //实例化Handler，每秒得到当前播放进度，SeekBar.setProgress
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case PROGRESS:
                    //1.得到当前的播放视频的进度
                    int currentPosition = videoView.getCurrentPosition();
                    //2.SeeKBar.setProgress(当前进度)
                    seekbarVideo.setProgress(currentPosition);

                    //更新文本的播放进度
                    tvCurrentTime.setText(utils.stringForTime(currentPosition));
                    //3.每秒更新一次
                    handler.removeMessages(PROGRESS);
                    handler.sendEmptyMessageDelayed(PROGRESS, 1000);
                    break;
            }
        }
    };
    private ArrayList<MediaItem> mediaItems;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        utils = new Utils();

        findViews();
        //实例化videoView
        videoView = (VideoView) findViewById(R.id.video_View);
        //获取uri
        getData();
        //设置播放
        setData();
        setListener();


        //设置控制面板
        //videoView.setMediaController(new MediaController(this));
    }

    private void setData() {
        //当列表不等于空时
        if (mediaItems != null && mediaItems.size() > 0) {
            //得到数据
            MediaItem mediaItem = mediaItems.get(position);
            videoView.setVideoPath(mediaItem.getData());
            //设置视频的标题
            tvName.setText(mediaItem.getName());
        } else if (uri != null) {
            videoView.setVideoURI(uri);
            tvName.setText(uri.toString());
        }
        //videoView.setVideoURI(uri);
        //当一进来的时候，也要设置一个前后按钮的状态
        setButtonState();

    }

    private void getData() {
        uri = getIntent().getData();//得到一个地址的情况，一般来自文件夹浏览器，相册
        mediaItems = (ArrayList<MediaItem>) getIntent().getSerializableExtra("videolist");
        position = getIntent().getIntExtra("position", 0);//列表中的位置
        getIntent().getSerializableExtra("vediolist");
    }

    //把监听的都放在一个方法里，Ctrl+Alt+M设置
    private void setListener() {
        //设置监听
        videoView.setOnPreparedListener(new MyonPreparedListener());
        //播放出错监听，播放出错时监听这个方法
        videoView.setOnErrorListener(new MyOnErrorListener());

        //播放完成监听
        videoView.setOnCompletionListener(new MyOnCompletionListener());

        //设置SeekBar状态变化的监听
        seekbarVideo.setOnSeekBarChangeListener(new VideoOnSeekBarChangeListener());
    }

    class MyonPreparedListener implements MediaPlayer.OnPreparedListener {

        @Override
        public void onPrepared(MediaPlayer mp) {
            //1.1首先得到总时长  mp.getDuration(); 或  videoView.getDuration();
            int duration = videoView.getDuration();
            //1.2和seekBar关联总长度
            seekbarVideo.setMax(duration);
            tvDuration.setText(utils.stringForTime(duration));
            //2发消息
            handler.sendEmptyMessage(PROGRESS);

            videoView.start();//开始播放
        }
    }

    class MyOnErrorListener implements MediaPlayer.OnErrorListener {
        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
            Toast.makeText(SystemVideoPlayer.this, "播放出错了", Toast.LENGTH_SHORT).show();
            return true;
        }
    }

    class MyOnCompletionListener implements MediaPlayer.OnCompletionListener {
        @Override
        public void onCompletion(MediaPlayer mp) {
            /*Toast.makeText(SystemVideoPlayer.this, "播放完成", Toast.LENGTH_SHORT).show();
            finish();*/
            setPlayNext();
        }
    }

    class VideoOnSeekBarChangeListener implements SeekBar.OnSeekBarChangeListener {
        //当我们的手指滑动时，会引起SeekBar进度变化，会回调这个变化
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            //如果是用户引起的，fromUser就为true，如果不是用户引起的，就为false
            if (fromUser) {
                videoView.seekTo(progress);
            }
        }

        //当我们的手指触碰时，回调该方法
        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        //当我们的手指离开时，回调该方法
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    }
}


