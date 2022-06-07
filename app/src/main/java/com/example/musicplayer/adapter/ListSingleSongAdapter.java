package com.example.musicplayer.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bean.SingleSongBean;
import com.example.bean.SongListBean;
import com.example.db.DBHelper;
import com.example.musicplayer.R;

import java.util.List;

public class ListSingleSongAdapter extends RecyclerView.Adapter<ListSingleSongAdapter.ListSingleSongViewHolder> {

    private List<SingleSongBean> list;

    private Context context;

    private int songListId;

    private TextView countTV;

    private ListSingleSongAdapter that;

    private ListSingleSongAdapter.OnItemClickListener onItemClickListener;

    public interface OnItemClickListener{
        public void onItemClick(View view, int position);
    }

    public ListSingleSongAdapter(List<SingleSongBean> list, Context context, int songListId, TextView countTV) {
        this.list = list;
        this.context = context;
        this.songListId = songListId;
        this.that = this;
        this.countTV = countTV;
    }

    public void setOnItemClickListener(ListSingleSongAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public ListSingleSongAdapter(List<SingleSongBean> list, Context context){
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
    public ListSingleSongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.recyclerview_song_list_single_item, null);
        return new ListSingleSongViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListSingleSongViewHolder holder, @SuppressLint("RecyclerView") int position) {
        SingleSongBean singleSongBean = list.get(position);

        holder.idTV.setText(singleSongBean.getId());
        holder.songTV.setText(singleSongBean.getSong());
        holder.singerTV.setText(singleSongBean.getSinger());
        holder.timeTV.setText(singleSongBean.getDuration());

        holder.removeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRemoveDialog(singleSongBean);
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(v,position);
            }
        });
    }

    private void showRemoveDialog(SingleSongBean singleSongBean) {
        final AlertDialog.Builder normalDialog = new AlertDialog.Builder(context);
        normalDialog.setTitle("删除" + singleSongBean.getSong());
        normalDialog.setMessage("确定删除单曲" + singleSongBean.getSong() + "?");
        normalDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DBHelper db = new DBHelper(context);
                        List<SingleSongBean> singleSongBeans = db.removeSong(singleSongBean.getID(),songListId);
                        list = singleSongBeans;
                        that.notifyDataSetChanged();

                        countTV.setText(list.size() + "首");
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

    class ListSingleSongViewHolder extends RecyclerView.ViewHolder{

        public TextView idTV,songTV,singerTV,timeTV;

        public Button removeBtn;

        public ListSingleSongViewHolder(@NonNull View itemView) {
            super(itemView);
            idTV = itemView.findViewById(R.id.item_local_music_num);
            songTV = itemView.findViewById(R.id.item_local_music_song);
            singerTV = itemView.findViewById(R.id.item_local_music_singer);
            timeTV = itemView.findViewById(R.id.item_local_music_duration);
            removeBtn = itemView.findViewById(R.id.song_list_single_delete);
        }
    }
}
