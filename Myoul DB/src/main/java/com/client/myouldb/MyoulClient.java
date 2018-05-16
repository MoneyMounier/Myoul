package com.client.myouldb;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

//connect to server, issue command, and wait for result
public class MyoulClient extends Thread{

    private String cmd, address, result;
    private int port;

    public MyoulClient(String cmd, String address, int port) {
        this.cmd = cmd;
        this.address = address;
        this.port = port;

    }

    @Override
    public void run(){

        try {
            System.out.println(cmd + " " + address + " " +port);
            Socket sock = new Socket(address, port);

            ObjectOutputStream out = new ObjectOutputStream(sock.getOutputStream());
            out.writeObject(cmd);

            ObjectInputStream in = new ObjectInputStream(sock.getInputStream());
            String result = (String)in.readObject();

            sock.close();

            //return result;
        } catch (Exception e) {
            e.printStackTrace();
        }

        //return null;
    }
}
