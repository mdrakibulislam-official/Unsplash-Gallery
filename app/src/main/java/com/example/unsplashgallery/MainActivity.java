package com.example.unsplashgallery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.unsplashgallery.Api.ApiUtilities;
import com.example.unsplashgallery.adapter.ImageAdapter;
import com.example.unsplashgallery.model.ImageModel;
import com.example.unsplashgallery.model.SearchModel;

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
    private int page = 1;
    private ProgressDialog progressDialog;

    private int pageSize = 30;
    private boolean isLoading;
    private boolean isLastPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        list = new ArrayList<>();
        adapter = new ImageAdapter(this, list);
        manager = new GridLayoutManager(this,3);

        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading");
        progressDialog.setCancelable(false);
        progressDialog.show();

        getData();

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int visibleItem = manager.getChildCount();
                int totalItem = manager.getItemCount();
                int firstVisibleItemPosition = manager.findFirstVisibleItemPosition();

                if (!isLoading && !isLastPage){
                    if ((visibleItem+firstVisibleItemPosition >=totalItem)
                        && firstVisibleItemPosition >= 0 && totalItem >= pageSize){
                        page++;
                        getData();
                    }
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    private void getData() {
        isLoading = true;
        ApiUtilities.getApiInterface().getImage(page, 30).enqueue(new Callback<List<ImageModel>>() {
            @Override
            public void onResponse(Call<List<ImageModel>> call, Response<List<ImageModel>> response) {

                if (response.body()!=null){
                    list.addAll(response.body());
                    adapter.notifyDataSetChanged();

                }

                isLoading = false;
                progressDialog.dismiss();

                if (list.size()>0){

                    isLastPage= list.size() <pageSize;
                }else isLastPage = true;
            }

            @Override
            public void onFailure(Call<List<ImageModel>> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(MainActivity.this,"Error : "+t.getMessage(),Toast.LENGTH_SHORT).show();
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
        ApiUtilities.getApiInterface().searchImage(query).enqueue(new Callback<SearchModel>() {
            @Override
            public void onResponse(Call<SearchModel> call, Response<SearchModel> response) {
                list.clear();
                list.addAll(response.body().getResults());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<SearchModel> call, Throwable t) {
                Toast.makeText(MainActivity.this,"Search Error: "+t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

    }
}