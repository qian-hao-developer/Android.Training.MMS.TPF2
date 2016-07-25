package com.example.hqian.kadai002_picapp;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CAMERA = 1;

    private ArrayAdapter<String> listAdapter;
    private ListView listView;
    private Uri mImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.mainListView);
        listAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.listitem);
        listView.setAdapter(listAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView recView = (TextView) view;
                showImage(recView.getText().toString());
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, final View view, int i, long l) {
                new AlertDialog.Builder(MainActivity.this).setTitle("DELETE")
                        .setMessage("Are you sure to Delete this Picture?")
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                deleteImage(((TextView)view).getText().toString());
                            }
                        })
                        .setNegativeButton("NO", null)
                        .show();

                return true;  //trueを返しておけば、その後のイベントはキャンセルされる
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.cameraMenu: {
                callCamera();
                break;
            }
            case R.id.reloadMenu: {
                reloadList();
                break;
            }
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void callCamera() {
        mImageUri = getPhotoUri();
        Intent intent = new Intent();
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CAMERA) {
            if (resultCode == RESULT_OK) {
                reloadList();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
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

    public Uri getPhotoUri() {
        long currentTimeMillis = System.currentTimeMillis();
        Date today = new Date(currentTimeMillis);
        java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyyyMMdd_HHmmss");
        String title = dateFormat.format(today);
        String dirPath = getDirPath();
        String fileName = "photo_" + title + ".jpg";
        String path = dirPath + "/" + fileName;
        File file = new File(path);

//        ContentValues values = new ContentValues();
//        values.put(MediaStore.Images.Media.TITLE, title);
//        values.put(MediaStore.Images.Media.DISPLAY_NAME, fileName);
//        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
//        values.put(MediaStore.Images.Media.DATA, path);
//        values.put(MediaStore.Images.Media.DATE_TAKEN, currentTimeMillis);
//        if (file.exists()){
//            values.put(MediaStore.Images.Media.SIZE, file.length());
//        }
//        Uri uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Uri uri = Uri.fromFile(file);
        return uri;
    }

    public void reloadList() {
        File[] allFile = new File(getDirPath() + "/").listFiles();
//        if (allFile != null) {
        listAdapter.clear();
        for (File file : allFile) {
            listAdapter.add(file.getName().toString());
        }
//        }
    }

    public void showImage(String path) {
        Intent intent = new Intent();
        intent.setClassName("com.example.hqian.kadai002_picapp", "com.example.hqian.kadai002_picapp.ImageActivity");
        intent.putExtra("viewPath", path);
        startActivity(intent);
    }

    public void deleteImage(String path){
        String dirPath = getDirPath();
        String filePath = dirPath + "/" + path;
        File file = new File(filePath);
        file.delete();
        reloadList();
    }
}
