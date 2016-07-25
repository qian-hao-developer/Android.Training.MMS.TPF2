package com.panasonic.avc.mms.devicetesttraining;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by hqian on 7/12/16.
 */
public class BluetoothTestClass {
    private final static int TIME_OUT = 10000;
    private final static int REAL_TIMEOUT = 1;
    private final static int DEFAULT_TIMEOUT = 2;
    private final static int NON_TIMEOUT = 3;

    private Context mContext;

    private BluetoothAdapter bluetoothAdapter;
    private TextView testResultView;
    private BroadcastReceiver broadcastReceiver;

    private Intent resultIntent;

    private int ACTIVE_TIME_OUT = DEFAULT_TIMEOUT;


    public BluetoothTestClass(Context context, BluetoothAdapter bluetoothAdapter, TextView textView, Intent intent) {
        this.mContext = context;
        this.bluetoothAdapter = bluetoothAdapter;
        this.testResultView = textView;
        this.resultIntent = intent;
    }


    /*
    TODO: test wifi function of turning on or off
     */
    public void bluetoothTest_TurnOnOff() {
        publishText("Turn_ON_OFF Test \n ----------------- \n");

        //if present status is at off, turn on and wait, then check
        switch (on_off_string()) {
            case "ON": {
                publishText("present status: ON , try TURN OFF: \n");
                wait_seconds(2);
                if (bluetooth_turn_off()) { //turn on/off first time --> succeed
                    publishText("Turn OFF: succeed! , test TURN ON: \n");
                    wait_seconds(2);
                    if (bluetooth_turn_on()) { //turn on/ off second time --> succeed
                        publishText("Turn ON: succeed! \n");
                    } else { //turn on/off second time --> failed
                        publishText("Turn ON: failed! \n");
                        publishText("Turn_On_Off test failed \n");
                        resultIntent.putExtra(mContext.getString(R.string.key_bluetoothTurnOnOff), false);
                        return;
                    }
                } else { //turn on/off first time --> failed
                    publishText("Turn OFF: failed! \n");
                    publishText("Turn_On_Off test failed \n");
                    resultIntent.putExtra(mContext.getString(R.string.key_bluetoothTurnOnOff), false);
                    return;
                }
                break;
            }
            case "OFF": {
                publishText("present status: OFF , try TURN ON: \n");
                wait_seconds(2);
                if (bluetooth_turn_on()) { //turn on/off first time --> succeed
                    publishText("Turn ON: succeed! , test TURN OFF: \n");
                    wait_seconds(2);
                    if (bluetooth_turn_off()) { //turn on/ off second time --> succeed
                        publishText("Turn OFF: succeed! \n");
                    } else { //turn on/off second time --> failed
                        publishText("Turn OFF: failed! \n");
                        publishText(" *****************\nTurn_On_Off test failed \n ***************** \n\n");
                        resultIntent.putExtra(mContext.getString(R.string.key_bluetoothTurnOnOff), false);
                        return;
                    }
                } else { //turn on/off first time --> failed
                    publishText("Turn ON: failed! \n");
                    publishText(" *****************\nTurn_On_Off test failed \n ***************** \n\n");
                    resultIntent.putExtra(mContext.getString(R.string.key_bluetoothTurnOnOff), false);
                    return;
                }
                break;
            }
            default:
                break;
        }

        publishText(" *****************\nTurn_On_Off test succeed \n ***************** \n\n");
        resultIntent.putExtra(mContext.getString(R.string.key_bluetoothTurnOnOff), true);
    }

    /*
    TODO: turn wifi on and return result
     */
    private boolean bluetooth_turn_on() {
        if (!bluetoothAdapter.isEnabled()) {
            bluetoothAdapter.enable();
            wait_seconds(3);
            return bluetoothAdapter.isEnabled();
        }

        return true;
    }

    /*
    TODO: turn wifi off and return result
     */
    private boolean bluetooth_turn_off() {
        if (bluetoothAdapter.isEnabled()) {
            bluetoothAdapter.disable();
            wait_seconds(3);
            return !bluetoothAdapter.isEnabled();
        }

        return true;
    }

    /*
    TODO: whether is on or off, return string result
     */
    private String on_off_string() {
        boolean isBluetoothEnable = bluetoothAdapter.isEnabled();
        return isBluetoothEnable ? "ON" : "OFF";
    }


    /*
    TODO: bluetooth detection test
     */
    public void bluetoothTest_detect() {
        publishText("Detect_Nearby_Devices Test \n ----------------- \n");
        publishText("Scaning......\n");

        //turn on gps or return failed
        if (!bluetooth_turn_on()) {
            bluetooth_turn_on();
        }

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //if have scanned any device, show info
                if (BluetoothDevice.ACTION_FOUND.equals(intent.getAction())) {
                    if (ACTIVE_TIME_OUT == DEFAULT_TIMEOUT) {
                        ACTIVE_TIME_OUT = NON_TIMEOUT;
                        publishText("scan bluetooth devices: succeed! \n");
                        wait_seconds(1);
                        publishText("Nearby devices (samples):\n");
                        wait_seconds(1);
                    }
                    if (ACTIVE_TIME_OUT != REAL_TIMEOUT) {
                        BluetoothDevice bluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                        publishText("Device: " + bluetoothDevice.getName() + " , Address: " + bluetoothDevice.getAddress() + "\n");
                    }
                } else {
                    //testing if registerReceiver by intentFilter works
                    Log.e("receive intent: ", intent.getAction());
                }
            }
        };
        IntentFilter intentFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        mContext.registerReceiver(broadcastReceiver, intentFilter);
        bluetoothAdapter.startDiscovery();

        //start timer
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (bluetoothAdapter.isDiscovering()) {
                    bluetoothAdapter.cancelDiscovery();
                }
                mContext.unregisterReceiver(broadcastReceiver);
                if (ACTIVE_TIME_OUT == DEFAULT_TIMEOUT) {
                    publishText("TIME OUT\n");
                    wait_seconds(2);
                    publishText(" *****************\nDetect_Nearby_Devices test failed \n ***************** \n\n");
                    resultIntent.putExtra(mContext.getString(R.string.key_bluetoothNearbyDevice), false);
                    ACTIVE_TIME_OUT = REAL_TIMEOUT;
                } else if (ACTIVE_TIME_OUT == NON_TIMEOUT) {
                    wait_seconds(2);
                    publishText(" *****************\nDetect_Nearby_Devices test succeed \n ***************** \n\n");
                    resultIntent.putExtra(mContext.getString(R.string.key_bluetoothNearbyDevice), true);
                    ACTIVE_TIME_OUT = REAL_TIMEOUT;
                }
                //attention: ACTIVE_TIME_OUT is not done init
            }
        }, TIME_OUT);

        while (ACTIVE_TIME_OUT != REAL_TIMEOUT) {
            //do nothing
        }
        //init ACTIVE_TIME_OUT
        ACTIVE_TIME_OUT = DEFAULT_TIMEOUT;

        wait_seconds(3);
        bluetooth_turn_off();
    }


    /*
    ******************* generation functions **********************************:
     */

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
        //equal: runOnUiThread
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                testResultView.append(text);
            }
        });

        wait_seconds(0.5f);
    }
}
