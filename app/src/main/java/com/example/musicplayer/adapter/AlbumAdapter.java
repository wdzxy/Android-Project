package com.example.musicplayer.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bean.AlbumBean;
import com.example.bean.SingleSongBean;
import com.example.musicplayer.R;

import java.util.List;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.AlbumViewHolder> {

    private List<AlbumBean> list;
    private Context context;

    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener{
        public void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(AlbumAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public AlbumAdapter(List<AlbumBean> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public AlbumViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //读取item的layout文件
        View view = View.inflate(context, R.layout.recyclerview_album, null);
        return new AlbumViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumViewHolder holder, @SuppressLint("RecyclerView") int position) {

        //将list中的数据绑定到view中
        AlbumBean albumBean = list.get(position);
//        if (holder.imageView.getDrawable() == null) {
//            holder.imageView.setImageURI(Uri.fromFile(new File(mainSongSheetItem.getDrawAble())));
//        }
        holder.nameView.setText(albumBean.getAlbumName());
        holder.countView.setText(String.valueOf(albumBean.getCount())+"首");

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

    class AlbumViewHolder extends RecyclerView.ViewHolder{

        public ImageView imageView;

        public TextView nameView;

        public TextView countView;

        public AlbumViewHolder(@NonNull View itemView) {
            super(itemView);
            //TODO
            //findViewById
            imageView = itemView.findViewById(R.id.song_sheet_item_img);
            nameView = itemView.findViewById(R.id.song_sheet_item_name);
            countView = itemView.findViewById(R.id.song_sheet_item_count);
        }
    }


}
