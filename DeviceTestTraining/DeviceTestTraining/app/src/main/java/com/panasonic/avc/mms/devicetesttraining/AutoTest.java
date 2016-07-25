package com.panasonic.avc.mms.devicetesttraining;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class AutoTest extends Activity {

    private boolean wifi_check_value;
    private boolean bluetooth_check_value;
    private boolean wwan_check_value;
    private boolean gps_check_value;

    private WifiManager wifiManager;
    private LocationManager locationManager;
    private BluetoothAdapter bluetoothAdapter;

    private TextView testResultView;
    private Intent resultIntent;
    private ArrayList<Method> methodArrayList;

    private int CHECK_TEST_CNT = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_test);

        //received intent
        Intent intent = getIntent();
        wifi_check_value = intent.getBooleanExtra(getString(R.string.key_wifiCheckValue), false);
        bluetooth_check_value = intent.getBooleanExtra(getString(R.string.key_bluetoothCheckValue), false);
        wwan_check_value = intent.getBooleanExtra(getString(R.string.key_wwanCheckValue), false);
        gps_check_value = intent.getBooleanExtra(getString(R.string.key_gpsCheckValue), false);

        //all kinds of managers
        wifiManager = (WifiManager)getSystemService(Context.WIFI_SERVICE);
        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        //parameters init
        testResultView = (TextView) findViewById(R.id.testResult);
            //by attribute of gravity="bottom",
            //it will scroll and always focus on latest line of text which is added
        testResultView.setMovementMethod(ScrollingMovementMethod.getInstance());
        resultIntent = new Intent();
        methodArrayList = new ArrayList<Method>();

        //choose test items
        add_test_to_array();

        //begin testing
        doTesting();
    }

    public void doTesting() {
        //show text
        //wifi
        TextView wifiTestCheckView = (TextView) findViewById(R.id.wifiTestCheckValue);
        wifiTestCheckView.setText(wifi_check_value ? "Run" : "Ignore");
        //bluetooth
        TextView bluetoothTestCheckView = (TextView) findViewById(R.id.bluetoothTestCheckValue);
        bluetoothTestCheckView.setText(bluetooth_check_value ? "Run" : "Ignore");
        //wwan
        TextView wwanTestCheckView = (TextView) findViewById(R.id.wwanTestCheckValue);
        wwanTestCheckView.setText(wwan_check_value ? "Run" : "Ignore");
        //gps
        TextView gpsTestCheckView = (TextView) findViewById(R.id.gpsTestCheckValue);
        gpsTestCheckView.setText(gps_check_value ? "Run" : "Ignore");


        //do test
        doMethodInOtherThread(AutoTest.this, methodArrayList);

    }

    public void wifiTesting() {
        wait_seconds(2);
        publishText("WIFI TEST START: \n\n");

        WifiTestClass wifiTestClass = new WifiTestClass(this, wifiManager, testResultView, resultIntent);
        wifiTestClass.wifiTest_TurnOnOff();
        wifiTestClass.wifiTest_accessPoint();

        check_finish();
    }

    public void bluetoothTesting() {
        wait_seconds(2);
        publishText("BLUETOOTH TEST START: \n\n");

        BluetoothTestClass bluetoothTestClass = new BluetoothTestClass(this, bluetoothAdapter, testResultView, resultIntent);
        bluetoothTestClass.bluetoothTest_TurnOnOff();
        bluetoothTestClass.bluetoothTest_detect();

        check_finish();
    }

    public void wwanTesting() {
        publishText("wwan module\n");

        check_finish();
    }

    public void gpsTesting() {
        wait_seconds(2);
        publishText("GPS TEST START: \n\n");

        GpsTestClass gpsTestClass = new GpsTestClass(this, locationManager, testResultView, resultIntent);
        gpsTestClass.gpsTest_TurnOnOff();
        gpsTestClass.gpsTest_satellite();

        check_finish();
    }



    /*
    TODO: add each test function to array
     */
    private void add_test_to_array() {
        try {
            if (wifi_check_value) {
                methodArrayList.add(AutoTest.class.getMethod("wifiTesting"));
                CHECK_TEST_CNT++;
            }
            if (bluetooth_check_value) {
                methodArrayList.add(AutoTest.class.getMethod("bluetoothTesting"));
                CHECK_TEST_CNT++;
            }
            if (wwan_check_value) {
                methodArrayList.add(AutoTest.class.getMethod("wwanTesting"));
                CHECK_TEST_CNT++;
            }
            if (gps_check_value) {
                methodArrayList.add(AutoTest.class.getMethod("gpsTesting"));
                CHECK_TEST_CNT++;
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    /*
    TODO: do each test in new thread
     */
    private void doMethodInOtherThread(final Object object, final ArrayList<Method> arrayList) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                int arrayListIndex = 0;
                while (arrayListIndex < arrayList.size()) {
                    try {
                        CHECK_TEST_CNT--;
                        arrayList.get(arrayListIndex).invoke(object);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }

                    arrayListIndex++;
                }
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }

    /*
    TODO: do return work when all test are finished
     */
    private void check_finish() {
        if (CHECK_TEST_CNT == 0) {
            setResult(RESULT_OK, resultIntent);
            finish();
        }
    }

    /*
    TODO: wait for seconds
     */
    private void wait_seconds(float sec) {
        try {
            Thread.sleep((long) (sec * 1000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /*
    TODO: publish text into testResultView
     */
    private void publishText(final String text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                testResultView.append(text);
            }
        });
        wait_seconds(0.5f);
    }
}
