package com.example.musicplayer.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bean.SingerBean;
import com.example.musicplayer.AlbumSingleActivity;
import com.example.musicplayer.R;
import com.example.musicplayer.SingleSongActivity;
import com.example.musicplayer.adapter.AlbumAdapter;
import com.example.musicplayer.adapter.SingerAdpter;
import com.example.musicplayer.adapter.SingleSongAdapter;
import com.example.musicplayer.player.Player;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SingerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SingerFragment extends Fragment {

    private View root;

    private List<SingerBean> list;

    private Player player;

    public SingerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SingerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SingerFragment newInstance(String param1, String param2) {
        SingerFragment fragment = new SingerFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (root == null) {
            root = getActivity().getLayoutInflater().inflate(R.layout.fragment_singer, null);
        }
        player = Player.getPlayer(getActivity());
        list = player.getSingerList();

        //获取RecyclerView
        RecyclerView musicRV = (RecyclerView) root.findViewById(R.id.singer_rv);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        musicRV.setLayoutManager(linearLayoutManager);

        SingerAdpter singerAdpter = new SingerAdpter(list,getActivity());
        musicRV.setAdapter(singerAdpter);
        singerAdpter.setOnItemClickListener(new SingerAdpter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                TextView singerTV = view.findViewById(R.id.singer_name);

                Intent intent = new Intent();
                intent.setClass(getActivity().getApplicationContext(), SingleSongActivity.class);
                intent.putExtra("singer",singerTV.getText().toString());
                startActivity(intent);
            }
        });


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (root == null){
            root = inflater.inflate(R.layout.fragment_singer,container,false);
        }

        return root;
    }
}