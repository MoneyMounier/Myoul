package com.example.myouldb;

import java.net.ServerSocket;
import java.net.Socket;

public class MyoulDB {

    private static final int port = 3666;
    private static ServerSocket server;

    public static void main(String[] args){

        try {
            server = new ServerSocket(port);
            while(true){
                Socket sock = server.accept();
                //create a new thread to handle the socket
            }
        } catch(Exception e){
            e.printStackTrace();
        }

    }
}
