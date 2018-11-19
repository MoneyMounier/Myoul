package com.server.myoul;

import java.io.Serializable;
import java.util.UUID;

public class Message implements Serializable {

    public UUID id;

    public String clsName;
    public String methName;
    public String user;
    public Serializable args;

    public Message(String clsName, String methName, String user, Serializable args){
        this.clsName = new StringBuilder("com.server.myoul.").append(clsName).toString();
        this.methName = methName;
        this.user = user;
        this.args = args;

        id = UUID.randomUUID();
    }
}
