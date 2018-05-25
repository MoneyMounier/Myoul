package com.client.myoul;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

//connect to server, issue command, and wait for result
public class MyoulClient extends Thread{

    private final int timeout = 10000;//timeout in ms

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
            System.out.println(client.result);
            return client.result;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void run(){

        try {
            Socket sock = new Socket();
            sock.connect(new InetSocketAddress(address, port), timeout);

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
