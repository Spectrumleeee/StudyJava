/**
 * CryptTool entity
 * Copyright (c) 2014, TP-Link Co.,Ltd.
 * Author: liguangpu <liguangpu@tp-link.net>
 * Updated: Aug 14, 2014
 */

package com.tplink.encrypt.lgp.encrypt;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class CryptTool {

    private final static int BUFFER_SIZE = 8192;
    private final static int ENCRYPT = 0;
    private final static int DECRYPT = 1;

    private Cipher cipher;
    private KeyPair keyPair;
    private long randomSeed;

    public CryptTool(String alg) {
        try {
            if (alg.equalsIgnoreCase("AES"))
                cipher = Cipher.getInstance(alg);
            else
                cipher = Cipher.getInstance("RSA",
                        new BouncyCastleProvider());
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * Encrypt the byteArray plainText with provied public key
     * 
     * @param plainText
     *            The byteArray plainText needed to be encrypted
     * @param publicKey
     *            The public key used to encrypt the byteArray plain text
     * @return byteArray The returned encrypted byteArray text
     * @throws Exception
     */
    public byte[] Encrypt(byte[] plainText, PublicKey publicKey)
            throws Exception {
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return cipher.doFinal(plainText);
    }

    /**
     * Encrypt the file with provided RSA public key
     * 
     * @param fileIn
     *            The file need to be encrypted
     * @param publicKey
     *            The public key used to encrypt the file
     * @throws Exception
     */
    public void EncryptFile(String fileIn, PublicKey publicKey)
            throws Exception {
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        CryptFile(fileIn, cipher, ENCRYPT);
    }

    /**
     * Decrypt the byteArray cipher text with provided private key
     * 
     * @param cipherText
     *            The byteArray cipher text needed to be decrypted
     * @param privateKey
     *            The private key used to decrypt the byteArray cipher text
     * @return byteArray The returned decrypted byteArray text
     * @throws Exception
     */
    public byte[] Decrypt(byte[] cipherText, PrivateKey privateKey)
            throws Exception {
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return cipher.doFinal(cipherText);
    }

    /**
     * Decrypt the file with provided RSA private key
     * 
     * @param fileIn
     *            The file needed to be decrypted
     * @param privateKey
     *            The privte key used to decrypt the file
     * @throws Exception
     */
    public void DecryptFile(String fileIn, PrivateKey privateKey)
            throws Exception {
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        CryptFile(fileIn, cipher, DECRYPT);
    }

    /**
     * Encrypt the byteArray plain text with defalut private key
     * 
     * @param plainText
     *            The byteArray plain text needed to be encrypted
     * @return byteArray The returned encrypted byteArray text
     * @throws Exception
     */
    public byte[] Encrypt(byte[] plainText) throws Exception {
        cipher.init(Cipher.ENCRYPT_MODE, keyPair.getPublic());
        return cipher.doFinal(plainText);
    }

    /**
     * Decrypt the byteArray cipher text with defalut private key
     * 
     * @param cipherText
     *            The byteArray cipher text needed to be decrypted
     * @return byteArray The returned decrypted byteArray text
     * @throws Exception
     */
    public byte[] Decrypt(byte[] cipherText) throws Exception {
        cipher.init(Cipher.DECRYPT_MODE, keyPair.getPrivate());
        return cipher.doFinal(cipherText);
    }

    /**
     * Encrypt the byteArray plain text with AES password
     * 
     * @param plainText
     *            The byteArray plain text needed to be encrypted
     * @param password
     *            The password used to encrypt the plain text
     * @return byteArray Return byteArray encrypted text
     * @throws Exception
     */
    public byte[] Encrypt(byte[] plainText, String password) throws Exception {
        SecretKeySpec key = new SecretKeySpec(getSecretKey(password)
                .getEncoded(), "AES");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return cipher.doFinal(plainText);
    }

    /**
     * Decrypt the cipher text with AES password
     * 
     * @param cipherText
     *            The byteArray cipher text needed to be decrypted
     * @param password
     *            The password used to decrypt the cipher byteArray
     * @return byteArray
     * @throws Exception
     */
    public byte[] Decrypt(byte[] cipherText, String password) throws Exception {

        SecretKeySpec key = new SecretKeySpec(getSecretKey(password)
                .getEncoded(), "AES");
        cipher.init(Cipher.DECRYPT_MODE, key);
        return cipher.doFinal(cipherText);
    }

    /**
     * Encrypt file with AES password
     * 
     * @param fileIn
     *            The path of file needed to be encrypted
     * @param password
     *            The password used to encrypt the file
     * @throws Exception
     *             The Exception my be thrown
     */
    public void EncryptFile(String fileIn, String password) throws Exception {
        CryptFile(fileIn, password, ENCRYPT);
    }

    /**
     * Decrypt file with AES password
     * 
     * @param cipherFileIn
     *            The path of cipher file
     * @param password
     *            The password used to decrypt the cipher file
     * @throws Exception
     *             The exception may be thrown
     */
    public void DecryptFile(String cipherFileIn, String password)
            throws Exception {
        CryptFile(cipherFileIn, password, DECRYPT);
    }

    /**
     * AES Encrypt or Decrypt file operation, inner method
     * 
     * @param fileIn
     *            The file path needed to be [encrypted|decrypted]
     * @param password
     *            The password used to [encrypted|decrypted] the file
     * @param encOrDec
     *            [enc|dec] which means [encrypted|decrypted] the file
     * @throws Exception
     */
    private void CryptFile(String fileIn, String password, int encOrDec)
            throws Exception {
        SecretKeySpec key = new SecretKeySpec(getSecretKey(password)
                .getEncoded(), "AES");
        FileInputStream fis = new FileInputStream(fileIn);
        FileOutputStream fos = null;

        if (ENCRYPT == encOrDec) {
            fos = new FileOutputStream(fileIn + "_enc");
            cipher.init(Cipher.ENCRYPT_MODE, key);
        } else if (DECRYPT == encOrDec) {
            fos = new FileOutputStream(fileIn + "_dec");
            cipher.init(Cipher.DECRYPT_MODE, key);
        }

        CipherOutputStream cos = new CipherOutputStream(
                new BufferedOutputStream(fos), cipher);
        BufferedInputStream bis = new BufferedInputStream(fis);
        byte[] readBuffer = new byte[BUFFER_SIZE];
        int size = 0;
        while ((size = bis.read(readBuffer, 0, BUFFER_SIZE)) >= 0) {
            cos.write(readBuffer, 0, size);
        }
        fos.flush();
        cos.close();
        bis.close();
    }

    /**
     * RSA Encrypt or Decrypt file operation, inner method
     * 
     * @param fileIn The file needed to be encrypted
     * @param cipher The cipher used to encrypt the file
     * @param encOrDec The mode [encrypt|decrypt]
     * @throws Exception
     */
    private void CryptFile(String fileIn, Cipher cipher, int encOrDec)
            throws Exception {
        FileInputStream fis = new FileInputStream(fileIn);
        FileOutputStream fos = null;

        if (ENCRYPT == encOrDec)
            fos = new FileOutputStream(fileIn + "_rsa");
        else if (DECRYPT == encOrDec)
            fos = new FileOutputStream(fileIn + "_dec");

        // Here the encrypt buffer size must be less than 128 
        byte[] buffer_enc = new byte[100];
        // Here the decrypt buffer size must be 128
        byte[] buffer_dec = new byte[128];
        int readBytes;
        byte[] encText = null;
        byte[] newArr = null;

        if (ENCRYPT == encOrDec) {
            while ((readBytes = fis.read(buffer_enc)) != -1) {

                if (buffer_enc.length == readBytes) {
                    newArr = buffer_enc;
                } else {
                    newArr = new byte[readBytes];
                    for (int i = 0; i < readBytes; i++) {
                        newArr[i] = buffer_enc[i];
                    }
                }
                encText = cipher.doFinal(newArr);
                fos.write(encText);
            }
            fos.flush();
        } else if (DECRYPT == encOrDec) {
            while ((readBytes = fis.read(buffer_dec)) != -1) {
                if (buffer_dec.length == readBytes) {
                    newArr = buffer_dec;
                } else {
                    newArr = new byte[readBytes];
                    for (int i = 0; i < readBytes; i++) {
                        newArr[i] = buffer_dec[i];
                    }
                }
                encText = cipher.doFinal(newArr);
                fos.write(encText);
            }
            fos.flush();
        }
        fis.close();
        fos.close();
    }

    /**
     * RSA Encrypt or Decrypt file operation [Reserved method, little bug]
     * 
     * @param fileIn
     * @param cipher
     * @param encOrDec
     * @throws Exception
     */
    @SuppressWarnings("unused")
    private void CryptFile_Back(String fileIn, Cipher cipher, int encOrDec)
            throws Exception {
        FileInputStream fis = new FileInputStream(fileIn);
        FileOutputStream fos = null;

        if (ENCRYPT == encOrDec)
            fos = new FileOutputStream(fileIn + "_rsa");
        else if (DECRYPT == encOrDec)
            fos = new FileOutputStream(fileIn + "_dec");

        int size = fis.available();
        byte[] encryptByte = new byte[size];
        fis.read(encryptByte);
        // RSA must use block encryption, get the block size of encryption
        int blockSize = cipher.getBlockSize();

        if (ENCRYPT == encOrDec) {
            // Get the output block size
            int outputBlockSize = cipher.getOutputSize(encryptByte.length);
            // Calculte the number of blocks needed to be encrypted
            int leavedSize = encryptByte.length % blockSize;
            int blocksNum = leavedSize == 0 ? encryptByte.length / blockSize
                    : encryptByte.length / blockSize + 1;

            byte[] cipherData = new byte[blocksNum * outputBlockSize];
            // Encrypt each block
            for (int i = 0; i < blocksNum; i++) {
                if ((encryptByte.length - i * blockSize) > blockSize) {
                    cipher.doFinal(encryptByte, i * blockSize, blockSize,
                            cipherData, i * outputBlockSize);
                } else {
                    cipher.doFinal(encryptByte, i * blockSize,
                            encryptByte.length - i * blockSize, cipherData, i
                                    * outputBlockSize);
                }
                fos.write(cipherData);
            }
        } else if (DECRYPT == encOrDec) {
            int j = 0;
            // Decrypt each block
            while ((encryptByte.length - j * blockSize) > 0) {
                fos.write(cipher.doFinal(encryptByte, j * blockSize, blockSize));
                j++;
            }
        }
        fis.close();
        fos.close();
    }

    /**
     * Transfer the byteArray of encoded RSA PublicKey to PublicKey instance
     * 
     * @param publicKeyEncoded
     *            The byteArray of encoded RSA public key
     * @return PublicKey Return a instance of PublicKey
     */
    public static PublicKey byte2PublicKey(byte[] publicKeyEncoded) {
        PublicKey publickey = null;

        try {
            KeySpec keySpec = new X509EncodedKeySpec(publicKeyEncoded);
            KeyFactory factory = KeyFactory.getInstance("RSA");
            publickey = factory.generatePublic(keySpec);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return publickey;
    }

    /**
     * Set the random seed of SecureRandom used to generate the RSA keyPair
     * 
     * @param rs
     *            The long parameter of random seed
     */
    public void setRandomSeed(long rs) {
        randomSeed = rs;
    }

    /**
     * Get the RSA KeyPair
     * 
     * @return KeyPair
     */
    public KeyPair getKeyPair() {
        KeyPairGenerator keyGen = null;
        try {
            keyGen = KeyPairGenerator.getInstance("RSA",
                    new BouncyCastleProvider());
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        SecureRandom sr = new SecureRandom();
        sr.setSeed(randomSeed);
        keyGen.initialize(1024, sr);
        return keyGen.generateKeyPair();
    }

    /**
     * Get the AES secret key
     * 
     * @param password
     *            The password used to generate the AES secret key
     * @return SecetKey
     */
    private SecretKey getSecretKey(String password) {
        KeyGenerator keyGen = null;
        try {
            keyGen = KeyGenerator.getInstance("AES");
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        keyGen.init(128, new SecureRandom(password.getBytes()));
        return keyGen.generateKey();
    }
}
