package com.myoul.app.demoapp;

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

    public boolean within(float topLat, float botLat, float topLng, float botLng){
        return ((sLat >= botLat && sLat <= topLat) && (sLng >= botLng && sLng <= topLng)) ||
                ((fLat >= botLat && fLat <= topLat) && (fLng >= botLng && fLng <= topLng));
    }
}
