package com.example.unsplashgallery.view.UI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.bumptech.glide.Glide;
import com.example.unsplashgallery.R;

import com.example.unsplashgallery.databinding.ActivityZoomBinding;
import com.example.unsplashgallery.service.DownloadImage;
import com.example.unsplashgallery.viewModel.ZoomActivityViewModel;


public class ZoomActivity extends AppCompatActivity {
    String content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityZoomBinding zoomBinding = ActivityZoomBinding.inflate(getLayoutInflater());
        setContentView(zoomBinding.getRoot());

        content = getIntent().getStringExtra("image");

        ZoomActivityViewModel image = new ViewModelProvider(this).get(ZoomActivityViewModel.class);
        image.setContent(content);

        image.getData().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Log.d("TAG", s);
                Glide.with(ZoomActivity.this).load(s).into(zoomBinding.zoomView);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.download_option, menu);
        MenuItem search = menu.findItem(R.id.download);
        search.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(@NonNull MenuItem menuItem) {

                String url = content;
                DownloadImage downloadImage = new DownloadImage("Unsplash Image", url, ZoomActivity.this);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_GRANTED) {

                        downloadImage.download();
                    } else {
                        ActivityCompat.requestPermissions(ZoomActivity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                        downloadImage.download();
                    }
                }
                return true;
            }
        });
        return true;
    }
}