package com.example.jgz.jgzafvalapp;

import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jgz.jgzafvalapp.models.LogItem;

import java.util.List;

/**
 * Created by Boy on 17-11-2016.
 */

public class LogAdapter extends RecyclerView.Adapter<LogAdapter.ViewHolder> {
    private List<LogItem> mItems;
    private LayoutInflater mInflater;
    private ClickableListener listener;

    private Boolean workout;
    private Boolean delete;

    public LogAdapter(Context context, List<LogItem> result, Boolean workout, ClickableListener listEventListener) {
        mInflater = LayoutInflater.from(context);
        mItems = result;
        listener = listEventListener;
        delete = false;
        this.workout = workout;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater.inflate(R.layout.default_item_row, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        LogItem item = getItem(position);

        if(workout)
            holder.title.setText(item.amount + " min " + item.name);
        else
            holder.title.setText(item.amount + "X " + item.name);

        if(delete)
            holder.removeIcon.setVisibility(View.VISIBLE);
        else
            holder.removeIcon.setVisibility(View.GONE);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(delete){
                    listener.onClick(position);
                    removeItem(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public LogItem getItem(int position) { return mItems.get(position); }

    public void editItem(int position, LogItem log) {
        mItems.set(position, log);
        notifyDataSetChanged();
    }

    public void addItem(LogItem log) {
        mItems.add(log);
        notifyDataSetChanged();
    }

    public void addListItem(List<LogItem> logs) {
        mItems.addAll(logs);
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        mItems.remove(position);
        notifyDataSetChanged();
    }

    public void switchDeleteMode() {
        delete = !delete;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public ImageView removeIcon;

        public ViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.item_title);
            removeIcon = (ImageView) itemView.findViewById(R.id.remove_icon);
        }
    }
}