package com.server.myoul;

import java.io.Serializable;
import java.util.UUID;

public class Message implements Serializable {

    public UUID id;
    public String clsName;
    public String methName;
    public String user;

    //content must be sent after object creation and before sealing it in a container
    public Serializable content;

    public Message(String clsName, String methName, String user){
        this.clsName = new StringBuilder("com.server.myoul.").append(clsName).toString();
        this.methName = methName;
        this.user = user;
        id = UUID.randomUUID();
    }
}
