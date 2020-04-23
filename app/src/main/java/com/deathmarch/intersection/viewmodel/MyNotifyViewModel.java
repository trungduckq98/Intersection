package com.deathmarch.intersection.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.deathmarch.intersection.model.MyNotification;
import com.deathmarch.intersection.repository.MyNotificationRepo;

import java.util.ArrayList;

public class MyNotifyViewModel extends ViewModel {
    LiveData<ArrayList<MyNotification>> liveDataNotify;
    private MyNotificationRepo myNotificationRepo = MyNotificationRepo.getInstane();
    public LiveData<ArrayList<MyNotification>> getLiveDataNotify(String cuurentUserId) {
        if (liveDataNotify==null){
            liveDataNotify = myNotificationRepo.getLiveData(cuurentUserId);
        }
        return liveDataNotify;
    }


}
