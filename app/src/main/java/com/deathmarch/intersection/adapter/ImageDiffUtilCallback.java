package com.deathmarch.intersection.adapter;

import androidx.recyclerview.widget.DiffUtil;

import java.util.ArrayList;

public class ImageDiffUtilCallback extends DiffUtil.Callback {
    ArrayList<String> olds;
    ArrayList<String> news;

    public ImageDiffUtilCallback(ArrayList<String> olds, ArrayList<String> news) {
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
        return olds.get(oldItemPosition).equals(news.get(newItemPosition));
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return false;
    }
}
