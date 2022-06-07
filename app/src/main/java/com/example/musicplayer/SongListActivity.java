package com.example.musicplayer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.bean.SingleSongBean;
import com.example.db.DBHelper;
import com.example.listener.BtnListener;
import com.example.listener.BtnTypes;
import com.example.musicplayer.adapter.ListSingleSongAdapter;
import com.example.musicplayer.adapter.SingleSongAdapter;
import com.example.musicplayer.player.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class SongListActivity extends AppCompatActivity {

    private TextView songListNameTV,songListCountTV,songListCreateTimeTV;

    private RecyclerView songListRV;

    private Button addSongBtn;

    //底部播放器
    private Button nextIV,playIV,lastIV;

    private TextView songTV,singerTV;

    private Player player;

    private ListSingleSongAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_list);

        player = Player.getPlayer(getApplicationContext());

        songListNameTV = findViewById(R.id.song_list_name);
        songListCountTV = findViewById(R.id.song_list_count);
        songListCreateTimeTV = findViewById(R.id.song_list_create_time);

        Intent intent = getIntent();
        String listId = intent.getStringExtra("listId");
        String listName = intent.getStringExtra("listName");
        String listCount = intent.getStringExtra("listCount");
        String listCreateTime = intent.getStringExtra("listCreateTime");

        songListNameTV.setText(listName);
        songListCountTV.setText(listCount + "首");
        songListCreateTimeTV.setText(listCreateTime);

        songListRV = (RecyclerView) findViewById(R.id.song_list_item);

        DBHelper dbHelper = new DBHelper(getApplicationContext());
        List<SingleSongBean> songListByListId = dbHelper.getSongListByListId(Integer.parseInt(listId));

        adapter = new ListSingleSongAdapter(songListByListId,this,Integer.parseInt(listId),songListCountTV);

        adapter.setOnItemClickListener(new ListSingleSongAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                int currentPosistion = position;
                SingleSongBean singleSongBean = songListByListId.get(currentPosistion);
                //设置底部歌曲和歌手名
                singerTV.setText(singleSongBean.getSinger());
                songTV.setText(singleSongBean.getSong());
                //停止上一首
                stopMusic();
                //播放
                playMusic(singleSongBean);
            }
        });

        songListRV.setAdapter(adapter);

        //设置布局管理器，垂直，不反转
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        songListRV.setLayoutManager(linearLayoutManager);

        //底部播放器
        nextIV = (Button) findViewById(R.id.local_music_bottom_iv_next);
        playIV = (Button) findViewById(R.id.local_music_bottom_iv_play);
        lastIV = (Button) findViewById(R.id.local_music_bottom_iv_last);

        singerTV = (TextView) findViewById(R.id.local_music_bottom_tv_singer);
        songTV = (TextView) findViewById(R.id.local_music_bottom_tv_song);

        BtnListener playBtnListener = new BtnListener(player, BtnTypes.PLAY);
        playIV.setOnClickListener(playBtnListener);

        BtnListener nextBtnListener = new BtnListener(player,BtnTypes.NEXT);
        nextIV.setOnClickListener(nextBtnListener);

        BtnListener lastBtnListener = new BtnListener(player,BtnTypes.LAST);
        lastIV.setOnClickListener(lastBtnListener);

        player.addView(songTV,singerTV,playIV);

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

        addSongBtn = findViewById(R.id.add_song_btn);
        addSongBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddSongDialog(listId);
            }
        });

    }

    private void showAddSongDialog(String songListId) {

        //当前歌单已经包含的单曲，在展示添加列表时应该过滤这些已经包含的
        List<SingleSongBean> contains = adapter.getList();
        List<SingleSongBean> singleSongList = player.getSingleSongList();
        List<String> collect = singleSongList
                .stream()
                .filter(bean -> {
                    return !contains.contains(bean);
                })
                .map(SingleSongBean::getSong)
                .collect(Collectors.toList());
        final String[] items = collect.toArray(new String[collect.size()]);
        //选中的单曲的ID
        ArrayList<String> yourChoices = new ArrayList<>();
        // 设置默认选中的选项，全为false默认均未选中
        final boolean initChoiceSets[] = new boolean[collect.size()];
        yourChoices.clear();
        AlertDialog.Builder multiChoiceDialog = new AlertDialog.Builder(this);
        multiChoiceDialog.setTitle("添加单曲");
        multiChoiceDialog.setMultiChoiceItems(items, initChoiceSets,
                new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which,
                                        boolean isChecked) {
                        SingleSongBean singleSongBean = singleSongList.get(which);
                        if (isChecked) {
                            yourChoices.add(singleSongBean.getID());
                        } else {
                            yourChoices.remove(singleSongBean.getID());
                        }
                    }
                });
        multiChoiceDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DBHelper dbHelper = new DBHelper(getApplicationContext());
                        List<SingleSongBean> singleSongBeans = dbHelper.insertSong(
                                yourChoices,
                                Integer.parseInt(songListId)
                        );
                        songListCountTV.setText(singleSongBeans.size() + "首");
                        adapter.setList(singleSongBeans);
                        adapter.notifyDataSetChanged();
                    }
                });
        multiChoiceDialog.show();
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
}