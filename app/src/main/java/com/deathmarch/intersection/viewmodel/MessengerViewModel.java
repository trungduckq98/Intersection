package com.deathmarch.intersection.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.deathmarch.intersection.model.Messenger;
import com.deathmarch.intersection.repository.MessengerRepository;

import java.util.ArrayList;

public class MessengerViewModel extends ViewModel {
    private LiveData<ArrayList<Messenger>> liveDataMessenger;
    private MessengerRepository messengerRepository = MessengerRepository.getInstance();


    public LiveData<ArrayList<Messenger>> getLiveDataMessenger(String currenrUserId, String anotherUserId){
        if (liveDataMessenger==null){
            liveDataMessenger = messengerRepository.getLivedataMessenger(currenrUserId, anotherUserId);
        }
        return liveDataMessenger;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        messengerRepository.deleteListener();
    }
}
