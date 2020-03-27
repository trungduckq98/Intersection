package com.deathmarch.intersection.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.deathmarch.intersection.model.Comment;
import com.deathmarch.intersection.repository.CommentRepository;

import java.util.ArrayList;

public class CommentViewModel extends ViewModel {
    private CommentRepository commentRepository = CommentRepository.getInstance();
    private LiveData<ArrayList<Comment>> liveDataComment;
    public LiveData<ArrayList<Comment>> getLiveDataComment(String postUserId, String postId ){
        Log.d("vvvvvvvvvv", "vao view model lay data");
        if (liveDataComment==null){
            Log.d("vvvvvvvvvv", "view model chua co data");
            liveDataComment = commentRepository.getLiveDataCmt(postUserId, postId);
        }
        Log.d("vvvvvvvvvv", "view model co data");
        return liveDataComment;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        Log.d("vvvvvvvvvv", "view model bi huy");
        commentRepository.deleteListener();
    }
}
