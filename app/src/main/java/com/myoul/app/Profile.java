package com.myoul.app;

/**
 * Created by mounien on 1/22/19.
 */

public class Profile {

    public String user;
    public String pass;
    public ProfileType type;

    public enum ProfileType{
        Driver,
        Normal
    }
}
