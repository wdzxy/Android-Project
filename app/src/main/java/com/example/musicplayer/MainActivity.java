package com.example.musicplayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.musicplayer.Fragment.MusicLibFragment;
import com.example.musicplayer.Fragment.MyMusicFragment;
import com.example.musicplayer.player.Player;
import com.xuexiang.xui.widget.tabbar.EasyIndicator;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<Fragment> fragments;

    private MyFragmentPageAdapter adapter;

    //底部播放器
    private ImageView nextIV,playIV,lastIV;

    private TextView songTV,singerTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //需要在程序中动态获取存储的读写权限，否则不部分系统中无法打开音乐文件
        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
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
        nextIV = (ImageView) findViewById(R.id.local_music_bottom_iv_next);
        playIV = (ImageView) findViewById(R.id.local_music_bottom_iv_play);
        lastIV = (ImageView) findViewById(R.id.local_music_bottom_iv_last);

        singerTV = (TextView) findViewById(R.id.local_music_bottom_tv_singer);
        songTV = (TextView) findViewById(R.id.local_music_bottom_tv_song);

        Player.setContext(this);
        Player.addView(songTV,singerTV,playIV);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Player.stop();
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