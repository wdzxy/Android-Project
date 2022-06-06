package com.example.musicplayer;

import android.content.Intent;
import android.os.Bundle;

import com.example.bean.PathBean;
import com.example.listener.BtnListener;
import com.example.listener.BtnTypes;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class LocalMusicActivity extends AppCompatActivity {

    private List<Fragment> fragments;

    private Player player;

    //底部播放器
    private Button nextIV,playIV,lastIV;

    private TextView songTV,singerTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_music);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

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