package com.deathmarch.intersection.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.deathmarch.intersection.model.User;
import com.deathmarch.intersection.repository.FriendOnlineRepository;

import java.util.ArrayList;

public class FriendOnlineViewModel extends ViewModel {
    private LiveData<ArrayList<User>> liveDataFriendOnline;
    private FriendOnlineRepository friendOnlineRepository = FriendOnlineRepository.getInstance();

    public LiveData<ArrayList<User>> getLiveDataFriendOnline(String currentUserId) {
        if (liveDataFriendOnline==null){
            liveDataFriendOnline = friendOnlineRepository.getLiveDataFriendOnline(currentUserId);
        }
        return liveDataFriendOnline;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }
}
