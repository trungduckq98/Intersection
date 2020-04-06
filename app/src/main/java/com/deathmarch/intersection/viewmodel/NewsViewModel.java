package com.deathmarch.intersection.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.deathmarch.intersection.model.Post;
import com.deathmarch.intersection.repository.NewsRepository;

import java.util.ArrayList;

public class NewsViewModel extends ViewModel {
    private LiveData<ArrayList<Post>> liveDataPost;

    private NewsRepository newsRepository = NewsRepository.getInstance();

    public LiveData<ArrayList<Post>> getLiveDataPost(String currentUserId) {
        if (liveDataPost==null){
            liveDataPost = newsRepository.getLiveDataPost(currentUserId);
        }
        return liveDataPost;
    }
}
