package com.client.myoul;

import com.server.myoul.Message;

import java.util.UUID;

public class LoginClient {

    public static Message Login(MyoulClient client, String user, String pass){
        Message message = new Message("LoginServer", "authorize", user);
        message.content = pass;
        if(client.send(LoginClient.class, message, false))
            return message;
        else
            return null;
    }

    public static String checkReply(MyoulClient client, UUID id){
        Message message = client.recieve(id);

        if(message == null)
            return null;
        else if(message.content.getClass() == String.class)
            return (String)message.content;

        System.out.println(message.content.getClass());
        return "received invalid message.";
    }
}
