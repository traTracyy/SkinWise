package com.example.skinwise.Model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SharedViewModel extends ViewModel {
    private final MutableLiveData<String> uidLiveData = new MutableLiveData<>();

    public void setUid(String uid) {
        uidLiveData.setValue(uid);
    }

    public LiveData<String> getUid() {
        return uidLiveData;
    }
}
