package com.tplink.encrypt.lgp.encrypt;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * Hello world!
 * 
 */
public class TestMain {
    public static void main(String[] args){

//        testEnDecryptString();
//        testAESEnDecryptFile();
        testRSAEnDecryptFile();
        
    }
    
    public static void testRSAEnDecryptFile(){
        CryptTool tool = new CryptTool("RSA");
        String fileIn = "D:/TEST/ThreadPool-li.java";
        
        KeyPair keyPair = tool.getKeyPair();
        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();
        
        try {
            tool.EncryptFile(fileIn, publicKey);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            System.out.println("Failed to RSA Encrypt file");
            e.printStackTrace();
        }
        try {
            tool.DecryptFile(fileIn+"_rsa", privateKey);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            System.out.println("Failed to RSA Decrypt file");
            return;
        }
    }
    
    public static void testAESEnDecryptFile(){
        CryptTool tool = new CryptTool("AES");
        String fileIn = "d:/JUnit In Action.pdf";
        String password = "123456";
        
        try {
            tool.EncryptFile(fileIn, password);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            System.out.println("Failed to AES Encrypt file");
            return;
        }
        try {
            tool.DecryptFile(fileIn+"_enc", password);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            System.out.println("Failed to AES Decrypt file");
            return;
        }
        
    }
    
    public static void testEnDecryptString(){
        CryptTool tool = new CryptTool("AES");
        String plainText = "Hello World ! Welcome SpectrumLeeee!";
        String password = "123456";
        
        byte[] cipherText = null;
        try {
            cipherText = tool.Encrypt(plainText.getBytes(), password);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            System.out.println("Failed to Encrypt! ");
            return;
        }
        byte[] decryptText = null;
        try {
            decryptText = tool.Decrypt(cipherText, "123456");
        } catch (Exception e) {
            System.out.println("Failed to Decrypt! ");
            return;
        }
        
        System.out.println(new String(decryptText, 0, decryptText.length));
    }

}