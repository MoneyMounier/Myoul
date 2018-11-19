package com.client.myoul;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import com.server.myoul.Message;
import javax.crypto.Cipher;
import javax.crypto.SealedObject;

public class MyoulClient extends Thread{

    //TODO: write standalone client for website
    public static Object query(){
        //needs to be written
        return null;
    }

    //non static methods and variables
    private String serverAddress;
    private int port;
    private Socket sock;
    private ObjectOutputStream outStream;
    private ObjectInputStream inStream;

    //put in key store
    protected KeyPair keyPair;
    protected PublicKey serverKey;

    //queues to transfer around data
    public ConcurrentHashMap<Class<?>, Message> input;
    public ConcurrentHashMap<UUID, Object> output;

    public boolean send(Class<?> cls, Message message, boolean replace){
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

    public Object recieve(UUID id){return output.remove(id);}

    public MyoulClient(String address, int port) {
        super("MyoulClient");
        this.serverAddress = address;
        this.port = port;

        try {
            //generates keys
            KeyPairGenerator keyGen =  KeyPairGenerator.getInstance("EC");
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
        Message message;
        Object result;

        public Communicate(){
            this.start();
        }

        //TODO: rebuild for v3
        @Override
        public void run() {
            try {
                sock = new Socket();
                sock.connect(new InetSocketAddress(serverAddress, port), timeout);

                //step 0.1: send public key.
                outStream = new ObjectOutputStream(sock.getOutputStream());
                outStream.writeObject(keyPair.getPublic());

                //step 0.2: wait up to 10 seconds for clients public key
                inStream = new ObjectInputStream(sock.getInputStream());
                PublicKey temp = (PublicKey) inStream.readObject();

                while (!input.isEmpty()) {
                    for(Map.Entry<Class<?>, Message> entry : input.entrySet()) {
                        System.out.println("sending message");
                        input.remove(entry.getKey());
                        message = entry.getValue();


                        //send message encrypting if key is provided
                        if (serverKey == null) {
                            //send unencrypted object
                            ObjectOutputStream outStream = new ObjectOutputStream(sock.getOutputStream());
                            outStream.writeObject("missing server key");
                        } else {
                            //seal message
                            Cipher cipher = Cipher.getInstance("RSA");
                            cipher.init(Cipher.ENCRYPT_MODE, serverKey);
                            SealedObject sealedOutput = new SealedObject(output, cipher);
                            //send it
                            ObjectOutputStream stream = new ObjectOutputStream(sock.getOutputStream());
                            stream.writeObject(sealedOutput);
                        }


                        //wait for and process object
                        ObjectInputStream inStream = new ObjectInputStream(sock.getInputStream());
                        result = inStream.readObject();

                        if (PublicKey.class.isAssignableFrom(result.getClass())) {
                            System.out.println("got a key");

                            serverKey = (PublicKey) result;
                            //need to resend message
                            input.put(entry.getKey(), message);
                        } else if (result.getClass() == SealedObject.class)
                            output.put(message.id, ((SealedObject) result).getObject(keyPair.getPrivate()));
                        else
                            output.put(message.id, result);//only used for unencrypted error messages
                    }
                }
            } catch(IOException e){
                result = "IOException";
                e.printStackTrace();
            } catch(ClassNotFoundException e){
                result = "ClassNotFoundException";
                e.printStackTrace();
            } catch(NoSuchAlgorithmException e){
                result = "NoSuchAlgorithmException";
                e.printStackTrace();
            } catch(InvalidKeyException e){
                result = "InvalidKeyException";
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
