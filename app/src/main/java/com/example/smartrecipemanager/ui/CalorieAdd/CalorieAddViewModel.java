package com.example.smartrecipemanager.ui.CalorieAdd;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
/**
 * auto create, responsible for preparing data for the UI
 * */
public class CalorieAddViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public CalorieAddViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is dashboard fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}