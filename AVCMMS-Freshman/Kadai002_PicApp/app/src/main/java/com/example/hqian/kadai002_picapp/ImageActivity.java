package com.example.hqian.kadai002_picapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import java.io.File;

public class ImageActivity extends AppCompatActivity {

    ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        mImageView = (ImageView)findViewById(R.id.imageView);

        Intent intent = getIntent();
        if (intent != null){
            showImg(intent.getStringExtra("viewPath"));
        }
    }

    public void showImg(String imgPath){
        String dirPath = getDirPath();
        String filePath = dirPath + "/" + imgPath;
        Uri uri = Uri.fromFile(new File(filePath));
        mImageView.setImageURI(uri);
    }

    public String getDirPath() {
        String dirPath = "";
        //after android 4.4, we can not use external storage directly
        //but we can use our own directory, so we use it
        File extStorageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (extStorageDir.canWrite()) {
            dirPath = extStorageDir.getPath();
        }

        return dirPath;
    }
}
