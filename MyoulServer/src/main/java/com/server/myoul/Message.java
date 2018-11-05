package com.server.myoul;

import java.io.Serializable;

public class Message implements Serializable {
    public String clsName;
    public String methName;
    public String[] cmd;

    public Message(String clsName, String methName, String cmd){
        this.clsName = new StringBuilder("com.server.myoul.").append(clsName).toString();
        this.methName = methName;
        this.cmd = cmd.split(" ");
    }
}
