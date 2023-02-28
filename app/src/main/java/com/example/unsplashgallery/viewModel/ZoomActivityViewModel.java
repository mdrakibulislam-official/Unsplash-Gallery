package com.example.unsplashgallery.viewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ZoomActivityViewModel extends ViewModel {
    String content;
    MutableLiveData<String> data;

    public MutableLiveData<String> getData() {
        data = new MutableLiveData<>();
        data.postValue(content);
        return data;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
