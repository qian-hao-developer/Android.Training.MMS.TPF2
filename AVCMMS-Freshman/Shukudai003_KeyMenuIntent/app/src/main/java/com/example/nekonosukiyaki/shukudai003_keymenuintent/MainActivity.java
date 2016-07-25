package com.example.nekonosukiyaki.shukudai003_keymenuintent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int MENUA = 1;
    private static final int MENUB = 2;
    private static final int MENUC = 3;
    private static final int MENUD = 4;
    private static final int MENU_GROUP_1 = 0;
    private static final int MENU_GROUP_2 = 1;

    private boolean changeMenu;

    private Button pushBTN;
    private EditText artistVew, albumView, trackView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pushBTN = (Button)findViewById(R.id.goButton);
        pushBTN.setOnClickListener(this);

        artistVew = (EditText) findViewById(R.id.artist_name_edit);
        albumView = (EditText)findViewById(R.id.album_name_edit);
        trackView = (EditText)findViewById(R.id.track_name_edit);

        changeMenu = false;
    }

    @Override
    public void onClick(View view) {
        if(view == pushBTN){
            if(artistVew.getText().toString().equals("") || albumView.getText().toString().equals("") || trackView.getText().toString().equals("")){
                Toast tst = Toast.makeText(this, "全ての情報をセットしてください", Toast.LENGTH_LONG);
                tst.show();
            }else {
                Intent intent = new Intent();
                intent.setClassName("com.example.nekonosukiyaki.shukudai003_keymenuintent", "com.example.nekonosukiyaki.shukudai003_keymenuintent.PlayActivity");
                intent.putExtra("artist", artistVew.getText().toString());
                intent.putExtra("album", albumView.getText().toString());
                intent.putExtra("track", trackView.getText().toString());
                startActivity(intent);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(MENU_GROUP_1, MENUA, 0, "楽曲A").setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
        menu.add(MENU_GROUP_1, MENUB, 0, "楽曲B").setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
        menu.add(MENU_GROUP_1, MENUC, 0, "楽曲C").setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);

        menu.add(MENU_GROUP_2, MENUD, 0, "オールクリア");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case MENUA:{
                artistVew.setText("嵐");
                albumView.setText("Beautiful World");
                trackView.setText("Lotus");
                changeMenu = true;
                break;
            }
            case MENUB:{
                artistVew.setText("aiko");
                albumView.setText("まとめ");
                trackView.setText("花火");
                changeMenu = true;
                break;
            }
            case MENUC:{
                artistVew.setText("GIRL NEXT DOOR");
                albumView.setText("NEXT FUTURE");
                trackView.setText("Infinity");
                changeMenu = true;
                break;
            }
            case MENUD:{
                artistVew.setText(null);
                albumView.setText(null);
                trackView.setText(null);
                changeMenu = false;
                break;
            }
            default:break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (changeMenu){
            menu.setGroupVisible(MENU_GROUP_1, false);
            menu.setGroupVisible(MENU_GROUP_2, true);
        }else {
            menu.setGroupVisible(MENU_GROUP_1, true);
            menu.setGroupVisible(MENU_GROUP_2, false);
        }
        return super.onPrepareOptionsMenu(menu);
    }
}
