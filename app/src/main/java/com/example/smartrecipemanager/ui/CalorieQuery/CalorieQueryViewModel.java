package com.example.smartrecipemanager.ui.CalorieQuery;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CalorieQueryViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public CalorieQueryViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}