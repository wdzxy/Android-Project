package com.example.musicplayer.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bean.MainSongSheetBean;
import com.example.musicplayer.R;

import java.util.List;

public class MainSongSheetAdapter extends RecyclerView.Adapter<MainSongSheetAdapter.MainSongSheetViewHolder> {

    private List<MainSongSheetBean> list;
    private Context context;

    public MainSongSheetAdapter(List<MainSongSheetBean> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public MainSongSheetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //读取item的layout文件
        View view = View.inflate(context, R.layout.recyclerview_main_song_sheet_item, null);
        return new MainSongSheetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MainSongSheetViewHolder holder, int position) {

        //将list中的数据绑定到view中
        MainSongSheetBean mainSongSheetBean = list.get(position);
//        if (holder.imageView.getDrawable() == null) {
//            holder.imageView.setImageURI(Uri.fromFile(new File(mainSongSheetItem.getDrawAble())));
//        }
        holder.nameView.setText(mainSongSheetBean.getName());
        holder.countView.setText(String.valueOf(mainSongSheetBean.getCount())+"首");
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    class MainSongSheetViewHolder extends RecyclerView.ViewHolder{

        public ImageView imageView;

        public TextView nameView;

        public TextView countView;

        public MainSongSheetViewHolder(@NonNull View itemView) {
            super(itemView);
            //TODO
            //findViewById
            imageView = itemView.findViewById(R.id.song_sheet_item_img);
            nameView = itemView.findViewById(R.id.song_sheet_item_name);
            countView = itemView.findViewById(R.id.song_sheet_item_count);
        }
    }


}