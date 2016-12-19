package com.example.jgz.jgzafvalapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.StrictMode;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.jgz.jgzafvalapp.models.ChallengeItem;
import com.example.jgz.jgzafvalapp.models.InfoItem;

import java.net.URL;
import java.util.List;

/**
 * Created by Boy on 4-12-2016.
 */

public class InfoAdapter extends RecyclerView.Adapter<InfoAdapter.ViewHolder> {
    private List<InfoItem> mItems;
    private LayoutInflater mInflater;
    private ClickableListener listener;
    private Context context;

    public InfoAdapter(Context context, List<InfoItem> result, ClickableListener listEventListener) {
        mInflater = LayoutInflater.from(context);
        mItems = result;
        listener = listEventListener;
        this.context = context;
    }

    @Override
    public InfoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new InfoAdapter.ViewHolder(mInflater.inflate(R.layout.info_item_row, parent, false));
    }

    @Override
    public void onBindViewHolder(final InfoAdapter.ViewHolder holder, final int position) {
        final InfoItem item =  getItem(position);

        holder.title.setText(item.title);
        holder.summary.setText(item.summary);

        Glide.with(context).load(item.imageUrl).crossFade().fitCenter().into(holder.image);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public InfoItem getItem(int position) { return mItems.get(position); }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public TextView summary;
        public ImageView image;

        public ViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.info_item_title);
            summary = (TextView) itemView.findViewById(R.id.info_item_text);
            image = (ImageView) itemView.findViewById(R.id.info_item_image);
        }
    }
}
