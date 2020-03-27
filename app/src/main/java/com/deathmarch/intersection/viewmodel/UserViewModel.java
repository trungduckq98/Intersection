package com.deathmarch.intersection.viewmodel;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.deathmarch.intersection.model.User;
import com.deathmarch.intersection.repository.UserRepository;

public class UserViewModel extends ViewModel {
    private LiveData<User> liveDataUser;
    private UserRepository userRepository = UserRepository.getInstance();
    public LiveData<User> getLiveDataUser(String userId){
        Log.d("haiduong2020", "vao view model lay data");
        if (liveDataUser==null){
            Log.d("haiduong2020", "view model chua co data");
            liveDataUser = userRepository.getLiveDataUser(userId);
        }
        Log.d("haiduong2020", "view model co data");
        return liveDataUser;
    }

    @Override
    protected void onCleared() {
        Log.d("haiduong2020", "view model bi huy");
        super.onCleared();
        userRepository.removeListener();
    }


}
