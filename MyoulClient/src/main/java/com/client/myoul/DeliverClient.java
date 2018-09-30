package com.client.myoul;

/**
 * Created by nick_ on 9/30/2018.
 */

public class DeliverClient {

    public static String deliver(float lat1, float long1, float lat2, float long2, String address, int port){
        String result = null;
        result = MyoulClient.query(String.format("deliver %f %f %f %f", lat1, long1, lat2, long2), address, port);
        return result;
    }
}
