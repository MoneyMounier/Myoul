package com.server.myoul;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
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

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SealedObject;

public class MyoulServer {

    private static final String serverUser = "root";
    private static final String serverPass = "Wasr13!!";
    private static final String jdbcConnect = "jdbc:mysql://localhost:3306/Myoul?useSSL=false";

    private static final int port = 3666;
    private static final int timeout = 10000;
    private static ServerSocket server;

    protected static KeyPair keyPair;


    //TODO: when no connection can be made to database

    public static void main(String[] args){

        try {
            server = new ServerSocket(port);
            printAdresses();

            //generates keys
            KeyPairGenerator keyGen =  KeyPairGenerator.getInstance("RSA");
            keyPair = keyGen.generateKeyPair();

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
        ObjectOutputStream outStream;
        ObjectInputStream inStream;
        PublicKey clientKey;

        public Client(Socket sock){
            System.out.println("Accepted a connection");
            this.sock = sock;
            this.start();
        }

        @Override
        public void run() {
            //handle actual connection
            try {
                //step 0: set timeout
                sock.setSoTimeout(timeout);

                //step 0.1: send public key.
                outStream = new ObjectOutputStream(sock.getOutputStream());
                outStream.writeObject(keyPair.getPublic().getEncoded());

                //step 0.2: wait up to 10 seconds for clients public key
                inStream = new ObjectInputStream(sock.getInputStream());
                X509EncodedKeySpec ks = new X509EncodedKeySpec((byte[])inStream.readObject());

                KeyFactory keyFactory = KeyFactory.getInstance("RSA");
                PublicKey temp = keyFactory.generatePublic(ks);

                //repeat steps 1 through 5 until connection is done
                while (!sock.isClosed()) {

                    //step 1: receive message from client, a 10 sec wait breaks the connection
                    Container container = (Container) inStream.readObject();

                    //step 2: decrypt and continue
                    Message message = container.getMessage(keyPair.getPrivate());//throws errors

                    //step 3: get the users public key
                    if (clientKey == null) {
                        //check db first

                        /*ResultSet rset = MyoulServer.query(String.format("select key from activeUsers where username = '%s';", message.user));
                        if (rset.first()) {
                            byte[] bytes = rset.getBytes("key");
                            X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(bytes);
                            PublicKey dbtemp = keyFactory.generatePublic(publicKeySpec);
                            //if they dont equal that means someone is trying access from a new device/session if logging in let them otherwise tell em no
                            if(dbtemp.equals(temp)){
                                clientKey = temp;
                            }
                        }*/ //no Database at the moment

                        //must be logging in, check to verify. return error if trying to run a method without logging in
                        if (!(message.clsName.equals("com.server.myoul.LoginServer") && message.methName.equals("authorize"))) {
                            message.content = "login required.";
                            returnObj(message, temp);
                        }
                        //login
                        else{
                            message = LoginServer.authorize(message, temp);
                            returnObj(message, temp);
                        }
                    }
                    //run method and class provided in message
                    //what if someone manages to put a malicious class in server folder and runs its methods...
                    //step 5: run desired class and method
                    if (clientKey != null) {
                        Class<?> cls = Class.forName(message.clsName);
                        Method meth = cls.getMethod(message.methName, Message.class);

                        Message result = (Message)meth.invoke(null, message);
                        returnObj(result, clientKey);
                    }
                }
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (InvalidKeySpecException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchPaddingException e) {
                e.printStackTrace();
            } catch (IllegalBlockSizeException e) {
                e.printStackTrace();
            } catch (BadPaddingException e) {
                e.printStackTrace();
            }

            try {
                sock.close();
            } catch (IOException e) {
                //do nothing because socket is already closed
            }
        }

    private void returnObj(Message output, PublicKey key){
            if(sock.isClosed())
                return;
            try {
                Container container = new Container(key, output);
                outStream.writeObject(container);
            } catch (Exception e) {
                e.printStackTrace();
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