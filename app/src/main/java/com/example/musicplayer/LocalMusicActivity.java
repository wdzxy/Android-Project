package com.example.musicplayer;

import android.os.Bundle;

import com.example.bean.PathBean;
import com.example.musicplayer.Fragment.AlbumFragment;
import com.example.musicplayer.Fragment.PathFragment;
import com.example.musicplayer.Fragment.SingerFragment;
import com.example.musicplayer.Fragment.SingleSongFragment;
import com.example.musicplayer.adapter.PathAdapter;
import com.example.musicplayer.player.Player;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.xuexiang.xui.widget.tabbar.EasyIndicator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.FrameMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class LocalMusicActivity extends AppCompatActivity {

    private List<Fragment> fragments;

    private Player player;

    //底部播放器
    private ImageView nextIV,playIV,lastIV;

    private TextView songTV,singerTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_music);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        //设置tab
        String[] tabs = this.getResources().getStringArray(R.array.tab_local_music);
        fragments = new ArrayList<>(tabs.length);

        SingleSongFragment singleSongFragment = new SingleSongFragment();
        fragments.add(singleSongFragment);

        AlbumFragment albumFragment = new AlbumFragment();
        fragments.add(albumFragment);

        SingerFragment singerFragment = new SingerFragment();
        fragments.add(singerFragment);

        PathFragment pathFragment = new PathFragment();
        fragments.add(pathFragment);

        EasyIndicator localMusicEasyIndicator = (EasyIndicator) findViewById(R.id.local_music_easyIndicator);
        localMusicEasyIndicator.setTabTitles(tabs);

        FragmentManager supportFragmentManager = getSupportFragmentManager();
        LocalMusciFragmentAdapter localMusciFragmentAdapter = new LocalMusciFragmentAdapter(supportFragmentManager);
        ViewPager viewPager = (ViewPager) findViewById(R.id.local_music_viewpager);

        localMusicEasyIndicator.setViewPager(viewPager,localMusciFragmentAdapter);

        //底部播放器
        nextIV = (ImageView) findViewById(R.id.local_music_bottom_iv_next);
        playIV = (ImageView) findViewById(R.id.local_music_bottom_iv_play);
        lastIV = (ImageView) findViewById(R.id.local_music_bottom_iv_last);

        singerTV = (TextView) findViewById(R.id.local_music_bottom_tv_singer);
        songTV = (TextView) findViewById(R.id.local_music_bottom_tv_song);

        player = Player.getPlayer(this);
        player.addView(songTV,singerTV,playIV);
    }

    class LocalMusciFragmentAdapter extends FragmentPagerAdapter{

        public LocalMusciFragmentAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments == null ? null : fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments == null ? 0 : fragments.size();
        }
    }
}