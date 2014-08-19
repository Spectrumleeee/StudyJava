/**
 * 
 * Copyright (c) 2014, TP-Link Co.,Ltd.
 * Author: liguangpu <liguangpu@tp-link.net>
 * Updated: Aug 14, 2014
 */

package com.tplink.encrypt.lgp.encrypt;

public class Secret {

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub

        CryptTool tool = new CryptTool("AES");

        if (3 == args.length && args[1].equals("enc")) {
            try {
                tool.EncryptFile(args[0], args[2]);
                System.out.println("Success to Encrypt the file!");
            } catch (Exception e) {
                // TODO Auto-generated catch block
                System.out.println("Failed to Encrypt the file!");
                return;
            }
        } else if (3 == args.length && args[1].equals("dec")) {
            try {
                tool.DecryptFile(args[0], args[2]);
                System.out.println("Success to Decrypt the file!");
            } catch (Exception e) {
                // TODO Auto-generated catch block
                System.out.println("Failed to Decrypt the file!");
                return;
            }
        } else{
            System.out.println("USAGE: java Secret [filePath] [password] [enc|dec]");
        }
    }

}
