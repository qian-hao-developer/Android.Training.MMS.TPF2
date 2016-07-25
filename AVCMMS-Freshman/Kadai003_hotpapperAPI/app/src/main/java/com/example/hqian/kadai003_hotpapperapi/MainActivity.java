package com.example.hqian.kadai003_hotpapperapi;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    //global parameters

    //private static final parameters


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.mapMenu:{
                if (isConnected()){
                    Intent intent = new Intent();
                    intent.setClassName("com.example.hqian.kadai003_hotpapperapi", "com.example.hqian.kadai003_hotpapperapi.MapsActivity");
                    startActivity(intent);
                }else {
                    new AlertDialog.Builder(this).setTitle("ERROR")
                            .setMessage("Network Connection failed!")
                            .setPositiveButton("YES", null)
                            .show();
                }
                break;
            }
            default:break;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean isConnected(){
        boolean connected = false;
        ConnectivityManager connMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()){
            connected = true;
        }

        return connected;
    }
}
