package com.example.musicplayer;

import android.content.Intent;
import android.os.Bundle;

import com.example.bean.PathBean;
import com.example.bean.SingleSongBean;
import com.example.listener.BtnListener;
import com.example.listener.BtnTypes;
import com.example.musicplayer.Fragment.AlbumFragment;
import com.example.musicplayer.Fragment.PathFragment;
import com.example.musicplayer.Fragment.SingerFragment;
import com.example.musicplayer.Fragment.SingleSongFragment;
import com.example.musicplayer.adapter.AlbumAdapter;
import com.example.musicplayer.adapter.PathAdapter;
import com.example.musicplayer.adapter.SingleSongAdapter;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.view.FrameMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class SingleSongActivity extends AppCompatActivity {

    private RecyclerView musicRV;

    private List<Fragment> fragments;

    private Player player;

    private SingleSongAdapter adapter;

    //底部播放器
    private Button nextIV,playIV,lastIV;

    private TextView songTV,singerTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_song);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent intent = getIntent();
        String singer = intent.getStringExtra("singer");

        player = Player.getPlayer(getApplicationContext());
        //获取RecyclerView
        musicRV = (RecyclerView) findViewById(R.id.album_single_rv);
        System.out.println(musicRV);
        //设置适配器
        adapter = new SingleSongAdapter(player.getListBySinger(singer), getApplicationContext());
        //设置监听事件
        adapter.setOnItemClickListener(new SingleSongAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                int currentPosistion = position;
                SingleSongBean singleSongBean = player.getListBySinger(singer).get(currentPosistion);
                //设置底部歌曲和歌手名
                singerTV.setText(singleSongBean.getSinger());
                songTV.setText(singleSongBean.getSong());
                //停止上一首
                stopMusic();
                //播放
                playMusic(singleSongBean);
            }
        });
//        setEventList();
        musicRV.setAdapter(adapter);
        //设置布局管理器，垂直，不反转
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        musicRV.setLayoutManager(linearLayoutManager);





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
    /**
     * 播放音乐
     */
    private void playMusic(SingleSongBean bean) {
        player.start(bean);
        playIV.setBackgroundResource(R.drawable.ic_pause);//应替换为暂停图标
    }

    /**
     * 停止正在播放的音乐
     */
    private void stopMusic() {
        player.stop();
        //修改播放按钮
        playIV.setBackgroundResource(R.drawable.ic_play);//此处应使用播放图标，需要替换
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