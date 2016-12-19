package com.example.jgz.jgzafvalapp;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.jgz.jgzafvalapp.models.Answer;
import com.example.jgz.jgzafvalapp.models.ChallengeItem;
import com.example.jgz.jgzafvalapp.models.Question;

import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Boy on 11-12-2016.
 */

public class QuizAdapter extends RecyclerView.Adapter<QuizAdapter.ViewHolder> {
    private List<Answer> mItems;
    private List<Answer> rightAnswers;
    private LayoutInflater mInflater;
    private ClickableListener listener;

    private int selectCount = 0;
    private int characterNumber = 65;

    public QuizAdapter(Context context, List<Answer> result, ClickableListener listEventListener) {
        this.mInflater = LayoutInflater.from(context);
        this.mItems = result;
        this.listener = listEventListener;
    }

    @Override
    public QuizAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new QuizAdapter.ViewHolder(mInflater.inflate(R.layout.quiz_item_row, parent, false));
    }

    @Override
    public void onBindViewHolder(final QuizAdapter.ViewHolder holder, final int position) {
        final Answer item =  getItem(position);

        if(item.rightAnswer == true) {
            rightAnswers.add(item);
        }

        holder.title.setText(((char) characterNumber) + " - " + item.answer);
//        holder.itemView.setBackgroundColor(Color.TRANSPARENT);
        characterNumber++;

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectCount < 1 || item.selected) {
                    item.selected = !item.selected;
                    listener.onClick(position);
                    selectCount = ((item.selected == true) ? ++selectCount : --selectCount);
                    holder.itemView.setBackgroundColor((item.selected == true) ? Color.parseColor("#00A3E0") : Color.TRANSPARENT);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public Answer getItem(int position) { return mItems.get(position); }

    public void addItems(List<Answer> items) {
        mItems.addAll(items);
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        mItems.remove(position);
        notifyDataSetChanged();
    }

    public void clearList() {
        mItems.clear();
        characterNumber = 65;
        notifyDataSetChanged();
    }

    public List<Answer> getRightAnswers() {
        return rightAnswers;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title;

        public ViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.answer_name);
        }
    }
}
