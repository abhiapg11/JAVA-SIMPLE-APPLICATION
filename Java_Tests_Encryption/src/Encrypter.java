import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


public class Encrypter {
    private static SecretKey mSecretKey = generateKey();
//    private static IvParameterSpec mIvspec = new IvParameterSpec(new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0});
    private static IvParameterSpec mIvspec = new IvParameterSpec(mSecretKey.getEncoded());
    
    private static SecretKey generateKey() {
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance("AES");
            keyGen.init(128);
            return keyGen.generateKey();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        
        return null;
    }

    @SuppressWarnings("unused")
    private static SecretKey getKey(byte[] keyBytes) {
        return new SecretKeySpec(keyBytes, "AES");
    }

    private static Cipher getCipher(int opmode)
            throws NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidKeyException, InvalidAlgorithmParameterException {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(opmode, mSecretKey, mIvspec);
        return cipher;
    }

    public static void encrypt(String filePathToEncrypt, String filePathWithEncryptResult) {
        try {
            encryptStream(new FileInputStream(filePathToEncrypt), new FileOutputStream(filePathWithEncryptResult));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
    }

    private static void encryptStream(FileInputStream fileInputStream, FileOutputStream fileOutputStream) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] writeBuffer = new byte[1024];
        int bytesReaded = 0;

        try {
            CipherOutputStream cipherOutputStream = new CipherOutputStream(fileOutputStream, getCipher(Cipher.ENCRYPT_MODE));

            while ((bytesReaded = fileInputStream.read(writeBuffer)) != -1) {
                cipherOutputStream.write(writeBuffer, 0, bytesReaded);
            }

            byteArrayOutputStream.writeTo(cipherOutputStream);
            cipherOutputStream.close();
            byteArrayOutputStream.reset();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void decrypt(String filePathWithEncryptedData, String filePathWithDecryptedResult) {
        try {
            decryptStream(new FileInputStream(filePathWithEncryptedData), new FileOutputStream(filePathWithDecryptedResult));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
    }

    private static void decryptStream(InputStream inputStream, OutputStream outputStream) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException {
        byte[] readBuffer = new byte[1024];
        int byterRead = 0;

        try {
            inputStream = new CipherInputStream(inputStream, getCipher(Cipher.DECRYPT_MODE));

            while ((byterRead = inputStream.read(readBuffer)) != -1) {
                outputStream.write(readBuffer, 0, byterRead);
            }

            outputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    public static void cip() {
//        try {
////            SecretKey secretKey = generateKey();
////            byte[] secretKeyBytes = secretKey.getEncoded();
////            System.out.println("bytes: " + secretKeyBytes);
////
////            for (byte b : secretKeyBytes) {
////                System.out.print("[" + b + "]");
////            }
//
////            byte[] iv = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
////            IvParameterSpec ivspec = new IvParameterSpec(iv);
//
//            byte[] clearText = "This is clear text".getBytes();
//            // -------------
//            Cipher cipherEncrypt = getCipher(Cipher.ENCRYPT_MODE);
//            byte[] cipherText = cipherEncrypt.doFinal(clearText);
//
//            System.out.println("\nCiphered: " + new String(cipherText));
//
//            // -------------
//            Cipher cipherDecrypt = Cipher.getInstance("AES/CBC/PKCS5Padding");
////            cipherDecrypt.init(Cipher.DECRYPT_MODE, secretKey, ivspec);
//            
//            SecretKey decryptSecretKey = new SecretKeySpec(mSecretKey.getEncoded(), "AES");
//            cipherDecrypt.init(Cipher.DECRYPT_MODE, decryptSecretKey, mIvspec);
//            
//            byte[] decryptedText = cipherDecrypt.doFinal(cipherText);
//            System.out.println("Decrypted: " + new String(decryptedText));
//
//        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException | InvalidAlgorithmParameterException e) {
//            e.printStackTrace();
//        }
//    }
}
