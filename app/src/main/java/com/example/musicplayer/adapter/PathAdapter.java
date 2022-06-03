package com.example.musicplayer.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bean.PathBean;
import com.example.musicplayer.R;

import java.util.List;

public class PathAdapter extends RecyclerView.Adapter<PathAdapter.PathViewHolder> {

    private List<PathBean> list;

    private Context context;

    public PathAdapter(List<PathBean> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public PathViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.recyclerview_path, null);
        return new PathViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PathViewHolder holder, int position) {
        PathBean pathBean = list.get(position);
        holder.path.setText(pathBean.getPath());
        holder.count.setText(pathBean.getCount()+"首");
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    class PathViewHolder extends RecyclerView.ViewHolder {

        public TextView path;

        public TextView count;

        public PathViewHolder(@NonNull View itemView) {
            super(itemView);

            path = itemView.findViewById(R.id.path_name);
            count = itemView.findViewById(R.id.path_count);
        }
    }
}
