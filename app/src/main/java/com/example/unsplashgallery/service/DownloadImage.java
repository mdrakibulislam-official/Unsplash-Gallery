package com.example.unsplashgallery.service;


import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;

public class DownloadImage {
    private String fileName;
    private String url;
    private Context context;

    public DownloadImage(String fileName, String url, Context context) {
        this.fileName = fileName;
        this.url = url;
        this.context = context;
    }

    public void download() {
        try {
            DownloadManager manager = null;

            manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);

            Uri downloadUri = Uri.parse(url);

            DownloadManager.Request request = new DownloadManager.Request(downloadUri);
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                    .setAllowedOverRoaming(false)
                    .setTitle(fileName)
                    .setMimeType("image/jpeg")
                    .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                    .setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES, File.separator + fileName + System.currentTimeMillis() + ".jpg");

            manager.enqueue(request);

            Toast.makeText(context, "Download Image Successfully", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();

        }
    }
}
