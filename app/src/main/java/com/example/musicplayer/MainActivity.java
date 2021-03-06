package com.example.musicplayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.media.session.MediaButtonReceiver;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.bean.SingleSongBean;
import com.example.listener.BtnListener;
import com.example.listener.BtnTypes;
import com.example.musicplayer.Fragment.MusicLibFragment;
import com.example.musicplayer.Fragment.MyMusicFragment;
import com.example.musicplayer.notification.Notification;
import com.example.musicplayer.player.Player;
import com.example.view.CircleImageView;
import com.xuexiang.xui.widget.tabbar.EasyIndicator;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<Fragment> fragments;

    private Player player;

    private MyFragmentPageAdapter adapter;

    //底部播放器
    private Button nextIV,playIV,lastIV;

    private TextView songTV,singerTV;

    private CircleImageView circleImageView;

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Notification notification = new Notification(context);
            player = Player.getPlayer(context);
            final String NEXT = "NEXT";
            final String PREV = "PREV";
            final String PAUSE = "PAUSE";
            switch (intent.getAction()){
                case NEXT:
                    player.playNext();
                    notification.sendNotification();
                    break;
                case PREV:
                    player.playLast();
                    notification.sendNotification();
                    break;
                case PAUSE:
                    player.play();
                    notification.sendNotification();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Notification notification = new Notification(this);
        setContentView(R.layout.activity_main);

        //需要在程序中动态获取存储的读写权限，否则不部分系统中无法打开音乐文件
        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO},
                1);


        String[] stringArray = this.getResources().getStringArray(R.array.tab_segment);

        //顶部tabBar添加Fragment
        fragments = new ArrayList<>();
        MyMusicFragment myMusicFragment = new MyMusicFragment();
        MusicLibFragment musicLibFragment = new MusicLibFragment();
        fragments.add(myMusicFragment);
        fragments.add(musicLibFragment);

        FragmentManager supportFragmentManager = getSupportFragmentManager();
        adapter = new MyFragmentPageAdapter(supportFragmentManager);

        EasyIndicator easyIndicator = findViewById(R.id.easy_indicator);
        easyIndicator.setTabTitles(stringArray);

        ViewPager viewPager = findViewById(R.id.viewPager);
        easyIndicator.setViewPager(viewPager,adapter);

        //底部播放器
        nextIV = (Button) findViewById(R.id.local_music_bottom_iv_next);
        playIV = (Button) findViewById(R.id.local_music_bottom_iv_play);
        lastIV = (Button) findViewById(R.id.local_music_bottom_iv_last);

        singerTV = (TextView) findViewById(R.id.local_music_bottom_tv_singer);
        songTV = (TextView) findViewById(R.id.local_music_bottom_tv_song);

        circleImageView = (CircleImageView) findViewById(R.id.local_music_bottom_iv_icon);

        player = Player.getPlayer(this);
        player.addView(songTV,singerTV,playIV,circleImageView);

        BtnListener playBtnListener = new BtnListener(player, BtnTypes.PLAY);
        playIV.setOnClickListener(v -> {
            playBtnListener.onClick(v);
            notification.sendNotification();
        });

        BtnListener nextBtnListener = new BtnListener(player,BtnTypes.NEXT);
        nextIV.setOnClickListener(v -> {
            nextBtnListener.onClick(v);
            notification.sendNotification();
        });

        BtnListener lastBtnListener = new BtnListener(player,BtnTypes.LAST);
        lastIV.setOnClickListener(v -> {
            lastBtnListener.onClick(v);
            notification.sendNotification();
        });

        RelativeLayout bottomPlayer = findViewById(R.id.bottom_layout);
        bottomPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (player.getStatus()){
                    String songName = songTV.getText().toString();
                    startActivity(new Intent(getApplicationContext(),PlayerActivity.class)
                            .putExtra("songName",songName));
                }
            }
        });

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("PREV");
        intentFilter.addAction("PAUSE");
        intentFilter.addAction("NEXT");
        registerReceiver(broadcastReceiver,intentFilter);
        final NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        int importance = NotificationManager.IMPORTANCE_LOW;
        NotificationChannel mChannel =new NotificationChannel("channel_20","123", importance);
        mChannel.setSound(null,null);
        manager.createNotificationChannel(mChannel);

        notification.sendNotification();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        player.stop();
    }

    class MyFragmentPageAdapter extends FragmentPagerAdapter{

        public MyFragmentPageAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }

//    private void sendNotification(){
//        final String id ="channel_1";//⾃定义设置ID通道描述属性
//        final String description ="123";//通知栏管理重要提⽰消息声⾳设定
//        final NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//        int importance = NotificationManager.IMPORTANCE_HIGH;
//        NotificationChannel mChannel =new NotificationChannel(id,"123", importance);
//        manager.createNotificationChannel(mChannel);
//
//        //前一首
//        Intent prevIntent = new Intent();
//        prevIntent.setAction("PREV");
//        PendingIntent prevPendingIntent =
//                PendingIntent.getBroadcast(this, 0, prevIntent, 0);
//
//        //暂停
//        Intent pauseIntent = new Intent();
//        pauseIntent.setAction("PAUSE");
//        PendingIntent pausePendingIntent =
//                PendingIntent.getBroadcast(this, 0, pauseIntent, 0);
//
//        //下一首
//        Intent nextIntent = new Intent();
//        nextIntent.setAction("NEXT");
//        PendingIntent nextPendingIntent =
//                PendingIntent.getBroadcast(this, 0, nextIntent, 0);
//        Intent intent = new Intent(this, MainActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
//
//        Player player = Player.getPlayer(this);
//
//        singerTV = findViewById(R.id.local_music_bottom_tv_singer);
//        songTV =  findViewById(R.id.local_music_bottom_tv_song);
//
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, id)
//                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
//                .setSmallIcon(R.drawable.ic_collect)
//                //按钮
//                .addAction(R.drawable.ic_prev,"Previous",prevPendingIntent)
//                .addAction(player.isPlaying()?R.drawable.ic_pause:R.drawable.ic_play,"PAUSE",pausePendingIntent)
//                .addAction(R.drawable.ic_next,"Next",nextPendingIntent)
//                //style
//                .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
//                        .setShowActionsInCompactView(1 /* #1: pause button */))
//                //标题和文字
//                .setContentTitle(singerTV.getText().toString())
//                .setContentText(songTV.getText().toString())
//                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//                .setContentIntent(pendingIntent);
//        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
//        notificationManagerCompat.notify(1,builder.build());
//    }

}