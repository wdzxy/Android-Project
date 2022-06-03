package com.example.musicplayer.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.bean.AlbumBean;
import com.example.bean.MainSongSheetBean;
import com.example.musicplayer.R;
import com.example.musicplayer.adapter.MainSongSheetAdapter;
import com.example.musicplayer.player.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyMusicFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyMusicFragment extends Fragment {

    private View root;

    private Player player;

    private Button localMusic;

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate the layout for this fragment
        if (root == null) {
            root = getActivity().getLayoutInflater().inflate(R.layout.fragment_my_music, null);
        }

        //主页专辑
        player = Player.getPlayer(getContext());
        List<AlbumBean> albumList = player.getAlbumList();

        RecyclerView mainSongSheet = (RecyclerView)root.findViewById(R.id.main_song_sheet);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mainSongSheet.setLayoutManager(linearLayoutManager);
        mainSongSheet.setAdapter(new MainSongSheetAdapter(albumList,getActivity()));

        localMusic = (Button) root.findViewById(R.id.local_music);
        localMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClassName("com.example.musicplayer","com.example.musicplayer.LocalMusicActivity" );
                startActivity(intent);
            }
        });
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