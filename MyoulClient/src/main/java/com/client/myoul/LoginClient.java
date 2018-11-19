package com.client.myoul;

import com.server.myoul.Message;

import java.util.UUID;

public class LoginClient {

    public static Message Login(MyoulClient client, String user, String pass){
        Message message = new Message("LoginServer", "authorize", user, pass);
        if(client.send(LoginClient.class, message, false))
            return message;
        else
            return null;
    }

    public static String checkReply(MyoulClient client, UUID id){
        Object obj = client.recieve(id);

        if(obj == null)
            return null;
        else if(obj.getClass() == String.class)
            return (String)obj;

        System.out.println(obj.getClass());
        return "received invalid message";
    }
}
