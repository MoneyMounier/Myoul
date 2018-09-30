package com.client.myoul;

/**
 * Created by nick_ on 9/30/2018.
 */

public class SendClient {

    public static String send(float lat, float lon, String address, int port){
        String result = null;
        result = MyoulClient.query(String.format("send %f %f", lat, lon), address, port);
        return result;
    }
}
