package com.client.myoul;

import com.server.myoul.Message;

public class LoginClient {

    public static boolean Login(MyoulClient client, String user, String pass){
        Message message = new Message("LoginServer", "authorize", user, pass);
        message.key = client.keyPair.getPublic().getEncoded();
        return client.send(message);
    }

    public static String checkReply(MyoulClient client){
        Object obj = client.recieve();
        if(obj == null)
            return null;
        else if(obj.getClass() == String.class)
            return (String)obj;
        else
            return "received invalid message";
    }
}
