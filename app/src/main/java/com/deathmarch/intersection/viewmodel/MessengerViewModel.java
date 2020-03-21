package com.deathmarch.intersection.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.deathmarch.intersection.model.Messenger;
import com.deathmarch.intersection.model.UserMain;
import com.deathmarch.intersection.repository.FriendRepository;
import com.deathmarch.intersection.repository.MessengerRepository;

import java.util.ArrayList;

public class MessengerViewModel extends ViewModel {
    private LiveData<ArrayList<Messenger>> liveDataMessenger;
    private MessengerRepository messengerRepository = MessengerRepository.getInstance();
    public void init(String anotherUserId){
        liveDataMessenger = messengerRepository.getLivedataMessenger(anotherUserId);
    }

    public LiveData<ArrayList<Messenger>> getLiveDataMessenger(){
        return liveDataMessenger;
    }

}
