package com.example.unsplashgallery.view.UI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


import com.example.unsplashgallery.R;
import com.example.unsplashgallery.view.adapter.ImageAdapter;
import com.example.unsplashgallery.databinding.ActivityMainBinding;
import com.example.unsplashgallery.model.ImageModel;
import com.example.unsplashgallery.model.SearchModel;
import com.example.unsplashgallery.network.RetrofitClient;
import com.example.unsplashgallery.viewModel.MainActivityViewModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<ImageModel> list;

    private GridLayoutManager manager;
    private ImageAdapter adapter;
    private ProgressDialog progressDialog;
    private int pageSize = 30;
    private boolean isLoading;
    private boolean isLastPage;
    private MainActivityViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding mainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mainBinding.getRoot());

        recyclerView = mainBinding.recView;

        list = new ArrayList<>();
        adapter = new ImageAdapter(this, list);
        manager = new GridLayoutManager(this, 3);

        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading");
        progressDialog.setCancelable(false);
        progressDialog.show();

        viewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);
        int page = viewModel.incrementPage();
        showImage(page);


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int visibleItem = manager.getChildCount();
                int totalItem = manager.getItemCount();
                int firstVisibleItemPosition = manager.findFirstVisibleItemPosition();

                if (!isLoading && !isLastPage) {
                    if ((visibleItem + firstVisibleItemPosition >= totalItem)
                            && firstVisibleItemPosition >= 0 && totalItem >= pageSize) {

                        int page = viewModel.incrementPage();
                        showImage(page);

                    }
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    private void showImage(int pageNumber) {
        isLoading = true;

        viewModel.getImages(pageNumber).observe(this, new Observer<List<ImageModel>>() {
            @Override
            public void onChanged(List<ImageModel> imageModels) {
                if (imageModels != null) {
                    list.addAll(viewModel.getTotalImageList(imageModels));
                    adapter.notifyDataSetChanged();
                }
                isLoading = false;
                progressDialog.dismiss();

                if (list.size() > 0) {
                    isLastPage = list.size() < pageSize;
                } else isLastPage = true;
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_option, menu);

        MenuItem search = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) search.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                progressDialog.show();
                searchData(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return true;
    }

    private void searchData(String query) {
        progressDialog.dismiss();
        viewModel.getSearchImages(query).observe(this, new Observer<List<ImageModel>>() {
            @Override
            public void onChanged(List<ImageModel> imageModel) {

                //viewModel.ClearAll();
                list.clear();
                list.addAll(imageModel);
                //list.addAll(viewModel.getSearchImageList(imageModel));

                adapter.notifyDataSetChanged();
            }
        });

    }
}