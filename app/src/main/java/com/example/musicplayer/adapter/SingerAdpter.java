package com.example.musicplayer.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bean.SingerBean;
import com.example.musicplayer.R;

import org.w3c.dom.Text;
import org.w3c.dom.ls.LSOutput;

import java.util.List;

public class SingerAdpter extends RecyclerView.Adapter<SingerAdpter.SingerViewHolder> {

    private List<SingerBean> list;

    private Context context;

    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener{
        public void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(SingerAdpter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }


    public SingerAdpter(List<SingerBean> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public SingerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = View.inflate(context, R.layout.recyclerview_singer, null);
        return new SingerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SingerViewHolder holder, @SuppressLint("RecyclerView") int position) {

        SingerBean singerBean = list.get(position);
        holder.singer.setText(singerBean.getSinger());
        holder.count.setText(singerBean.getCount()+"首");

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

    class SingerViewHolder extends RecyclerView.ViewHolder{

        public TextView singer;

        public TextView count;

        public SingerViewHolder(@NonNull View itemView) {
            super(itemView);

            singer = (TextView) itemView.findViewById(R.id.singer_name);
            count = (TextView) itemView.findViewById(R.id.singer_count);
        }
    }
}
