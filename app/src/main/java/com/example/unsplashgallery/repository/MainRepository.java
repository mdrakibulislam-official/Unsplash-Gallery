package com.example.unsplashgallery.repository;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;

import com.example.unsplashgallery.model.ImageModel;
import com.example.unsplashgallery.model.SearchModel;
import com.example.unsplashgallery.network.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainRepository {

    private static MainRepository mainRepository;
    private static Context mContext;
    private MutableLiveData liveData;
    private MutableLiveData liveSearchData;

    private int pageSize = 30;

    private List<ImageModel> imageModelList;
    private List<ImageModel> searchModelList;


    public static MainRepository getMainRepository(Context context) {

        if (mainRepository == null) {
            mContext = context;
            mainRepository = new MainRepository();
        }
        return mainRepository;
    }

    public MutableLiveData<List<ImageModel>> getImageList(int page) {

        if (liveData == null) {
            liveData = new MutableLiveData<>();
        }
        RetrofitClient.getApiInterface().getImage(page, 30).enqueue(new Callback<List<ImageModel>>() {
            @Override
            public void onResponse(Call<List<ImageModel>> call, Response<List<ImageModel>> response) {

                imageModelList = response.body();
                liveData.postValue(imageModelList);

            }

            @Override
            public void onFailure(Call<List<ImageModel>> call, Throwable t) {

            }
        });
        return liveData;
    }

    public MutableLiveData<List<ImageModel>> getSearchList(String query) {
        if (liveSearchData == null) {
            liveSearchData = new MutableLiveData<>();
        }
        RetrofitClient.getApiInterface().searchImage(query).enqueue(new Callback<SearchModel>() {
            @Override
            public void onResponse(Call<SearchModel> call, Response<SearchModel> response) {
                searchModelList = response.body().getResults();
                liveSearchData.postValue(searchModelList);
            }

            @Override
            public void onFailure(Call<SearchModel> call, Throwable t) {

            }
        });
        return liveSearchData;
    }
}

