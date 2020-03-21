package com.deathmarch.intersection.adapter;

import androidx.recyclerview.widget.DiffUtil;

import com.deathmarch.intersection.model.Messenger;

import java.util.ArrayList;

public class MessengerDiffUtilCallback extends DiffUtil.Callback {
    ArrayList<Messenger> olds;
    ArrayList<Messenger> news;

    public MessengerDiffUtilCallback(ArrayList<Messenger> olds, ArrayList<Messenger> news) {
        this.olds = olds;
        this.news = news;
    }

    @Override
    public int getOldListSize() {
        return olds.size();
    }

    @Override
    public int getNewListSize() {
        return news.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        long oldsTime = olds.get(oldItemPosition).getTime();
        long newsTime = news.get(newItemPosition).getTime();
        return oldsTime==newsTime;
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return false;
    }
}
