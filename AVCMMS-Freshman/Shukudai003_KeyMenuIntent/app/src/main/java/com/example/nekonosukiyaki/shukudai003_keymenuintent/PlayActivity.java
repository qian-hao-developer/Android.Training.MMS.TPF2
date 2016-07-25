package com.example.nekonosukiyaki.shukudai003_keymenuintent;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.widget.TextView;
import android.widget.ToggleButton;

public class PlayActivity extends AppCompatActivity {

    private TextView artistView, albumView, trackView;
    private ToggleButton togBTN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        artistView = (TextView)findViewById(R.id.artist_name_rec);
        albumView = (TextView)findViewById(R.id.album_name_rec);
        trackView = (TextView)findViewById(R.id.track_name_rec);
        togBTN = (ToggleButton)findViewById(R.id.togButton);

        Intent intent = getIntent();
        if(intent != null){
            artistView.setText(intent.getStringExtra("artist"));
            albumView.setText(intent.getStringExtra("album"));
            trackView.setText(intent.getStringExtra("track"));
        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK){
            if (event.getAction() == KeyEvent.ACTION_DOWN){
                if (togBTN.isChecked()){
                    return false;
                }
            }
        }
        return super.dispatchKeyEvent(event);
    }
}
