package com.deathmarch.intersection.viewmodel;

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

    public void initFriend(){
        if (liveDataFriend==null){
            liveDataFriend = friendRepository.getLivedataUserFriend();
        }
    }
    public LiveData<ArrayList<UserMain>> getLiveDataFriend(){
        return liveDataFriend;
    }

    public void initReceive(){
      //  if (liveDataReceive==null){
            liveDataReceive = friendRepository.getLivedataUserReceive();
     //  }
    }

    public LiveData<ArrayList<UserMain>> getLiveDataReceive(){
        return liveDataReceive;
    }

    public void initSend(){
        if (liveDataSend==null){
            liveDataSend = friendRepository.getLivedataUserSend();
        }
    }

    public LiveData<ArrayList<UserMain>> getLiveDataSend(){
        return liveDataSend;
    }


}
