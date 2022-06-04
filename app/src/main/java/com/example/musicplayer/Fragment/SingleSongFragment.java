package com.example.musicplayer.Fragment;

import android.content.ContentResolver;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bean.SingleSongBean;
import com.example.listener.BtnListener;
import com.example.listener.BtnTypes;
import com.example.musicplayer.R;
import com.example.musicplayer.adapter.SingleSongAdapter;
import com.example.musicplayer.player.Player;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SingleSongFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SingleSongFragment extends Fragment {

    private Player player;

    private View root;

    private RecyclerView musicRV;

    private SingleSongAdapter adapter;

    //数据源
    private List<SingleSongBean> list;

    //底部播放器
    private Button nextIV,playIV,lastIV;

    private TextView songTV,singerTV;

    //记录当前正在播放的音乐的位置
    int currentPosistion = -1;

    public SingleSongFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SingleSongFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SingleSongFragment newInstance(String param1, String param2) {
        SingleSongFragment fragment = new SingleSongFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (root == null) {
            root = getActivity().getLayoutInflater().inflate(R.layout.fragment_single_song, null);
        }
        player = Player.getPlayer(getActivity());
        list = player.getSingleSongList();

        //获取RecyclerView
        musicRV = (RecyclerView) root.findViewById(R.id.single_song);
        //设置适配器
        adapter = new SingleSongAdapter(list, getActivity());
        //设置监听事件
        setEventList();
        musicRV.setAdapter(adapter);
        //设置布局管理器，垂直，不反转
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        musicRV.setLayoutManager(linearLayoutManager);

        //底部播放器
        nextIV = (Button) getActivity().findViewById(R.id.local_music_bottom_iv_next);
        playIV = (Button) getActivity().findViewById(R.id.local_music_bottom_iv_play);
        lastIV = (Button) getActivity().findViewById(R.id.local_music_bottom_iv_last);

        singerTV = (TextView) getActivity().findViewById(R.id.local_music_bottom_tv_singer);
        songTV = (TextView) getActivity().findViewById(R.id.local_music_bottom_tv_song);

        BtnListener playBtnListener = new BtnListener(player, BtnTypes.PLAY);
        playIV.setOnClickListener(playBtnListener);

        BtnListener nextBtnListener = new BtnListener(player,BtnTypes.NEXT);
        nextIV.setOnClickListener(nextBtnListener);

        BtnListener lastBtnListener = new BtnListener(player,BtnTypes.LAST);
        lastIV.setOnClickListener(lastBtnListener);

    }

    /**
     * 为每一个单曲设置监听
     */
    private void setEventList() {
        adapter.setOnItemClickListener(new SingleSongAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                currentPosistion = position;
                SingleSongBean singleSongBean = list.get(currentPosistion);
                //设置底部歌曲和歌手名
                singerTV.setText(singleSongBean.getSinger());
                songTV.setText(singleSongBean.getSong());
                //停止上一首
                stopMusic();
                //播放
                playMusic(singleSongBean);
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (root == null){
            root = inflater.inflate(R.layout.fragment_single_song,container,false);
        }

        return root;
    }
}