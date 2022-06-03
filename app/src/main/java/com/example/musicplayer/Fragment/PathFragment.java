package com.example.musicplayer.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bean.PathBean;
import com.example.musicplayer.R;
import com.example.musicplayer.adapter.PathAdapter;
import com.example.musicplayer.adapter.SingerAdpter;
import com.example.musicplayer.player.Player;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PathFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PathFragment extends Fragment {

    private View root;

    private List<PathBean> list;

    private Player player;

    public PathFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PathFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PathFragment newInstance(String param1, String param2) {
        PathFragment fragment = new PathFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (root == null) {
            root = getActivity().getLayoutInflater().inflate(R.layout.fragment_path, null);
        }
        player = Player.getPlayer(getActivity());
        list = player.getPathList();

        //获取RecyclerView
        RecyclerView musicRV = (RecyclerView) root.findViewById(R.id.path_rv);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        musicRV.setLayoutManager(linearLayoutManager);
        musicRV.setAdapter(new PathAdapter(list,getActivity()));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (root == null){
            root = inflater.inflate(R.layout.fragment_path,container,false);
        }

        return root;
    }
}