package com.example.dg_andriod.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {

    enum HomeStatus {
        LOADING,
        SCANNING,
        SEARCHING,
        SAVING,
        FOUND
    };
    private MutableLiveData<HomeStatus> mStatus;

    public HomeViewModel() {
        mStatus = new MutableLiveData<>();
        mStatus.setValue(HomeStatus.SCANNING);
    }

    public LiveData<HomeStatus> getStatus() {
        return mStatus;
    }
}