package com.maudit.utils;

import android.util.Base64;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by Mohankumar on 1/18/2016.
 */
public class PasswordEncryption {
    private static String password = "internalaudit@internalaudit";

    public  String encrypt(String plainText) {
        try {
            SecretKeySpec skeySpec = getKey(password);
            byte[] clearText = plainText.getBytes("UTF8");

            //IMPORTANT TO GET SAME RESULTS ON iOS and ANDROID
            final byte[] iv = new byte[16];
            Arrays.fill(iv, (byte) 0x00);
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);

            // Cipher is not thread safe
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, ivParameterSpec);

            String encrypedValue = Base64.encodeToString(
                    cipher.doFinal(clearText), Base64.DEFAULT);
            Log.d("Encrypted", "Encrypted: " + plainText + " -> " + encrypedValue);
            return encrypedValue;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public  String decrypt(String encryptedText) {
        try {
            SecretKey key = getKey(password);

            //IMPORTANT TO GET SAME RESULTS ON iOS and ANDROID
            final byte[] iv = new byte[16];
            Arrays.fill(iv, (byte) 0x00);
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);

            byte[] encrypedPwdBytes = Base64.decode(encryptedText, Base64.DEFAULT);
            // cipher is not thread safe
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
            cipher.init(Cipher.DECRYPT_MODE, key, ivParameterSpec);
            byte[] decrypedValueBytes = (cipher.doFinal(encrypedPwdBytes));

            String decrypedValue = new String(decrypedValueBytes);
            Log.d("encryptedText", "Decrypted: " + encryptedText + " -> " + decrypedValue);
            return decrypedValue;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static SecretKeySpec getKey(String password)
            throws UnsupportedEncodingException {


        int keyLength = 128;
        byte[] keyBytes = new byte[keyLength / 8];
        // explicitly fill with zeros
        Arrays.fill(keyBytes, (byte) 0x0);

        // if password is shorter then key length, it will be zero-padded
        // to key length
        byte[] passwordBytes = password.getBytes("UTF-8");
        int length = passwordBytes.length < keyBytes.length ? passwordBytes.length
                : keyBytes.length;
        System.arraycopy(passwordBytes, 0, keyBytes, 0, length);
        SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");
        return key;
    }


}
