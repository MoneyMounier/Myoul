package com.client.myoul;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.util.concurrent.SynchronousQueue;
import com.server.myoul.Message;
import javax.crypto.SealedObject;

//connect to server, issue command, and wait for result
public class MyoulClient extends Thread{


    public static Object query(String cls, String meth, String user, String cmd, String address, int port){

        Message message = new Message(cls, meth, user, cmd);
        //needs to be written
        return null;
    }

    //non static methods and variables

    private final int timeout = 10000;//timeout in ms

    private String serverAddress;
    private int port;

    protected KeyPair keyPair;
    //protected Cipher cipher;

    public SynchronousQueue<Message> input;
    public SynchronousQueue<Object> output;

    public boolean send(Message message){
        return input.offer(message);
    }

    public Object recieve(){
        return output.poll();
    }

    public MyoulClient(String address, int port) {
        super("MyoulClient");
        this.serverAddress = address;
        this.port = port;

        try {
            //generates keys and inits cipher(might not need cipher)
            KeyPairGenerator keyGen =  KeyPairGenerator.getInstance("EC");
            keyPair = keyGen.generateKeyPair();
            //cipher = Cipher.getInstance("ECIES", keyGen.getProvider());
            //cipher.init(Cipher.DECRYPT_MODE, keyPair.getPrivate());
        }catch (Exception e){
            e.printStackTrace();
        }

        input = new SynchronousQueue<>();
        output = new SynchronousQueue<>();

        this.start();
    }

    @Override
    public void run() {
        while(true){
            try{
                //wait for a message from another thread
                Message message = input.take();
                if (message != null) {
                    //send and wait to recieve
                    Object result = communicate(message);
                    if(result != null){
                        //offer the result, waiting for another thread to recieve it
                        output.put(result);
                    }
                }
            } catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    private Object communicate(Message message){
        try {
            //get connection with the timeout value
            Socket sock = new Socket();
            Object result;

            sock.connect(new InetSocketAddress(this.serverAddress, this.port), this.timeout);

            //send message
            ObjectOutputStream output = new ObjectOutputStream(sock.getOutputStream());
            output.writeObject(message);

            //wait for response and close
            ObjectInputStream input = new ObjectInputStream(sock.getInputStream());

            boolean encrypted = input.readBoolean();

            if(encrypted) {
                //retrieve and decrypt the message
                SealedObject sealedResult = (SealedObject) input.readObject();
                sock.close();
                return sealedResult.getObject(keyPair.getPrivate());
            }else {
                result = input.readObject();
                sock.close();
                return result;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
