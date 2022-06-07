package com.example.musicplayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.bean.SingleSongBean;
import com.example.musicplayer.broadcast.MyBroadcastReceiver;
import com.example.listener.BtnListener;
import com.example.listener.BtnTypes;
import com.example.musicplayer.Fragment.MusicLibFragment;
import com.example.musicplayer.Fragment.MyMusicFragment;
import com.example.musicplayer.notification.Notification;
import com.example.musicplayer.player.Player;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        player = Player.getPlayer(this);
        player.addView(songTV,singerTV,playIV);

        BtnListener playBtnListener = new BtnListener(player, BtnTypes.PLAY);
        playIV.setOnClickListener(playBtnListener);

        BtnListener nextBtnListener = new BtnListener(player,BtnTypes.NEXT);
        nextIV.setOnClickListener(nextBtnListener);

        BtnListener lastBtnListener = new BtnListener(player,BtnTypes.LAST);
        lastIV.setOnClickListener(lastBtnListener);

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
        Notification notification = new Notification(this);
        notification.sendnotification();
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
}