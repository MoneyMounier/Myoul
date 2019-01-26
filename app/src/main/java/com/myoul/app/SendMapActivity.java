package com.myoul.app;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Toast;

import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;


import java.util.ArrayList;
import java.util.List;

public class SendMapActivity extends FragmentActivity implements OnMapReadyCallback {
    public static final String LEAD = "com.myoul.app";
    Global global;

    private GoogleMap mMap;

    private LatLng origin;
    private LatLng dest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_4);
        global = (Global) this.getApplication();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //set camera to look at bellingham
        LatLng bell = new LatLng(48.7519, -122.4787);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(bell, 10));

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

                //set points
                if (dest != null) {
                    mMap.clear();
                    origin = latLng;
                    dest = null;
                } else if (origin == null) {
                    origin = latLng;
                } else {
                    dest = latLng;
                }

                // Creating MarkerOptions
                MarkerOptions options = new MarkerOptions();

                // Setting the options of the marker
                options.position(latLng);
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.map_pin);
                BitmapDescriptor desc = BitmapDescriptorFactory.fromBitmap(Bitmap.createScaledBitmap(bitmap, 50, 50, false));
                options.icon(desc);

                // Add new marker to the Google Map Android API V2
                mMap.addMarker(options);

                // Checks, whether start and end locations are captured
                if (origin != null && dest != null) {
                    Routing routing = new Routing.Builder()
                            .travelMode(Routing.TravelMode.DRIVING)
                            .withListener(new RoutingListener() {
                                @Override
                                public void onRoutingFailure(RouteException e) {
                                    System.out.println("failure");
                                }

                                @Override
                                public void onRoutingStart() {
                                    System.out.println("start");
                                }

                                @Override
                                public void onRoutingSuccess(ArrayList<Route> route, int shortestRouteIndex) {

                                    ArrayList<Polyline> polylines = new ArrayList<>();
                                    //add route(s) to the map.
                                    for (int i = 0; i < route.size(); i++) {
                                        PolylineOptions polyOptions = new PolylineOptions();
                                        polyOptions.addAll(route.get(i).getPoints());
                                        Polyline polyline = mMap.addPolyline(polyOptions);
                                        polylines.add(polyline);
                                    }
                                }

                                @Override
                                public void onRoutingCancelled() {
                                    System.out.println("cancelled");
                                }
                            })
                            .waypoints(origin, dest)
                            .build();
                    routing.execute();
                }
            }
        });
    }

    public void nextSM(View view){
        Intent intent = new Intent(this, SendActivity.class);
        intent.putExtra("current", 5);
        startActivity(intent);
    }
    public void prevSM(View view){
        Intent intent = new Intent(this, SendActivity.class);
        intent.putExtra("current", 3);
        startActivity(intent);
    }
}
