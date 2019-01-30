package com.myoul.app.demoapp;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.myoul.app.R;

public class DeliveryActivity extends FragmentActivity implements OnMapReadyCallback {

    public static final String LEAD = "com.myoul.app";
    Global global;

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deliver_1);
        global = (Global)this.getApplication();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney, Australia, and move the camera.
        LatLng bell = new LatLng(48.7519, -122.4787);
        //mMap.addMarker(new MarkerOptions().position(bell).title("Marker in Bellingham"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(bell, 10));
    }
}
