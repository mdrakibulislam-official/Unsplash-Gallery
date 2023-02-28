package com.example.unsplashgallery.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.unsplashgallery.repository.MainRepository;
import com.example.unsplashgallery.model.ImageModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivityViewModel extends AndroidViewModel {

    private ArrayList<ImageModel> list = new ArrayList<>();
    private ArrayList<ImageModel> searchList = new ArrayList<>();
    private int page = 0;
    private MainRepository repository;

    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        repository = MainRepository.getMainRepository(application);
    }

    public LiveData<List<ImageModel>> getImages(int page) {
        return repository.getImageList(page);
    }

    public MutableLiveData<List<ImageModel>> getSearchImages(String Query) {
        return repository.getSearchList(Query);
    }

    public int incrementPage() {
        page++;
        return page;
    }

    public List<ImageModel> getTotalImageList(List<ImageModel> imageModels) {
        list.addAll(imageModels);
        return list;
    }

    public List<ImageModel> getSearchImageList(List<ImageModel> imageModels) {
        searchList.addAll(imageModels);
        return searchList;
    }

    public void ClearAll() {
        searchList.clear();
    }
}
