package com.myoul.app.demoapp;

import android.app.Application;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by mounien on 1/22/19.
 */

public class Global extends Application{

    //TODO save and read from file
    private LinkedList<Delivery> deliveries;
    private LinkedList<Profile> profiles;

    private boolean created = false;

    public Profile User;

    //init the queues
    public void create(){
        if(created)
            return;
        profiles = new LinkedList<>();
        deliveries = new LinkedList<>();
        created = true;

        //temp
        Profile temp = new Profile();
        temp.user = "Username";
        temp.pass = "Password";
        temp.type = Profile.ProfileType.Normal;
        profiles.add(temp);
    }

    //returns a user given a username and password
    public Profile getUser(String user, String pass){
        Profile result = null;
        int len = profiles.size();
        for(int i = 0; i < len; i++){
            Profile temp = profiles.remove();
            System.out.println(temp.user + " " + temp.pass);
            if(temp.user.equals(user) && temp.pass.equals(pass)){
                result = temp;
                User = temp;
            }
            profiles.add(temp);
        }
        return result;
    }

    //adds new user
    public void addUser(Profile profile){
        profiles.add(profile);
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
