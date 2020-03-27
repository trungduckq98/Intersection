package com.deathmarch.intersection.viewmodel;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.RecyclerView;

import com.deathmarch.intersection.repository.ImageRepository;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ImageViewModel extends ViewModel {
    private LiveData<ArrayList<String>> liveDataImage;
    private ImageRepository imageRepository = ImageRepository.getInstance();
    public LiveData<ArrayList<String>> getLiveDataImage(String userId){
        if (liveDataImage==null){
            liveDataImage = imageRepository.getLiveDataImage(userId);
        }
        return liveDataImage;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        imageRepository.deleteListener();
    }
}
