package com.myoul.app;

import android.app.Application;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by mounien on 1/22/19.
 */

public class Global extends Application{

    private Queue<Delivery> deliveries;
    private Queue<Profile> profiles;

    //init the queues
    public void create(){
        profiles = new LinkedList<>();
        deliveries = new LinkedList<>();
    }

    //returns a user given a username and password
    public Profile getUser(String user, String pass){
        Profile result = null;
        int len = profiles.size();
        for(int i = 0; i < len; i++){
            Profile temp = profiles.remove();
            if(temp.user.equals(user) && temp.pass.equals(pass)){
                result = temp;
            }
            profiles.add(temp);
        }
        return result;
    }

    //returns an array of deliveries within the provided parameters
    public Delivery[] getDeliveries(float topLat, float botLat, float topLng, float botLng){
        Queue<Delivery> results = new LinkedList<>();
        int len = deliveries.size();
        for(int i = 0; i < len; i++){
            Delivery temp = deliveries.remove();
            if(temp.within(topLat, botLat, topLng, botLng)){
                results.add(temp);
            }
            deliveries.add(temp);
        }
        return (Delivery[])results.toArray();
    }
}
