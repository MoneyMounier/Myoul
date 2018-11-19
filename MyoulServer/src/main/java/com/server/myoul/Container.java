package com.server.myoul;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SealedObject;
import javax.crypto.SecretKey;

/**
 * Created by nick_ on 11/13/2018.
 */

public class Container {

    private SealedObject sealedKey;
    private SealedObject sealedMessage;

    //aglo should be "ECIES" when sent by the server and "RSA" when sent by server for performance reasons
    public Container(PublicKey publicKey, Message message, String algo){
        try {
            //gen aes key
            KeyGenerator keyGen = KeyGenerator.getInstance("AES");
            SecureRandom random = new SecureRandom();
            keyGen.init(random);
            SecretKey sKey = keyGen.generateKey();

            //encrypt secret key
            Cipher cipher = Cipher.getInstance(algo);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            sealedKey = new SealedObject(sKey, cipher);

            //encrypt message
            cipher =  Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, sKey);
            sealedMessage = new SealedObject(message, cipher);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
    }

    public Message getMessage(PrivateKey privateKey) throws InvalidKeyException, IOException, ClassNotFoundException, NoSuchAlgorithmException{
        SecretKey skey = (SecretKey)sealedKey.getObject(privateKey);
        Message message = (Message)sealedMessage.getObject(skey);
        return message;
    }
}
