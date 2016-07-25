package com.panasonic.avc.mms.devicetesttraining;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity {
    public static final int REQUEST_AUTO_TEST = 0;
    public static final int REQUEST_MULTI_TOUCH_TEST = 1;
    public static final int REQUEST_SINGLE_TOUCH_TEST = 2;
    public static final int REQUEST_FRONT_CAMERA_TEST = 3;
    public static final int REQUEST_REAR_CAMERA_TEST = 4;
    public static final int REQUEST_CAMERA_LIGHT_TEST = 5;

    public static final int TEST_FIRST_ITEM = 0;
    public static final int TEST_SECOND_ITEM = 1;

    private CheckBox mCheckBox_wifi;
    private CheckBox mCheckBox_bluetooth;
    private CheckBox mCheckBox_wwan;
    private CheckBox mCheckBox_gps;

    private ImageView mImageView_wifi;
    private ImageView mImageView_bluetooth;
    private ImageView mImageView_wwan;
    private ImageView mImageView_gps;
    private ImageView mImageView_multi_touch;
    private ImageView mImageView_single_touch;
    private ImageView mImageView_front_camera;
    private ImageView mImageView_rear_camera;
    private ImageView mImageView_camera_light;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mCheckBox_wifi = (CheckBox) findViewById(R.id.wifi_checkBox);
        mCheckBox_bluetooth = (CheckBox) findViewById(R.id.bluetooth_checkBox);
        mCheckBox_wwan = (CheckBox) findViewById(R.id.wwan_checkBox);
        mCheckBox_gps = (CheckBox) findViewById(R.id.gps_checkBox);

        mImageView_wifi = (ImageView) findViewById(R.id.imageView_wifi);
        mImageView_bluetooth = (ImageView) findViewById(R.id.imageView_bluetooth);
        mImageView_wwan = (ImageView) findViewById(R.id.imageView_wwan);
        mImageView_gps = (ImageView) findViewById(R.id.imageView_gps);
        mImageView_multi_touch = (ImageView) findViewById(R.id.imageView_multi_touch);
        mImageView_single_touch = (ImageView) findViewById(R.id.imageView_single_touch);
        mImageView_front_camera = (ImageView) findViewById(R.id.imageView_front_camera);
        mImageView_rear_camera = (ImageView) findViewById(R.id.imageView_rear_camera);
        mImageView_camera_light = (ImageView) findViewById(R.id.imageView_camera_light);
    }

    public void select_wifi(View view) {
        changeCheckBox(mCheckBox_wifi);
    }

    public void select_bluetooth(View view) {
        changeCheckBox(mCheckBox_bluetooth);
    }

    public void select_wwan(View view) {
        changeCheckBox(mCheckBox_wwan);
    }

    public void select_gps(View view) {
        changeCheckBox(mCheckBox_gps);
    }

    private void changeCheckBox(CheckBox checkBox) {
        if (checkBox.isChecked()) {
            checkBox.setChecked(false);
        } else {
            checkBox.setChecked(true);
        }
    }

    public void click_run_button(View view) {
        final boolean wifi_check_value = mCheckBox_wifi.isChecked();
        final boolean bluetooth_check_value = mCheckBox_bluetooth.isChecked();
        final boolean wwan_check_value = mCheckBox_wwan.isChecked();
        final boolean gps_check_value = mCheckBox_gps.isChecked();

        if (wifi_check_value || bluetooth_check_value ||
                wwan_check_value || gps_check_value) {
            // TODO: AutoTestActivity結合時に有効化
            Intent intent_auto = new Intent(this, AutoTest.class);
            intent_auto.putExtra(getString(R.string.key_wifiCheckValue), wifi_check_value);
            intent_auto.putExtra(getString(R.string.key_bluetoothCheckValue), bluetooth_check_value);
            intent_auto.putExtra(getString(R.string.key_wwanCheckValue), wwan_check_value);
            intent_auto.putExtra(getString(R.string.key_gpsCheckValue), gps_check_value);

            startActivityForResult(intent_auto, REQUEST_AUTO_TEST);

        } else {
            showToast(R.string.error_no_check);
        }
    }

    public void select_multiTouch(View view) {
        /* TODO: マルチタッチテスト結合時に有効化
        Intent intent_multiTouch = new Intent(this, MultiTouchActivity.class);
        startActivityForResult(intent_multiTouch, REQUEST_MULTI_TOUCH_TEST);
        */
    }

    public void select_singleTouch(View view) {
        /* TODO: シングルタッチテスト結合時に有効化
        Intent intent_singleTouch = new Intent(this, SingleTouchActivity.class);
        startActivityForResult(intent_singleTouch, REQUEST_SINGLE_TOUCH_TEST);
        */
    }

    public void select_frontCamera(View view) {
        Intent intent_frontCamera = new Intent(this, FrontCameraActivity.class);
        startActivityForResult(intent_frontCamera, REQUEST_FRONT_CAMERA_TEST);
    }

    public void select_rearCamera(View view) {
        Intent intent_rearCamera = new Intent(this, RearCameraActivity.class);
        startActivityForResult(intent_rearCamera, REQUEST_REAR_CAMERA_TEST);
    }

    public void select_cameraLight(View view) {
        /* TODO: カメラライトテスト結合時に有効化
        Intent intent_cameraLight = new Intent(this, CameraLightActivity.class);
        startActivityForResult(intent_cameraLight, REQUEST_CAMERA_LIGHT_TEST);
        */
    }

    private void changeTextView(boolean isPass, TextView textView) {
        if (isPass) {
            textView.setText(R.string.passed);
        } else {
            textView.setText(R.string.failed);
        }
    }

    private void changeImageView(boolean isPass, ImageView imageView) {
        if (isPass) {
            // TODO:適切な画像に差し替える
            imageView.setImageResource(android.R.drawable.presence_online);
        } else {
            // TODO:適切な画像に差し替える
            imageView.setImageResource(android.R.drawable.presence_busy);
        }
        imageView.setVisibility(View.VISIBLE);
    }

    private boolean checkAllPass(boolean... isAllPass) {
        if (isAllPass == null) {
            return false;
        }
        boolean result = true;
        for (boolean bool : isAllPass) {
            result &= bool;
        }
        return result;
    }

    private void showToast(int resource_id) {
        Toast.makeText(this, resource_id, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent getTestValue) {
        super.onActivityResult(requestCode, resultCode, getTestValue);

        switch (requestCode) {
            case REQUEST_AUTO_TEST:
                if (resultCode == RESULT_OK) {
                    if (mCheckBox_wifi.isChecked()) {
                        boolean wifi_turnOnOff = getTestValue.getBooleanExtra(getString(R.string.key_wifiTurnOnOff), false);
                        boolean wifi_accessPoint = getTestValue.getBooleanExtra(getString(R.string.key_wifiAccessPoint), false);

                        changeTextView(wifi_turnOnOff, (TextView) findViewById(R.id.textView_wifi_turnOnOff));
                        changeTextView(wifi_accessPoint, (TextView) findViewById(R.id.textView_wifi_detect));

                        changeImageView(checkAllPass(wifi_turnOnOff, wifi_accessPoint), mImageView_wifi);
                    }

                    if (mCheckBox_bluetooth.isChecked()) {
                        boolean bluetooth_turnOnOff = getTestValue.getBooleanExtra(getString(R.string.key_bluetoothTurnOnOff), false);
                        boolean bluetooth_nearByDevice = getTestValue.getBooleanExtra(getString(R.string.key_bluetoothNearbyDevice), false);

                        changeTextView(bluetooth_turnOnOff, (TextView) findViewById(R.id.textView_bluetooth_turnOnOff));
                        changeTextView(bluetooth_nearByDevice, (TextView) findViewById(R.id.textView_bluetooth_detect));

                        changeImageView(checkAllPass(bluetooth_turnOnOff, bluetooth_nearByDevice), mImageView_bluetooth);
                    }

                    if (mCheckBox_wwan.isChecked()) {
                        boolean wwan_turnOnOff = getTestValue.getBooleanExtra(getString(R.string.key_wwanTurnOnOff), false);
                        boolean wwan_SIM = getTestValue.getBooleanExtra(getString(R.string.key_wwanSIM), false);
                        boolean wwan_network = getTestValue.getBooleanExtra(getString(R.string.key_wwanNetwork), false);

                        changeTextView(wwan_turnOnOff, (TextView) findViewById(R.id.textView_wwan_turnOnOff));
                        changeTextView(wwan_SIM, (TextView) findViewById(R.id.textView_wwan_SIM));
                        changeTextView(wwan_network, (TextView) findViewById(R.id.textView_wwan_network));

                        changeImageView(checkAllPass(wwan_turnOnOff, wwan_SIM, wwan_network), mImageView_wwan);
                    }

                    if (mCheckBox_gps.isChecked()) {
                        boolean gps_turnOnOff = getTestValue.getBooleanExtra(getString(R.string.key_gpsTurnOnOff), false);
                        boolean gps_detect = getTestValue.getBooleanExtra(getString(R.string.key_gpsDetect), false);

                        changeTextView(gps_turnOnOff, (TextView) findViewById(R.id.textView_gps_turnOnOff));
                        changeTextView(gps_detect, (TextView) findViewById(R.id.textView_gps_detect));

                        changeImageView(checkAllPass(gps_turnOnOff, gps_detect), mImageView_gps);
                    }
                } else {
                    showToast(R.string.cancelled);
                }
                break;
            case REQUEST_MULTI_TOUCH_TEST:
                if (resultCode == RESULT_OK) {
                    boolean[] multiTouchBooleanArray = getTestValue.getBooleanArrayExtra(getString(R.string.key_multi_touch));

                    changeTextView(multiTouchBooleanArray[TEST_FIRST_ITEM], (TextView) findViewById(R.id.textView_multi_touch));

                    changeImageView(checkAllPass(multiTouchBooleanArray), mImageView_multi_touch);
                } else {
                    showToast(R.string.cancelled);
                }
                break;
            case REQUEST_SINGLE_TOUCH_TEST:
                if (resultCode == RESULT_OK) {

                    boolean[] singleTouchBooleanArray = getTestValue.getBooleanArrayExtra(getString(R.string.key_single_touch));

                    changeTextView(singleTouchBooleanArray[TEST_FIRST_ITEM], (TextView) findViewById(R.id.textView_single_touch));

                    changeImageView(checkAllPass(singleTouchBooleanArray), mImageView_single_touch);
                } else {
                    showToast(R.string.cancelled);
                }
                break;
            case REQUEST_FRONT_CAMERA_TEST:
                if (resultCode == RESULT_OK) {
                    boolean[] frontCameraBooleanArray = getTestValue.getBooleanArrayExtra(getString(R.string.key_front_camera));

                    changeTextView(frontCameraBooleanArray[TEST_FIRST_ITEM], (TextView) findViewById(R.id.textView_front_camera_display));
                    changeTextView(frontCameraBooleanArray[TEST_SECOND_ITEM], (TextView) findViewById(R.id.textView_front_light_sensor));

                    changeImageView(checkAllPass(frontCameraBooleanArray), mImageView_front_camera);
                } else {
                    showToast(R.string.cancelled);
                }
                break;
            case REQUEST_REAR_CAMERA_TEST:
                if (resultCode == RESULT_OK) {
                    boolean[] rearCameraBooleanArray = getTestValue.getBooleanArrayExtra(getString(R.string.key_rear_camera));

                    changeTextView(rearCameraBooleanArray[TEST_FIRST_ITEM], (TextView) findViewById(R.id.textView_rear_camera_display));

                    changeImageView(checkAllPass(rearCameraBooleanArray), mImageView_rear_camera);
                } else {
                    showToast(R.string.cancelled);
                }
                break;
            case REQUEST_CAMERA_LIGHT_TEST:
                if (resultCode == RESULT_OK) {
                    boolean[] cameraLightBooleanArray = getTestValue.getBooleanArrayExtra(getString(R.string.key_camera_light));

                    changeTextView(cameraLightBooleanArray[TEST_FIRST_ITEM], (TextView) findViewById(R.id.textView_rear_camera_display));

                    changeImageView(checkAllPass(cameraLightBooleanArray), mImageView_camera_light);
                } else {
                    showToast(R.string.cancelled);
                }
                break;
            default:
                break;
        }
    }
}


