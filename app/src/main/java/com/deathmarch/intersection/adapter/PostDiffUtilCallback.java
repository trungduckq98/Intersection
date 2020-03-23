package com.deathmarch.intersection.adapter;

import androidx.recyclerview.widget.DiffUtil;

import com.deathmarch.intersection.model.Post;

import java.util.ArrayList;

public class PostDiffUtilCallback extends DiffUtil.Callback {
    ArrayList<Post> olds;
    ArrayList<Post> news;

    public PostDiffUtilCallback(ArrayList<Post> olds, ArrayList<Post> news) {
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
        return olds.get(oldItemPosition).getPostId().equals(news.get(newItemPosition).getPostId());
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return false;
    }
}
