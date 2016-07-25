package com.panasonic.avc.mms.devicetesttraining;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by hqian on 16/07/11.
 */
public class GpsTestClass {
    private final static int TIME_OUT = 10000;

    private Context mContext;

    private LocationManager locationManager;
    private TextView testResultView;
    private GpsStatus.Listener gpsStatusListener;
    private LocationListener locationListener;
    private Looper mLooper;

    private Intent resultIntent;
//    private Handler handler;
//    private Runnable addListenerRunnable;

    private boolean ACTIVE_TIME_OUT = true;


    public GpsTestClass(Context context, LocationManager locationManager, TextView textView, Intent intent) {
        this.mContext = context;
        this.locationManager = locationManager;
        this.testResultView = textView;
        this.resultIntent = intent;
    }


    /*
    TODO: test gps function of turning on or off
     */
    public void gpsTest_TurnOnOff() {
        publishText("Turn_ON_OFF Test \n ----------------- \n");

        //if present status is at off, turn on and wait, then check
        switch (on_off_string()) {
            case "ON": {
                publishText("present status: ON , try TURN OFF: \n");
                wait_seconds(2);
                if (gps_turn_off()) { //turn on/off first time --> succeed
                    publishText("Turn OFF: succeed! , test TURN ON: \n");
                    wait_seconds(2);
                    if (gps_turn_on()) { //turn on/ off second time --> succeed
                        publishText("Turn ON: succeed! \n");
                    } else { //turn on/off second time --> failed
                        publishText("Turn ON: failed! \n");
                        publishText("Turn_On_Off test failed \n");
                        resultIntent.putExtra(mContext.getString(R.string.key_gpsTurnOnOff), false);
                        return;
                    }
                } else { //turn on/off first time --> failed
                    publishText("Turn OFF: failed! \n");
                    publishText("Turn_On_Off test failed \n");
                    resultIntent.putExtra(mContext.getString(R.string.key_gpsTurnOnOff), false);
                    return;
                }
                break;
            }
            case "OFF": {
                publishText("present status: OFF , try TURN ON: \n");
                wait_seconds(2);
                if (gps_turn_on()) { //turn on/off first time --> succeed
                    publishText("Turn ON: succeed! , test TURN OFF: \n");
                    wait_seconds(2);
                    if (gps_turn_off()) { //turn on/ off second time --> succeed
                        publishText("Turn OFF: succeed! \n");
                    } else { //turn on/off second time --> failed
                        publishText("Turn OFF: failed! \n");
                        publishText(" *****************\nTurn_On_Off test failed \n ***************** \n\n");
                        resultIntent.putExtra(mContext.getString(R.string.key_gpsTurnOnOff), false);
                        return;
                    }
                } else { //turn on/off first time --> failed
                    publishText("Turn ON: failed! \n");
                    publishText(" *****************\nTurn_On_Off test failed \n ***************** \n\n");
                    resultIntent.putExtra(mContext.getString(R.string.key_gpsTurnOnOff), false);
                    return;
                }
                break;
            }
            default:
                break;
        }

        publishText(" *****************\nTurn_On_Off test succeed \n ***************** \n\n");
        resultIntent.putExtra(mContext.getString(R.string.key_gpsTurnOnOff), true);
    }

    /*
    TODO: turn gps on and return result
     */
    private boolean gps_turn_on() {
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            ContentResolver resolver = mContext.getContentResolver();
            Settings.Secure.putInt(resolver, Settings.Secure.LOCATION_MODE, Settings.Secure.LOCATION_MODE_SENSORS_ONLY);
            return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        }

        return true;
    }

    /*
    TODO: turn gps off and return result
     */
    private boolean gps_turn_off() {
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            ContentResolver resolver = mContext.getContentResolver();
            Settings.Secure.putInt(resolver, Settings.Secure.LOCATION_MODE, Settings.Secure.LOCATION_MODE_OFF);
            return !locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        }

        return true;
    }

    /*
    TODO: whether is on or off, return string result
     */
    private String on_off_string() {
        boolean isGpsEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return isGpsEnable ? "ON" : "OFF";
    }

    /*
    TODO: test gps function of detecting satellite
     */
    public void gpsTest_satellite() {
        publishText("Detect_Satellite Test \n ----------------- \n");
        publishText("Detecting......\n");

        //turn on gps or return failed
        if (!gps_turn_on()) {
            publishText(" *****************\nDetect_Satellite test failed \n ***************** \n\n");
            resultIntent.putExtra(mContext.getString(R.string.key_gpsDetect), false);
            return;
        }

        init_GpsTest_Listeners();

        //add gpsStatusListener and requestLocationUpdate
        Looper.prepare();
//        handler = new Handler();
//        addListenerRunnable = new Runnable() {
//            @Override
//            public void run() {
////                locationManager.addGpsStatusListener(gpsStatusListener);
////                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0.5f, locationListener);
//            }
//        };
//        handler.post(addListenerRunnable);

        locationManager.addGpsStatusListener(gpsStatusListener);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0.5f, locationListener);

        mLooper = Looper.myLooper();
        //count down
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (ACTIVE_TIME_OUT) {
                    publishText("...\ndetecting time out!\n");
                    publishText(" *****************\nDetect_Satellite test failed \n ***************** \n\n");
                    resultIntent.putExtra(mContext.getString(R.string.key_gpsDetect), false);

                    wait_seconds(3);

                    locationManager.removeUpdates(locationListener);
                    locationManager.removeGpsStatusListener(gpsStatusListener);

                    mLooper.quit();
                }
            }
        }, TIME_OUT);

        Looper.loop();

        /*************************  Learning from Looper  ********************************

        code between Looper.prepare() & Looper.loop() will only be called one time
        if you add some listener between Looper(prepare&loop), it will go into listener's own loop
        by using the Looper.myLooper (the same looper)
        if we want to jump out the looper, <1.> do quit inside the callback function of listener
        or <2.> just between the Looper(prepare&loop)
          --> if choose <2.>
          make sure you do quit it if you use a if-construction, because it will only be called once

         *********************************************************************************/

    }

    /*
    TODO: define gpsStatusListener & LocationListener
     */
    private void init_GpsTest_Listeners() {
        // init gpsStatusListener
        gpsStatusListener = new GpsStatus.Listener() {
            @Override
            public void onGpsStatusChanged(int i) {
                gpsStatus_changed_action(i);
            }
        };

        // init locationListener
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                //do nothing
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {
                //do nothing
            }

            @Override
            public void onProviderEnabled(String s) {
                //do nothing
            }

            @Override
            public void onProviderDisabled(String s) {
                //do nothing
            }
        };
    }

    /*
    TODO: define what we do when gps' status changed
     */
    private void gpsStatus_changed_action(int event) {
        switch (event) {
            case GpsStatus.GPS_EVENT_FIRST_FIX: {
                publishText("gps first location ... \n");
                break;
            }
            case GpsStatus.GPS_EVENT_SATELLITE_STATUS: {
                detect_Satellite();
                break;
            }
            case GpsStatus.GPS_EVENT_STARTED: {
                publishText("gps actived\n");
                break;
            }
            case GpsStatus.GPS_EVENT_STOPPED: {
                publishText("gps stoped\n");
                break;
            }
            default:
                break;
        }
    }

    /*
    TODO: testing detection with the satellite
     */
    private void detect_Satellite() {
        //negative the time out flag
        ACTIVE_TIME_OUT = false;

        //detect all satellites that we can
        GpsStatus gpsStatus = locationManager.getGpsStatus(null);
        int maxSatellites = gpsStatus.getMaxSatellites();
        Iterable<GpsSatellite> iterable = gpsStatus.getSatellites();
        Iterator<GpsSatellite> iterator = iterable.iterator();
        ArrayList<GpsSatellite> satellitesList = new ArrayList<GpsSatellite>();
        int count = 0;
        while (iterator.hasNext() && count <= maxSatellites) {
            GpsSatellite satellite = iterator.next();
            satellitesList.add(satellite);
            count++;
            if (count >= 6) { //for time saving , we only take top 5 satellites
                break;
            }
        }
        //get result
        if (maxSatellites > 0) {
            publishText("search satellite: succeed! \n");
            wait_seconds(2);
            publishText("top satellite info:\n");
            wait_seconds(2);
            showSatelliteInfo(satellitesList.get(0));
            wait_seconds(3);
            publishText(" *****************\nDetect_Satellite test succeed \n ***************** \n\n");
            resultIntent.putExtra(mContext.getString(R.string.key_gpsDetect), true);
        } else {
            publishText(" *****************\nDetect_Satellite test failed \n ***************** \n\n");
            resultIntent.putExtra(mContext.getString(R.string.key_gpsDetect), false);
        }

        wait_seconds(3);

        locationManager.removeUpdates(locationListener);
        locationManager.removeGpsStatusListener(gpsStatusListener);
        gps_turn_off();

        //Important!! if don't do this, we will never quit from Looper.loop()
        Looper.myLooper().quit();
    }

    /*
    TODO: show one satellite information
     */
    private void showSatelliteInfo(GpsSatellite satellite) {
        publishText("Azimuth of satellite:  ");
        publishText(Float.toString(satellite.getAzimuth()) + "\n");

        publishText("Elevation of satellite:  ");
        publishText(Float.toString(satellite.getElevation()) + "\n");

        publishText("PRN of satellite:  ");
        publishText(Integer.toString(satellite.getPrn()) + "\n");

        publishText("SNR of satellite:  ");
        publishText(Float.toString(satellite.getSnr()) + "\n");

        publishText("Has Almanac ? :  ");
        publishText(Boolean.toString(satellite.hasAlmanac()) + "\n");

        publishText("Has Ephemeris ? :  ");
        publishText(Boolean.toString(satellite.hasEphemeris()) + "\n");
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
