package com.deathmarch.intersection.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.deathmarch.intersection.model.Post;
import com.deathmarch.intersection.repository.PostRepository;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class PostViewModel extends ViewModel {
    private LiveData<ArrayList<Post>> liveDataPost;
    private PostRepository postRepository = PostRepository.getInstance();
    public void init(String userId){
       liveDataPost = postRepository.getLiveDataPost(userId);
    }
    public LiveData<ArrayList<Post>> getLiveDataPost(){
        return liveDataPost;
    }
}
