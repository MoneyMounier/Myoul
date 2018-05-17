package com.client.myoul;

public class LoginClient {

    public static String Login(String user, String pass, String address, int port){
        String result = null;
        result = MyoulClient.query(String.format("login %s %s", user, pass), address, port);
        return result;
    }
}
