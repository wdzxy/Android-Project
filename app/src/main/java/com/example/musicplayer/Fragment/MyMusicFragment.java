package com.example.musicplayer.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bean.AlbumBean;
import com.example.bean.SongListBean;
import com.example.db.DBHelper;
import com.example.musicplayer.AlbumSingleActivity;
import com.example.musicplayer.PlayerActivity;
import com.example.musicplayer.R;
import com.example.musicplayer.SongListActivity;
import com.example.musicplayer.adapter.AlbumAdapter;
import com.example.musicplayer.adapter.SongListAdapter;
import com.example.musicplayer.player.Player;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyMusicFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyMusicFragment extends Fragment {

    private View root;

    private Player player;

    private Button localMusic,myCollection,recentList;

    private View createListV;

    public MyMusicFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BlankFragment1.
     */
    // TODO: Rename and change types and number of parameters
    public static MyMusicFragment newInstance(String param1, String param2) {
        MyMusicFragment fragment = new MyMusicFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate the layout for this fragment
        if (root == null) {
            root = getActivity().getLayoutInflater().inflate(R.layout.fragment_my_music, null);
        }

        //主页歌单
        player = Player.getPlayer(getContext());
//        List<AlbumBean> albumList = player.getAlbumList();
        DBHelper dbHelper = new DBHelper(getContext());
        List<SongListBean> songList = dbHelper.getSongList();

        RecyclerView songListRV = (RecyclerView)root.findViewById(R.id.main_song_sheet);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        songListRV.setLayoutManager(linearLayoutManager);
        SongListAdapter songListAdapter = new SongListAdapter(songList, getActivity());
        songListRV.setAdapter(songListAdapter);
        songListAdapter.setOnItemClickListener(new SongListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent();
                intent.setClass(getContext(), SongListActivity.class);
                SongListBean songListBean = songList.get(position);
                intent.putExtra("listId",String.valueOf(songListBean.getId()));
                intent.putExtra("listName",songListBean.getName());
                intent.putExtra("listCreateTime",songListBean.getCreateTime());
                intent.putExtra("listCount",String.valueOf(songListBean.getCount()));
                startActivity(intent);
            }
        });

        localMusic = (Button) root.findViewById(R.id.local_music);
        localMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClassName("com.example.musicplayer","com.example.musicplayer.LocalMusicActivity" );
                startActivity(intent);
            }
        });

        myCollection = root.findViewById(R.id.my_collection);
        myCollection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getActivity().getApplicationContext(), AlbumSingleActivity.class);
                intent.putExtra("listType","collection");
                intent.putExtra("arg","");
                startActivity(intent);
            }
        });

        recentList = root.findViewById(R.id.resent_music);
        recentList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getContext(),AlbumSingleActivity.class);
                intent.putExtra("listType","recentList");
                intent.putExtra("arg","");
                startActivity(intent);
            }
        });

        createListV = root.findViewById(R.id.create_list);
        createListV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNewSongListDialog();
            }
        });
    }

    private void showNewSongListDialog() {
        final EditText editText = new EditText(getContext());
        AlertDialog.Builder inputDialog =
                new AlertDialog.Builder(getContext());
        inputDialog.setTitle("输入新建歌单标题").setView(editText);
        inputDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String listName = editText.getText().toString();
                        DBHelper dbHelper = new DBHelper(getContext());
                        dbHelper.insertList(listName);
                    }
                }).show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (root == null) {
            root = inflater.inflate(R.layout.fragment_my_music, container, false);
        }

        return root;
    }
}