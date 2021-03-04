package com.example.smartrecipemanager.ui.CalorieHistory;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CalorieHistoryViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public CalorieHistoryViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is notifications fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}