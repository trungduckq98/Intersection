package com.deathmarch.intersection.adapter;

import androidx.recyclerview.widget.DiffUtil;

import com.deathmarch.intersection.model.UserMain;

import java.util.ArrayList;

public class FriendDiffUtilCallback extends DiffUtil.Callback {
    ArrayList<UserMain> olds;
    ArrayList<UserMain> news;

    public FriendDiffUtilCallback(ArrayList<UserMain> olds, ArrayList<UserMain> news) {
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
        String oldId = olds.get(oldItemPosition).getUserId();
        String newId = news.get(newItemPosition).getUserId();
        return oldId.equals(newId);
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return false;
    }
}

