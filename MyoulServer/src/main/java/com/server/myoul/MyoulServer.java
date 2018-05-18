package com.server.myoul;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

public class MyoulServer {

    private static final int port = 3666;
    private static ServerSocket server;

    public static void main(String[] args){

        try {
            server = new ServerSocket(port);
            printAdresses();
            while(true){
                new Client(server.accept());
                //create a new thread to handle the socket
            }
        } catch(Exception e){
            e.printStackTrace();
        }

    }

    private static void printAdresses(){
        ///////////////////////////////
        //prints addresses server is listening to on in console
        List<String> addresses = new ArrayList<String>();
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            for(NetworkInterface ni : Collections.list(interfaces)){
                for(InetAddress address : Collections.list(ni.getInetAddresses()))
                {
                    if(address instanceof Inet4Address){
                        addresses.add(address.getHostAddress());
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }

        // Print the contents of our array to a string.  Yeah, should have used StringBuilder
        String ipAddress = new String("");
        for(String str:addresses)
        {
            ipAddress = ipAddress + str + "\n";
        }

        System.out.println(ipAddress);
        /////////////////////////
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
                System.out.println(input);
                String[] cmd = input.split(" ");
                String result = null;
                if(cmd[0].equals("login") && cmd.length == 3){
                    result = LoginServer.authorize(cmd[1], cmd[2]);
                    close(result);
                }else
                    close("Invalid Command");

            } catch (Exception e) {
                e.printStackTrace();
            }

            close("error");
        }

        private void close(String result){
            if(sock.isClosed())
                return;

            try {
                System.out.println(result);
                ObjectOutputStream stream = new ObjectOutputStream(sock.getOutputStream());
                stream.writeObject(result);
                stream.flush();
                sock.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}