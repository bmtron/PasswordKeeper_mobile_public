package com.brendan.passwordkeeper;

import android.os.Build;
import android.util.Xml;

import com.brendan.passwordkeeper.Globals.GlobalVars;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import androidx.annotation.RequiresApi;

public class Crypto {

    private static byte[] IV = Generate128BitsOfRandomEntropy();
    private static String PASSWORD = GlobalVars.passKey;
    private static byte[] SALT = Generate128BitsOfRandomEntropy();


    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String Encrypt(String plainText) throws Exception {
            try {
            Cipher c = getCipher(Cipher.ENCRYPT_MODE);
            byte[] encryptedVal = c.doFinal(getBytes(plainText));
            byte[] saltAndIvArr = concat(SALT, IV);
            byte[] fullByteArr = concat(saltAndIvArr, encryptedVal);
            String encoded = java.util.Base64.getEncoder().encodeToString(fullByteArr);
            return encoded;
        } catch(Throwable err) {
            throw new RuntimeException(err);
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String Decrypt(String cipherText) throws Exception {

        try {
            byte[] cipherTextBytesWithSaltAndIv = Base64.getDecoder().decode(cipherText);
            byte[] saltStringBytes = getSaltBytesFromCipher(cipherTextBytesWithSaltAndIv, 16);
            byte[] ivStringBytes = getIvBytesFromCipher(cipherTextBytesWithSaltAndIv, 16);
            byte[] cipherTextBytes = getCipherBytesFromCipher(cipherTextBytesWithSaltAndIv, 16);
            Cipher c = getDecryptCipher(Cipher.DECRYPT_MODE, ivStringBytes, saltStringBytes);
            byte[] decryptedVal = c.doFinal(cipherTextBytes);
            String encoded = new String(decryptedVal, StandardCharsets.UTF_8);
            return encoded;
        } catch (Throwable err) {
            throw new RuntimeException(err);
        }
    }

    private static byte[] getBytes(String str) throws UnsupportedEncodingException {
        return str.getBytes("UTF-8");
    }
    private static Cipher getCipher(int mode) throws Exception {

        Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
        byte[] iv = IV;
        c.init(mode, generateKey(), new IvParameterSpec(iv));
        return c;
    }

    private static Key generateKey() throws Exception {

        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] salt = SALT;

        KeySpec spec = new PBEKeySpec(PASSWORD.toCharArray(), salt, 65536, 128);
        SecretKey scrtKey = factory.generateSecret(spec);
        byte[] encoded = scrtKey.getEncoded();
        SecretKeySpec finalKeySpec = new SecretKeySpec(encoded, "AES");
        return finalKeySpec;
    }

    private static Cipher getDecryptCipher(int mode, byte[] iv, byte[] salt) throws Exception {
        Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
        c.init(mode, generateDecryptKey(salt), new IvParameterSpec(iv));
        return c;
    }
    private static Key generateDecryptKey(byte[] saltBytes) throws Exception {
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] salt = saltBytes;

        KeySpec spec = new PBEKeySpec(PASSWORD.toCharArray(), salt, 65536, 128);
        SecretKey scrtKey = factory.generateSecret(spec);
        byte[] encoded = scrtKey.getEncoded();
        SecretKeySpec finalKeySpec = new SecretKeySpec(encoded, "AES");
        return finalKeySpec;
    }
    private static byte[] Generate128BitsOfRandomEntropy() {
        byte[] randomBytes = new byte[16];
        SecureRandom random = new SecureRandom();
        random.nextBytes(randomBytes);

        return randomBytes;
    }
    private static byte[] concat(byte[] arr1, byte[] arr2) {
        byte[] firstArray = arr1;        //source array
        byte[] secondArray = arr2;  //destination array
        int fal = firstArray.length;        //determines length of firstArray
        int sal = secondArray.length;   //determines length of secondArray
        byte[] result = new byte[fal + sal];  //resultant array of size first array and second array
        System.arraycopy(firstArray, 0, result, 0, fal);
        System.arraycopy(secondArray, 0, result, fal, sal);
        return result;
    }
    private static byte[] getSaltBytesFromCipher(byte[] cipher, int size) {
        byte[] result = new byte[16];
        for (int i = 0; i < 16; i++) {
            result[i] = cipher[i];
        }
        return result;
    }

    private static byte[] getIvBytesFromCipher(byte[] cipher, int size) {
        byte[] result = new byte[16];
        int resultIndexCount = 0;
        for (int i = 16; i < 32; i++) {
            result[resultIndexCount] = cipher[i];
            resultIndexCount++;
        }
        return result;
    }
    private static byte[] getCipherBytesFromCipher(byte[] cipher, int size) {
        byte[] result = new byte[cipher.length - 32];
        int resultIndexCount = 0;
        for (int i = 32; i < cipher.length; i++) {
            result[resultIndexCount] = cipher[i];
            resultIndexCount++;
        }
        return result;
    }
}
