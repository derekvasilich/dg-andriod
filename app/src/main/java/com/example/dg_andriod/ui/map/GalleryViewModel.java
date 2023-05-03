package com.example.dg_andriod.ui.map;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.dg_andriod.data.remote.UserService;

public class GalleryViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    private UserService userService;

    public GalleryViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is gallery fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }


}