package com.panasonic.avc.mms.shukudai002_layoutbuttontoast;

import android.content.DialogInterface;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.app.NavUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements View.OnClickListener{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button_push = (Button)findViewById(R.id.button01);
        button_push.setOnClickListener(this);

        Button button_reset = (Button)findViewById(R.id.button02);
        button_reset.setOnClickListener(this);

        button_reset.setEnabled(false);
    }

    public void onClick(View view) {
        EditText text01 = (EditText)findViewById(R.id.editView01);
        EditText text02 = (EditText)findViewById(R.id.editView02);

        TextView text03 = (TextView)findViewById(R.id.textView03_2);
        TextView text04 = (TextView)findViewById(R.id.textView04_2);
        TextView text05 = (TextView)findViewById(R.id.textView05_2);

        Button button_reset = (Button)findViewById(R.id.button02);

        Toast tst;

        switch (view.getId()){
            case (R.id.button01):{
                if(text01.getText().toString().equals("") || text02.getText().toString().equals("")){
                    tst = Toast.makeText(this, "文字を入力して下さい", Toast.LENGTH_LONG);
                    tst.show();
                    break;
                }

                text03.setText(text01.getText());
                text04.setText(text02.getText());
                text05.setText(text01.getText().toString()+text02.getText().toString());

                text01.setText(null);
                text02.setText(null);

                button_reset.setEnabled(true);
                break;
            }
            case (R.id.button02):{
                text03.setText(null);
                text04.setText(null);
                text05.setText(null);

                button_reset.setEnabled(false);
                break;
            }
            default:break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }


}
