package com.server.myoul;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.SealedObject;

public class MyoulServer {

    private static final String serverUser = "root";
    private static final String serverPass = "Wasr13!!";
    private static final String jdbcConnect = "jdbc:mysql://localhost:3306/Myoul?useSSL=false";

    private static final int port = 3666;
    private static ServerSocket server;

    public static void main(String[] args){

        try {
            server = new ServerSocket(port);
            printAdresses();

            //new thread to track logged in users

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
                PublicKey key;
                key = null;

                ObjectInputStream stream = new ObjectInputStream(sock.getInputStream());
                Message input = (Message)stream.readObject();

                //get the users key
                if(input.key == null){
                    ResultSet rset = MyoulServer.query(String.format("select key from activeUsers where username = '%s';", input.user));
                    if(!rset.first()) {
                        close("User not logged in", null);
                    }else{
                        input.key = rset.getBytes("key");
                        KeyFactory keyFactory = KeyFactory.getInstance("EC");
                        X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(input.key);
                        key = keyFactory.generatePublic(publicKeySpec);
                    }
                }
                //if they provided one theyre logging in
                else if(!(input.clsName.equals("com.server.myoul.LoginServer") && input.methName.equals("authorize"))){
                    close("must log in first", null);
                }

                try {
                    Class<?> cls = Class.forName(input.clsName);
                    Method meth = cls.getMethod(input.methName, Message.class);

                    close((Serializable)meth.invoke(null, input), key);
                } catch(Exception e) {
                    e.printStackTrace();
                    close("Invalid Message", null);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            close("error", null);
        }

        private void close(Serializable output, PublicKey key){
            if(sock.isClosed())
                return;

            if(key == null){
                try {
                    ObjectOutputStream stream = new ObjectOutputStream(sock.getOutputStream());
                    stream.writeBoolean(false);
                    stream.writeObject(output);
                    stream.flush();
                    sock.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else{
                try {

                    Cipher cipher =  Cipher.getInstance("ECIES");
                    cipher.init(Cipher.ENCRYPT_MODE, key);
                    SealedObject sealedOutput = new SealedObject(output, cipher);

                    ObjectOutputStream stream = new ObjectOutputStream(sock.getOutputStream());
                    stream.writeBoolean(true);
                    stream.writeObject(sealedOutput);
                    stream.flush();
                    sock.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static int update(String strStmt){
        try {
            Connection conn = DriverManager.getConnection(jdbcConnect, serverUser, serverPass);
            Statement stmt = conn.createStatement();
            int rset = stmt.executeUpdate(strStmt);
            return rset;

        } catch(SQLException ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    public static ResultSet query(String strStmt){
        try{

            Connection conn = DriverManager.getConnection(jdbcConnect, serverUser, serverPass);
            Statement stmt = conn.createStatement();
            ResultSet rset = stmt.executeQuery(strStmt);
            return rset;

        } catch(SQLException ex) {
            ex.printStackTrace();
        }
        return null;
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
}