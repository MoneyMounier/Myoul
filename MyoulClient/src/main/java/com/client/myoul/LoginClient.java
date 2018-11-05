package com.client.myoul;

public class LoginClient {

    public static String Login(String user, String pass, String address, int port){
        String result = null;
        result = (String)MyoulClient.query("LoginServer", "authorize", String.format("%s %s %s", MyoulClient.GetAddress("mac"), user, pass), address, port);
        return result;
    }
}
