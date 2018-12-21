package com.server.myoul;

import java.io.IOException;
import java.io.Serializable;
import java.security.InvalidKeyException;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SealedObject;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by nick_ on 11/13/2018.
 */

public class Container implements Serializable{

    private byte[] sealedKey;
    private SealedObject sealedMessage;

    public Container(PublicKey publicKey, Message message){
        try {
            //gen aes key
            KeyGenerator keyGen = KeyGenerator.getInstance("AES");
            SecureRandom random = new SecureRandom();
            keyGen.init(random);
            SecretKey sKey = keyGen.generateKey();

            //encrypt secret key
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            sealedKey = cipher.doFinal(sKey.getEncoded());

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
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
    }

    public Message getMessage(PrivateKey privateKey) throws InvalidKeyException, IOException, ClassNotFoundException, NoSuchAlgorithmException, NoSuchPaddingException, BadPaddingException, IllegalBlockSizeException {

        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);

        SecretKey skey = new SecretKeySpec(cipher.doFinal(sealedKey), "AES" );;

        Message message = (Message)sealedMessage.getObject(skey);
        return message;
    }
}
