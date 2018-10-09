package com.client.myoul;

/**
 * Created by mounien on 10/8/18.
 */

public class ProfileClient {

    public static String all(String address, int port){
        String result = null;
        //server needs to know which profile to return
        result = MyoulClient.query("profile all", address, port);
        return result;
    }
}
