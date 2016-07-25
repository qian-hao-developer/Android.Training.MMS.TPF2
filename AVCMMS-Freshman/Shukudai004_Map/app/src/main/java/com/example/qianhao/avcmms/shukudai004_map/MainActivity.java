package com.example.qianhao.avcmms.shukudai004_map;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button pushBTN;
    private EditText readText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pushBTN = (Button)findViewById(R.id.pushBTN);
        pushBTN.setOnClickListener(this);

        readText = (EditText)findViewById(R.id.editView);
    }

    @Override
    public void onClick(View v) {
        if (v == pushBTN){
            if (readText.getText().toString().equals("")){
                Toast.makeText(this, "地名を入力してください", Toast.LENGTH_LONG).show();
            }else {
                Intent intent = new Intent();
//                intent.setClassName("com.example.qianhao.avcmms.shukudai004_map", "com.example.qianhao.avcmms.shukudai004_map.MyMapActivity");
                intent.setClassName("com.example.qianhao.avcmms.shukudai004_map", "com.example.qianhao.avcmms.shukudai004_map.MapsActivity");
                intent.putExtra("searchPlace", readText.getText().toString());
                startActivity(intent);
            }
        }
    }
}
