package com.panasonic.avc.mms.devicetesttraining;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Looper;
import android.widget.TextView;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by hqian on 16/07/11.
 */
public class WifiTestClass {
    private final static int TIME_OUT = 10000;
    private final static int REAL_TIMEOUT = 1;
    private final static int DEFAULT_TIMEOUT = 2;
    private final static int NON_TIMEOUT = 3;

    private Context mContext;

    private WifiManager wifiManager;
    private TextView testResultView;

    private Intent resultIntent;

    private int ACTIVE_TIME_OUT = DEFAULT_TIMEOUT;


    public WifiTestClass(Context context, WifiManager wifiManager, TextView textView, Intent intent) {
        this.mContext = context;
        this.wifiManager = wifiManager;
        this.testResultView = textView;
        this.resultIntent = intent;
    }


    /*
    TODO: test wifi function of turning on or off
     */
    public void wifiTest_TurnOnOff() {
        publishText("Turn_ON_OFF Test \n ----------------- \n");

        //if present status is at off, turn on and wait, then check
        switch (on_off_string()) {
            case "ON": {
                publishText("present status: ON , try TURN OFF: \n");
                wait_seconds(2);
                if (wifi_turn_off()) { //turn on/off first time --> succeed
                    publishText("Turn OFF: succeed! , test TURN ON: \n");
                    wait_seconds(2);
                    if (wifi_turn_on()) { //turn on/ off second time --> succeed
                        publishText("Turn ON: succeed! \n");
                    } else { //turn on/off second time --> failed
                        publishText("Turn ON: failed! \n");
                        publishText("Turn_On_Off test failed \n");
                        resultIntent.putExtra(mContext.getString(R.string.key_wifiTurnOnOff), false);
                        return;
                    }
                } else { //turn on/off first time --> failed
                    publishText("Turn OFF: failed! \n");
                    publishText("Turn_On_Off test failed \n");
                    resultIntent.putExtra(mContext.getString(R.string.key_wifiTurnOnOff), false);
                    return;
                }
                break;
            }
            case "OFF": {
                publishText("present status: OFF , try TURN ON: \n");
                wait_seconds(2);
                if (wifi_turn_on()) { //turn on/off first time --> succeed
                    publishText("Turn ON: succeed! , test TURN OFF: \n");
                    wait_seconds(2);
                    if (wifi_turn_off()) { //turn on/ off second time --> succeed
                        publishText("Turn OFF: succeed! \n");
                    } else { //turn on/off second time --> failed
                        publishText("Turn OFF: failed! \n");
                        publishText(" *****************\nTurn_On_Off test failed \n ***************** \n\n");
                        resultIntent.putExtra(mContext.getString(R.string.key_wifiTurnOnOff), false);
                        return;
                    }
                } else { //turn on/off first time --> failed
                    publishText("Turn ON: failed! \n");
                    publishText(" *****************\nTurn_On_Off test failed \n ***************** \n\n");
                    resultIntent.putExtra(mContext.getString(R.string.key_wifiTurnOnOff), false);
                    return;
                }
                break;
            }
            default:
                break;
        }

        publishText(" *****************\nTurn_On_Off test succeed \n ***************** \n\n");
        resultIntent.putExtra(mContext.getString(R.string.key_wifiTurnOnOff), true);
    }

    /*
    TODO: turn wifi on and return result
     */
    private boolean wifi_turn_on() {
        if (!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
            //turn on wifi until action works takes some time, so we wait some time
            wait_seconds(3);
            return wifiManager.isWifiEnabled();
        }

        return true;
    }

    /*
    TODO: turn wifi off and return result
     */
    private boolean wifi_turn_off() {
        if (wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(false);
            wait_seconds(3);
            return !wifiManager.isWifiEnabled();
        }

        return true;
    }

    /*
    TODO: whether is on or off, return string result
     */
    private String on_off_string() {
        boolean isWifiEnable = wifiManager.isWifiEnabled();
        return isWifiEnable ? "ON" : "OFF";
    }


    public void wifiTest_accessPoint() {
        publishText("Detect_AccessPoint Test \n ----------------- \n");
        publishText("Scaning......\n");

        //turn on gps or return failed
        if (!wifi_turn_on()) {
            wifi_turn_on();
        }

        wifiManager.startScan();
        List<ScanResult> accessPointList = wifiManager.getScanResults();

        if (accessPointList.size() < 2) {
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (ACTIVE_TIME_OUT == DEFAULT_TIMEOUT) {
                        publishText("TIME OUT\n");
                        wait_seconds(2);
                        publishText(" *****************\nDetect_AccessPoint test failed \n ***************** \n\n");
                        resultIntent.putExtra(mContext.getString(R.string.key_wifiAccessPoint), false);
                        ACTIVE_TIME_OUT = REAL_TIMEOUT;
                    }
                }
            }, TIME_OUT);

            while (accessPointList.size() < 2) {
                accessPointList = wifiManager.getScanResults();
                if (accessPointList.size() > 1) {
                    ACTIVE_TIME_OUT = NON_TIMEOUT;
                    break;
                }
                if (ACTIVE_TIME_OUT == REAL_TIMEOUT) {
                    ACTIVE_TIME_OUT = DEFAULT_TIMEOUT;
                    break;
                }
            }
        }

        if (ACTIVE_TIME_OUT == NON_TIMEOUT) {
            publishText("scan access point: succeed! \n");
            wait_seconds(2);
            publishText("Access Point List (Top 5):\n");
            wait_seconds(2);
            for (int i = 0; i < accessPointList.size(); i++) {
                if (i > 4) {
                    break;
                }
                publishText("BSSID: " + accessPointList.get(i).BSSID +
                        " , Freq: " + accessPointList.get(i).frequency +
                        "MHz , Level: " + accessPointList.get(i).level + "dBm\n");
            }
            wait_seconds(3);
            publishText(" *****************\nDetect_AccessPoint test succeed \n ***************** \n\n");
            resultIntent.putExtra(mContext.getString(R.string.key_wifiAccessPoint), true);
        }

        wait_seconds(3);
        wifi_turn_off();
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
