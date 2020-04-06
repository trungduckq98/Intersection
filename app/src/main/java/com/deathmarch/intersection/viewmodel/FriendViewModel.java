package com.deathmarch.intersection.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.deathmarch.intersection.model.UserMain;
import com.deathmarch.intersection.repository.FriendRepository;

import java.util.ArrayList;

public class FriendViewModel extends ViewModel {
    private LiveData<ArrayList<UserMain>> liveDataFriend;
    private LiveData<ArrayList<UserMain>> liveDataReceive;
    private LiveData<ArrayList<UserMain>> liveDataSend;

    private FriendRepository friendRepository = FriendRepository.getInstance();

    public LiveData<ArrayList<UserMain>> getLiveDataFriend(String currentUserId){
        Log.d("kienquoc", "FRIEND: vao viewmodel laydata ");
        if (liveDataFriend==null){
            Log.d("kienquoc", "FRIEND: viewmodel chua co data ");
            liveDataFriend = friendRepository.getLivedataUserFriend(currentUserId);
        }
        Log.d("kienquoc", "FRIEND: viewmodel co data ");
        return liveDataFriend;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        friendRepository.deleteListener();
    }

        public LiveData<ArrayList<UserMain>> getLiveDataReceive(String currentUserId){
        if (liveDataReceive==null){
            liveDataReceive = friendRepository.getLivedataUserReceive(currentUserId);
        }
        return liveDataReceive;
    }


    public LiveData<ArrayList<UserMain>> getLiveDataSend(String currentUserId){
        if (liveDataSend==null){
            liveDataSend=friendRepository.getLivedataUserSend(currentUserId);
        }
        return liveDataSend;
    }


}
