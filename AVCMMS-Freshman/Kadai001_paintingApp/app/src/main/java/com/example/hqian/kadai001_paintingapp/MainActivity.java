package com.example.hqian.kadai001_paintingapp;

import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    private boolean changeToEraserMenu = false;
    private GraphicsView graphicsView;
    private static final String strCOLORS[] = {"Black", "Red", "Green", "Blue"};
    private static final int COLORS[] = {Color.BLACK, Color.RED, Color.GREEN, Color.BLUE};
    private static final String strSIZES[] = {"5px", "10px", "15px", "20px", "25px"};
    private static final float SIZES[] = {5f, 10f, 15f, 20f, 25f};
    private int defaultItem_color = 0;
    private int defaultItem_size = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        graphicsView = new GraphicsView(this);
        setContentView(graphicsView);

        graphicsView.translateData_color(COLORS[defaultItem_color]);
        graphicsView.translateData_size(SIZES[defaultItem_size]);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (changeToEraserMenu) {
            menu.setGroupVisible(R.id.eraserGroup, true);
            menu.setGroupVisible(R.id.paintGroup, false);
        } else {
            menu.setGroupVisible(R.id.eraserGroup, false);
            menu.setGroupVisible(R.id.paintGroup, true);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        // set "all clear" text color red
//        MenuItem allClear = menu.getItem(menu.size()-1);      // get item by index
        MenuItem allClear = menu.findItem(R.id.allClearMenu);   // get item by id
        SpannableString spanString = new SpannableString(allClear.getTitle().toString());
        spanString.setSpan(new ForegroundColorSpan(Color.RED), 0, spanString.length(), 0);
        allClear.setTitle(spanString);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // paintGroup
            case R.id.eraserMenu: {
                graphicsView.translateData_color(Color.WHITE);
//                graphicsView.setEraser();
                changeToEraserMenu = true;
                break;
            }
            case R.id.paintColorMenu: {
                alertDialogShow_color();
                break;
            }
            case R.id.paintSizeMenu: {
                alertDialogShow_size();
                break;
            }
            // eraserGroup
            case R.id.paintMenu: {
                graphicsView.translateData_color(COLORS[defaultItem_color]);
                changeToEraserMenu = false;
                break;
            }
            case R.id.eraserSizeMenu: {
                alertDialogShow_size();
                break;
            }
            // all clear
            case R.id.allClearMenu: {
                graphicsView.setAllClearFlag(true);
                break;
            }
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void alertDialogShow_color() {
        final int[] tempDefaultItem = new int[1];

        new AlertDialog.Builder(this).setTitle("Make your choice")
                .setSingleChoiceItems(strCOLORS, defaultItem_color, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        tempDefaultItem[0] = i;
                    }
                })
                .setPositiveButton("確認", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        defaultItem_color = tempDefaultItem[0];
                        graphicsView.translateData_color(COLORS[defaultItem_color]);
                    }
                })
                .setNegativeButton("取消", null).show();
    }

    public void alertDialogShow_size() {
        final int[] tempDefaultItem = new int[1];

        new AlertDialog.Builder(this).setTitle("Make your choice")
                .setSingleChoiceItems(strSIZES, defaultItem_size, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        tempDefaultItem[0] = i;
                    }
                })
                .setPositiveButton("確認", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        defaultItem_size = tempDefaultItem[0];
                        graphicsView.translateData_size(SIZES[defaultItem_size]);
                    }
                })
                .setNegativeButton("取消", null).show();
    }
}
