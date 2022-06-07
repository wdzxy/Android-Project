package com.example.musicplayer.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bean.SongListBean;
import com.example.db.DBHelper;
import com.example.musicplayer.R;

import java.util.List;

public class SongListAdapter extends RecyclerView.Adapter<SongListAdapter.ViewHolder> {

    private List<SongListBean> list;

    private Context context;

    private OnItemClickListener onItemClickListener;

    private SongListAdapter that;

    public interface  OnItemClickListener{
        public void onItemClick(View view ,int position);
    }

    public SongListAdapter(List<SongListBean> list, Context context) {
        this.list = list;
        this.context = context;
        this.that = this;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public List<SongListBean> getList() {
        return list;
    }

    public void setList(List<SongListBean> list) {
        this.list = list;
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
        holder.countTV.setText(String.valueOf(songListBean.getCount()));

        holder.removeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfirmDialog(songListBean);
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(v,position);
            }
        });
    }

    public void showConfirmDialog(SongListBean songListBean){
        final AlertDialog.Builder normalDialog = new AlertDialog.Builder(context);
        normalDialog.setTitle("删除" + songListBean.getName());
        normalDialog.setMessage("确定删除歌单" + songListBean.getName() + "?");
        normalDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DBHelper db = new DBHelper(context);
                        List<SongListBean> songListBeans = db.removeSongList(songListBean.getId());
                        list = songListBeans;
                        that.notifyDataSetChanged();
                    }
                });
        normalDialog.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //...To-do
                    }
                });
        // 显示
        normalDialog.show();
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public TextView nameTV;

        public TextView countTV;

        public Button removeBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nameTV = itemView.findViewById(R.id.song_sheet_item_name);
            countTV = itemView.findViewById(R.id.song_list_count);
            removeBtn = itemView.findViewById(R.id.remove_song_list);
        }
    }
}
