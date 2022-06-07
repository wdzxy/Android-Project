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

import com.example.bean.AlbumBean;
import com.example.bean.SingleSongBean;
import com.example.musicplayer.AlbumSingleActivity;
import com.example.musicplayer.R;
import com.example.musicplayer.adapter.AlbumAdapter;
import com.example.musicplayer.adapter.SingleSongAdapter;
import com.example.musicplayer.player.Player;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AlbumFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AlbumFragment extends Fragment {

    private List<AlbumBean> list;

    private View root;

    private Player player;

    public AlbumFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Album.
     */
    // TODO: Rename and change types and number of parameters
    public static AlbumFragment newInstance(String param1, String param2) {
        AlbumFragment fragment = new AlbumFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (root == null) {
            root = getActivity().getLayoutInflater().inflate(R.layout.fragment_album, null);
        }
        player = Player.getPlayer(getActivity());
        list = player.getAlbumList();

        //获取RecyclerView
        RecyclerView musicRV = (RecyclerView) root.findViewById(R.id.album_rv);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        musicRV.setLayoutManager(linearLayoutManager);
        AlbumAdapter albumAdapter = new AlbumAdapter(list,getActivity());

        albumAdapter.setOnItemClickListener(new AlbumAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                TextView albumTV = view.findViewById(R.id.song_sheet_item_name);

                Intent intent = new Intent();
                intent.setClass(getActivity().getApplicationContext(), AlbumSingleActivity.class);
                intent.putExtra("listType","album");
                intent.putExtra("arg",albumTV.getText().toString());
                startActivity(intent);
            }
        });

        musicRV.setAdapter(albumAdapter);



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (root == null){
            root = inflater.inflate(R.layout.fragment_album,container,false);
        }

        return root;
    }
}