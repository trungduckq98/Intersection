package com.deathmarch.intersection.adapter;

import androidx.recyclerview.widget.DiffUtil;

import com.deathmarch.intersection.model.MyNotification;

import java.util.ArrayList;

public class MyNotifyDiffUtillCallback extends DiffUtil.Callback {
    ArrayList<MyNotification> olds;
    ArrayList<MyNotification> news;

    public MyNotifyDiffUtillCallback(ArrayList<MyNotification> olds, ArrayList<MyNotification> news) {
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
        String oldPostId = olds.get(oldItemPosition).getPostId();
        String newPostId = news.get(newItemPosition).getPostId();

        String oldNotifyType = olds.get(oldItemPosition).getNotifyType();
        String newNotifyType = news.get(newItemPosition).getNotifyType();
        return oldPostId.equals(newPostId) && oldNotifyType.equals(newNotifyType);
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return true;
    }
}
