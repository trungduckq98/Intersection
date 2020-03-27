package com.deathmarch.intersection.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.deathmarch.intersection.model.Post;
import com.deathmarch.intersection.repository.PostRepository;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class PostViewModel extends ViewModel {
    private LiveData<ArrayList<Post>> liveDataPost;
    private PostRepository postRepository = PostRepository.getInstance();

    public LiveData<ArrayList<Post>> getLiveDataPost(String userId){
        Log.d("bbbbbbbb", "vao view model lay data");
        if (liveDataPost==null){
            Log.d("bbbbbbbb", "view model chua co data");
            liveDataPost = postRepository.getLiveDataPost(userId);
        }
        Log.d("bbbbbbbb", "view model da co data");
        return liveDataPost;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        postRepository.deleteListener();
    }
}
