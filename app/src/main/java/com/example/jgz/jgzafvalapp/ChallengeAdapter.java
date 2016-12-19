package com.example.jgz.jgzafvalapp;

/**
 * Created by Boy on 2-12-2016.
 */

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.example.jgz.jgzafvalapp.models.ChallengeItem;

/**
 * Created by Boy on 9-10-2016.
 */

public class ChallengeAdapter extends RecyclerView.Adapter<ChallengeAdapter.ViewHolder> {
    private List<ChallengeItem> mItems;
    private LayoutInflater mInflater;
    private ClickableListener listener;

    int day, resultDay;
    private int selectCount = 0;
    private  Boolean clickable;

    public ChallengeAdapter(Context context, List<ChallengeItem> result, Boolean clickable, ClickableListener listEventListener) {
        this.mInflater = LayoutInflater.from(context);
        this.mItems = result;
        this.clickable = clickable;
        this.listener = listEventListener;
        resultDay = Calendar.SUNDAY;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Calendar calendar = Calendar.getInstance();
        day = calendar.get(Calendar.DAY_OF_WEEK);

        ViewHolder viewHolder;

        if(day == resultDay && clickable == false)
            viewHolder = new ViewHolder(mInflater.inflate(R.layout.challenge_result_item_row, parent, false));
        else
            viewHolder = new ViewHolder(mInflater.inflate(R.layout.default_item_row, parent, false));

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final ChallengeItem item =  getItem(position);

        holder.title.setText(item.title);

        if(holder.yes == null) {
            holder.itemView.setBackgroundColor(Color.TRANSPARENT);
            holder.title.setTextColor(Color.BLACK);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickable) {
                    if (selectCount < 2 || item.selected) {
                        item.selected = !item.selected;
                        listener.onClick(position);
                        selectCount = ((item.selected == true) ? ++selectCount : --selectCount);
                        holder.itemView.setBackgroundColor((item.selected == true) ? Color.parseColor("#C3395A") : Color.TRANSPARENT);
                        holder.title.setTextColor((item.selected == true) ? Color.WHITE : Color.BLACK);
                    }
                }
            }
        });

        if(day == resultDay && holder.yes != null) {
            holder.yes.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    removeItem(position);
                }
            });

            holder.no.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    removeItem(position);
                }
            });

            holder.partial.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    removeItem(position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public ChallengeItem getItem(int position) { return mItems.get(position); }

    public void addItems(List<ChallengeItem> items) {
        mItems.addAll(items);
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        mItems.remove(position);
        notifyDataSetChanged();
    }

    public void clearList() {
        mItems.clear();
        notifyDataSetChanged();
    }

    public Set<String> getAllItemTitles() {
        if (mItems.size() == 2) {
            Set<String> itemTitles = new HashSet<>();
            for (ChallengeItem item : mItems) {
                itemTitles.add(item.title);
            }
            return itemTitles;
        }
        else return null;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public Button yes;
        public Button no;
        public Button partial;

        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);

        public ViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.item_title);

            if(day == Calendar.SUNDAY) {
                yes = (Button) itemView.findViewById(R.id.yes);
                no = (Button) itemView.findViewById(R.id.no);
                partial = (Button) itemView.findViewById(R.id.partial);
            }
        }
    }

    public void switchClickable() {
        clickable = !clickable;
    }

    public String getRemainingDays() {
        return String.valueOf(resultDay - day + 1);
    }
}
