package com.example.musicplayer.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bean.SongListBean;
import com.example.musicplayer.R;

import java.util.List;

public class SongListAdapter extends RecyclerView.Adapter<SongListAdapter.ViewHolder> {

    private List<SongListBean> list;

    private Context context;

    private OnItemClickListener onItemClickListener;

    public interface  OnItemClickListener{
        public void onItemClick(View view ,int position);
    }

    public SongListAdapter(List<SongListBean> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.recyclerview_main_song_sheet_item, null);
        return new SongListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SongListBean songListBean = list.get(position);

        holder.nameTV.setText(songListBean.getName());
        holder.countTV.setText(songListBean.getCount());

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

    class ViewHolder extends RecyclerView.ViewHolder{

        public TextView nameTV;

        public TextView countTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nameTV = itemView.findViewById(R.id.song_sheet_item_name);
            countTV = itemView.findViewById(R.id.song_sheet_item_count);
        }
    }
}
