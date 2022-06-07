package com.example.musicplayer.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bean.SingleSongBean;
import com.example.musicplayer.R;

import java.util.List;

public class SingleSongAdapter extends RecyclerView.Adapter<SingleSongAdapter.SingleSongViewHolder> {

    private List<SingleSongBean> list;

    private Context context;

    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener{
        public void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public SingleSongAdapter(List<SingleSongBean> list, Context context){
        this.list = list;
        this.context = context;
    }

    public List<SingleSongBean> getList() {
        return list;
    }

    public void setList(List<SingleSongBean> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public SingleSongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(context, com.example.musicplayer.R.layout.recyclerview_single_song_item, null);
        return new SingleSongViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SingleSongViewHolder holder, @SuppressLint("RecyclerView") int position) {
        SingleSongBean singleSongBean = list.get(position);

        holder.idTV.setText(singleSongBean.getId());
        holder.songTV.setText(singleSongBean.getSong());
        holder.singerTV.setText(singleSongBean.getSinger());
        holder.timeTV.setText(singleSongBean.getDuration());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(v,position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    class SingleSongViewHolder extends RecyclerView.ViewHolder{

        public TextView idTV,songTV,singerTV,timeTV;

        public SingleSongViewHolder(@NonNull View itemView) {
            super(itemView);
            idTV = itemView.findViewById(R.id.item_local_music_num);
            songTV = itemView.findViewById(R.id.item_local_music_song);
            singerTV = itemView.findViewById(R.id.item_local_music_singer);
            timeTV = itemView.findViewById(R.id.item_local_music_duration);
        }
    }
}
