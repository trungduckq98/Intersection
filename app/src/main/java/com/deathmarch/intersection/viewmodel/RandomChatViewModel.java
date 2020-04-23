package com.deathmarch.intersection.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.deathmarch.intersection.model.Messenger;
import com.deathmarch.intersection.repository.RandomChatRepo;

import java.util.ArrayList;

public class RandomChatViewModel extends ViewModel {
    private LiveData<ArrayList<Messenger>> liveDataMessenger;
    private RandomChatRepo randomChatRepo = RandomChatRepo.getInstance();


    public LiveData<ArrayList<Messenger>> getLiveDataMessenger(String currenrUserId){
        if (liveDataMessenger==null){
            liveDataMessenger = randomChatRepo.getLivedataRandomChat(currenrUserId);
        }
        return liveDataMessenger;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        randomChatRepo.deleteListener();
    }
}
