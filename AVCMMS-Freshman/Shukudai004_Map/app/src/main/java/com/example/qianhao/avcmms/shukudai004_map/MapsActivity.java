package com.example.qianhao.avcmms.shukudai004_map;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.location.Geocoder;

import com.google.android.gms.identity.intents.Address;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Geocoder mGeocoder;
    String mSearchAddr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Intent intent = getIntent();
        if (intent != null){
            mSearchAddr = intent.getStringExtra("searchPlace");
        }
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mGeocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        try {
            List<android.location.Address> addrlist = mGeocoder.getFromLocationName(mSearchAddr, 1);
            if (addrlist != null && addrlist.size() > 0){
                android.location.Address address = addrlist.get(0);
                // Add a marker in Sydney and move the camera
                LatLng location = new LatLng(address.getLatitude(), address.getLongitude());
                mMap.addMarker(new MarkerOptions().position(location).title(mSearchAddr).icon(BitmapDescriptorFactory.fromResource(R.drawable.pin)));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 10));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
