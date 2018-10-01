package com.myoul.app;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by nick_ on 10/1/2018.
 */

public class Delivery {

    float sLat;
    float sLng;

    float fLat;
    float fLng;

    String info;

    public Delivery(float sLat, float sLng, float fLat, float fLng){
        this.sLat = sLat;
        this.sLng = sLng;
        this.fLat = fLat;
        this.fLng = fLng;
    }

    public void setInfo(String info){
        this.info = info;
    }

    public LatLng getSLatLng(){
        return new LatLng(sLat, sLng);
    }

    public LatLng getFLatLng(){
        return new LatLng(fLat, fLng);
    }
}
