package com.example.musicplayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.bean.SingleSongBean;
import com.example.db.DBHelper;
import com.example.listener.BtnListener;
import com.example.listener.BtnTypes;
import com.example.musicplayer.notification.Notification;
import com.example.musicplayer.player.Player;
import com.gauravk.audiovisualizer.visualizer.BarVisualizer;

import java.util.ArrayList;
import java.util.List;

public class PlayerActivity extends AppCompatActivity {

    private Button playBtn,nextBtn,prevBtn,ffBtn,frBtn,typeBtn,collectBtn,listBtn;

    private TextView songNameTV,startTV,stopTV;

    private ImageView songImg;

    private SeekBar seekMusic;

    private BarVisualizer visualizer;

    private static final String EXTRA_NAME = "song_name";

    private Player player;

    private DBHelper dbHelper;

    private List<SingleSongBean> list;

    private Thread seekBarUpdate;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        if (visualizer != null){
            visualizer.release();
        }
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("正在播放");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        prevBtn = findViewById(R.id.prev_btn);
        nextBtn = findViewById(R.id.next_btn);
        playBtn = findViewById(R.id.play_btn);
        ffBtn = findViewById(R.id.ff_btn);
        frBtn = findViewById(R.id.fr_btn);
        typeBtn = findViewById(R.id.player_status);
        collectBtn = findViewById(R.id.collect_song);
        listBtn = findViewById(R.id.song_list);
        songNameTV = findViewById(R.id.song_name);
        startTV = findViewById(R.id.start_txt);
        stopTV = findViewById(R.id.stop_txt);
        songImg = findViewById(R.id.song_img);
        seekMusic = findViewById(R.id.seek_bar);
        visualizer = findViewById(R.id.blast);

        player = Player.getPlayer(this);
        dbHelper = new DBHelper(getApplicationContext());

        SingleSongBean currentSong = player.getCurrentSong();
        boolean collected = dbHelper.isCollected(currentSong.getID());

        if (collected){
            collectBtn.setBackgroundResource(R.drawable.ic_collect);
        }else {
            collectBtn.setBackgroundResource(R.drawable.ic_un_clollected);
        }

        int audioSessionId = player.getAudioSessionId();
        if (audioSessionId != -1){
            visualizer.setAudioSessionId(audioSessionId);
        }

        Intent intent = getIntent();

        if (player.isPlaying()){
            playBtn.setBackgroundResource(R.drawable.ic_pause);
            startAnimation();
        }

        list = player.getSingleSongList();
        String songName = intent.getStringExtra("songName");
        songNameTV.setSelected(true);
        songNameTV.setText(songName);
        player.addSongTV(songNameTV);

        Notification notification =  new Notification(this);

        BtnListener playBtnListener = new BtnListener(player, BtnTypes.PLAY);
        playBtn.setOnClickListener(v -> {
            playBtnListener.onClick(v);
            notification.sendNotification();
        });
        player.addPlayBtn(playBtn);

        BtnListener nextBtnListener = new BtnListener(player, BtnTypes.NEXT);
        nextBtn.setOnClickListener(v -> {
            nextBtnListener.onClick(v);
            notification.sendNotification();
        });

        BtnListener prevBtnListener = new BtnListener(player, BtnTypes.LAST);
        prevBtn.setOnClickListener(v -> {
            prevBtnListener.onClick(v);
            notification.sendNotification();
        });

        BtnListener forwardBtnListener = new BtnListener(player, BtnTypes.FORWARD);
        ffBtn.setOnClickListener(forwardBtnListener);

        BtnListener rewardBtnListener = new BtnListener(player, BtnTypes.REWARD);
        frBtn.setOnClickListener(rewardBtnListener);

        BtnListener changeTypeBtnListener = new BtnListener(player, BtnTypes.PLAY_STATUS_CHANGE);
        typeBtn.setOnClickListener(changeTypeBtnListener);

        BtnListener collectBtnListener = new BtnListener(player, BtnTypes.COLLECT);
        collectBtnListener.setContext(getApplicationContext());
        collectBtn.setOnClickListener(collectBtnListener);

        //

        seekBarUpdate = new Thread(){
            @Override
            public void run() {
                int duration = player.getDuration();
                int currentPosition = 0;
                while (currentPosition < duration){
                    try {
                        sleep(500);
                        currentPosition = player.getCurrent();
                        seekMusic.setProgress(currentPosition);
                        seekMusic.setMax(player.getDuration());
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
            }
        };

        seekMusic.setMax(player.getDuration());
        seekBarUpdate.start();
        seekMusic.getProgressDrawable().setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.MULTIPLY);
        seekMusic.getThumb().setColorFilter(getResources().getColor(R.color.blue_light),PorterDuff.Mode.SRC_IN);
        seekMusic.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                player.seekTo(seekBar.getProgress());
            }
        });

        String endTime = createTime(player.getDuration());
        stopTV.setText(endTime);

        final Handler handler = new Handler();
        final int delay = 1000;

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                String currentTime = createTime(player.getCurrent());
                startTV.setText(currentTime);

                String duration = createTime(player.getDuration());
                stopTV.setText(duration);

                handler.postDelayed(this,delay);
            }
        },delay);

    }

    public void startAnimation(){
//        ObjectAnimator animator = ObjectAnimator.ofFloat(songImg,"rotation",0f,360f);
//        animator.setDuration(1000);
//        AnimatorSet animatorSet = new AnimatorSet();
//        animatorSet.play(animator);
//        animatorSet.start();
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.img_animation);
        LinearInterpolator lin = new LinearInterpolator();//设置动画匀速运动
        animation.setInterpolator(lin);
        songImg.startAnimation(animation);
    }

    public String createTime(int duration){
        String time = "";
        int min =duration/1000/60;
        int sec = duration/1000%60;

        time += min + ":";
        if (sec < 10){
            time += "0";
        }
        time += sec;

        return time;
    }
}