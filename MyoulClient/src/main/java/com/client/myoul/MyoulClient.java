package com.client.myoul;

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

    public static String query(String cmd, String address, int port){

        MyoulClient client = new MyoulClient(cmd, address, port);
        client.start();
        try {
            client.join();
            return client.result;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void run(){

        try {
            Socket sock = new Socket(address, port);

            ObjectOutputStream out = new ObjectOutputStream(sock.getOutputStream());
            out.writeObject(cmd);

            ObjectInputStream in = new ObjectInputStream(sock.getInputStream());
            result = (String)in.readObject();

            sock.close();

            //return result;
        } catch (Exception e) {
            e.printStackTrace();
        }

        //return null;
    }
}
