package com.server.myoul;

import java.io.Serializable;

public class Message implements Serializable {

    public String clsName;
    public String methName;
    public String user;
    public String[] cmd;
    public byte[] key;

    public Message(String clsName, String methName, String user, String cmd){
        this.clsName = new StringBuilder("com.server.myoul.").append(clsName).toString();
        this.methName = methName;
        this.user = user;
        this.cmd = cmd.split(" ");
    }
}
