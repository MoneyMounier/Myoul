package com.example.myouldb;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class MyoulDB {

    private static final int port = 3666;
    private static ServerSocket server;

    public static void main(String[] args){

        try {
            server = new ServerSocket(port);
            while(true){
                new Client(server.accept());
                //create a new thread to handle the socket
            }
        } catch(Exception e){
            e.printStackTrace();
        }

    }

    private static class Client extends Thread{

        Socket sock;

        public Client(Socket sock){
            System.out.println("Accepted a connection");
            this.sock = sock;
            this.start();
        }

        @Override
        public void run(){
            //handle actual connection
            try {
                ObjectInputStream stream = new ObjectInputStream(sock.getInputStream());
                String input = (String)stream.readObject();
                String[] cmd = input.split(" ");
                if(cmd[0] == "login"){
                    //run login script
                }else
                    close("Invalid Command");
            } catch (Exception e) {
                e.printStackTrace();
            }

            close("error");
        }

        private void close(String result){
            //send result to client and shutdown this thread
        }
    }
}