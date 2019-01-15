package com.client.myoul;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Map;
import java.util.UUID;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import com.server.myoul.Container;
import com.server.myoul.Message;
import javax.crypto.Cipher;
import javax.crypto.SealedObject;

public class MyoulClient extends Thread{

    //TODO: write standalone client for website
    public static Object query(){

        Runnable[][] functions = {{(int i) -> i+1}};

        return null;
    }

    //non static methods and variables
    private String serverAddress;
    private int port;
    private Socket sock;
    private ObjectOutputStream outStream;
    private ObjectInputStream inStream;

    //TODO key store
    protected KeyPair keyPair;
    protected PublicKey serverKey;

    //queues to transfer around data
    public ConcurrentHashMap<String, Message> input;
    public ConcurrentHashMap<UUID, Message> output;

    public boolean send(String cls, Message message, boolean replace){
        if(replace && input.containsKey(cls)) {
            input.replace(cls, message);
            return true;
        }
        else if(input.containsKey(cls)){
            return false;
        }else{
            input.put(cls, message);
            return true;
        }
    }

    public Message recieve(UUID id){return output.remove(id);}

    public MyoulClient(String address, int port) {
        super("MyoulClient");
        this.serverAddress = address;
        this.port = port;

        try {
            //generates keys
            KeyPairGenerator keyGen =  KeyPairGenerator.getInstance("RSA");
            keyPair = keyGen.generateKeyPair();

        }catch (Exception e){
            e.printStackTrace();
        }

        input = new ConcurrentHashMap<>();
        output = new ConcurrentHashMap<>();

        this.start();
    }

    //TODO: add functionality to detect when no connection can be made or connection is lost and handle it appropriately
    //TODO: handle error messages from server
    @Override
    public void run() {

        Communicate connection = null;

        while (true) {
            if(input.size() > 0){
                //process if any messages in map
                if (connection == null || !connection.isAlive()) {
                    System.out.println("starting communicate");
                    connection = new Communicate();
                }
            }
        }
    }

  private class Communicate extends Thread{

        private final int timeout = 10000;//timeout in ms

        public Communicate(){
            this.start();
        }

        //TODO: rebuild for v3
        @Override
        public void run() {
            Message message;
            Message result;
            Container container;

            try {
                sock = new Socket();
                sock.connect(new InetSocketAddress(serverAddress, port), timeout);

                //step 0.1: send public key.
                outStream = new ObjectOutputStream(sock.getOutputStream());
                outStream.writeObject(keyPair.getPublic().getEncoded());

                //step 0.2: wait up to 10 seconds for clients public key
                inStream = new ObjectInputStream(sock.getInputStream());
                X509EncodedKeySpec ks = new X509EncodedKeySpec((byte[])inStream.readObject());
                serverKey = KeyFactory.getInstance("RSA").generatePublic(ks);

                //repeat steps 1 to 5 until input is empty
                while (!input.isEmpty()) {

                    //step 1: get all messages currently in hash map and process them
                    for(Map.Entry<String, Message> entry : input.entrySet()) {

                        //step 2: remove next message from hashmap (no order enforced)
                        message = input.remove(entry.getKey());

                        //step 3: seal it up and send it to server
                        container = new Container(serverKey, message);
                        outStream.writeObject(container);

                        //step 4: wait for, recieve and decrypt result
                        container = (Container)inStream.readObject();
                        result = container.getMessage(keyPair.getPrivate());

                        //step 5: place result in output
                        output.put(result.id, result);
                    }
                }
            } catch(IOException e){
                e.printStackTrace();
            } catch(ClassNotFoundException e){
                e.printStackTrace();
            } catch(NoSuchAlgorithmException e){
                e.printStackTrace();
            } catch(InvalidKeyException e){
                e.printStackTrace();
            } catch(Exception e){
                e.printStackTrace();
            }

            try {
                sock.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            sock = null;
        }
    }
}
