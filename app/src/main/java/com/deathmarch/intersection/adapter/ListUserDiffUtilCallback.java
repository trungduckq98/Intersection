package com.deathmarch.intersection.adapter;

import androidx.recyclerview.widget.DiffUtil;

import com.deathmarch.intersection.model.User;

import java.util.ArrayList;

public class ListUserDiffUtilCallback extends DiffUtil.Callback {
    ArrayList<User> olds, news;

    public ListUserDiffUtilCallback(ArrayList<User> olds, ArrayList<User> news) {
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
        String oldUser = olds.get(oldItemPosition).getUserMain().getUserId();
        String newUser = news.get(newItemPosition).getUserMain().getUserId();
        return oldUser.equals(newUser);
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return false;
    }
}
