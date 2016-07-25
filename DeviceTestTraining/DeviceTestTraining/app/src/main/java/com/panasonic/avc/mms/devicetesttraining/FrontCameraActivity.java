package com.panasonic.avc.mms.devicetesttraining;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.camera2.CameraCharacteristics;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;

import java.util.List;

public class FrontCameraActivity extends Activity implements SensorEventListener {
    FragmentManager mFragmentManager;
    Camera2BasicFragment mTexture;
    SensorManager mSensorManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        if (null == savedInstanceState) {
            mFragmentManager = getFragmentManager();
            mFragmentManager.beginTransaction()
                    .replace(R.id.container,
                            Camera2BasicFragment.newInstance(CameraCharacteristics.LENS_FACING_FRONT),
                            Camera2BasicFragment.TAG_FRONT)
                    .commit();
            mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        }
    }

    /* 照度センサー */
    @Override
    protected void onResume() {
        super.onResume();
        List<Sensor> sensors = mSensorManager.getSensorList(Sensor.TYPE_LIGHT);
        if (sensors.size() > 0) {
            Sensor s = sensors.get(0);
            mSensorManager.registerListener(this, s, SensorManager.SENSOR_DELAY_UI);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // TODO Auto-generated method stub
        if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
            mTexture = (Camera2BasicFragment) mFragmentManager.findFragmentByTag(Camera2BasicFragment.TAG_FRONT);
            mTexture.setLuminanceText(event.values[0]);
        }
    }

    /* バックキーのフックとアラート出し */
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        Log.d("KeyCode", String.valueOf(event.getKeyCode()));
        Log.d("KeyCode", String.valueOf(event.getAction()));
        if (event.getAction() == KeyEvent.ACTION_UP) {
            switch (event.getKeyCode()) {
                case KeyEvent.KEYCODE_BACK:
                    showAlertDialog(R.array.front_camera, getResources().getString(R.string.key_front_camera));
                    return true;
                default:
                    return true;
            }
        }
        return true;
    }

    /* アラートダイアログで選択してもらって結果を呼び出し元アクティビティに返す
     * int    r_array:表示したい項目のarrayのリソースID
     * string key    :元のアクティビティに結果を送るときのキー */
    public void showAlertDialog(int r_array, final String key) {
        final boolean[] mCheckBooleans = new boolean[getResources().obtainTypedArray(r_array).length()];
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.dialog_camera);
        builder.setMultiChoiceItems(r_array, null,
                new DialogInterface.OnMultiChoiceClickListener() {
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        Log.d(key + " which", String.valueOf(which));
                        Log.d(key + " isChecked", String.valueOf(isChecked));
                        mCheckBooleans[which] = isChecked;
                    }
                }
        );
        builder.setPositiveButton(R.string.ok,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent result = new Intent();
                        result.putExtra(key, mCheckBooleans);
                        setResult(RESULT_OK, result);
                        Log.d(key + "Value0", String.valueOf(mCheckBooleans[0]));
                        Log.d(key + "Value1", String.valueOf(mCheckBooleans[1]));
                        finish();
                    }
                }
        );
        builder.setNegativeButton(R.string.cancel, null);
        builder.setCancelable(false);
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}