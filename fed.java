import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import java.security.MessageDigest;
import java.io.*;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.spec.IvParameterSpec;
import java.security.SecureRandom;

public class fed {
    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";
    private static final int BUFFER_SIZE = 8192;
    private static final int IV_LENGTH = 16;

    // สร้าง SecretKey จาก password
    public static SecretKey generateKey(String password) throws Exception {
        byte[] key = MessageDigest.getInstance("SHA-256").digest(password.getBytes("UTF-8"));
        return new SecretKeySpec(Arrays.copyOf(key, 16), "AES");
    }

    // สร้าง IV แบบสุ่ม
    private static IvParameterSpec generateIv() {
        byte[] iv = new byte[IV_LENGTH];
        new SecureRandom().nextBytes(iv);
        return new IvParameterSpec(iv);
    }

    public static void encryptFile(File inputFile, File outputFile, SecretKey secretKey) throws Exception {
        if (!inputFile.exists()) {
            throw new FileNotFoundException("Input file not found: " + inputFile.getPath());
        }
        
        IvParameterSpec iv = generateIv();
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
        
        try (FileInputStream fis = new FileInputStream(inputFile);
             FileOutputStream fos = new FileOutputStream(outputFile)) {
            
            // เขียน IV ลงในไฟล์ปลายทางที่ Encrypt
            fos.write(iv.getIV());
            
            // Encrypt ไฟล์
            try (CipherOutputStream cos = new CipherOutputStream(fos, cipher)) {
                byte[] buffer = new byte[BUFFER_SIZE];
                long totalBytes = inputFile.length();
                long processedBytes = 0;
                int bytesRead;
                
                while ((bytesRead = fis.read(buffer)) != -1) {
                    cos.write(buffer, 0, bytesRead);
                    processedBytes += bytesRead;
                    int progress = (int) ((processedBytes * 100) / totalBytes);
                    System.out.print("\rEncrypting: " + progress + "%");
                }
            }
            System.out.println("\nFile encrypted successfully: " + outputFile.getPath());
        }
    }

    public static void decryptFile(File inputFile, File outputFile, SecretKey secretKey) throws Exception {
        if (!inputFile.exists()) {
            throw new FileNotFoundException("Input file not found: " + inputFile.getPath());
        }
        
        try (FileInputStream fis = new FileInputStream(inputFile)) {
            // อ่าน IV
            byte[] ivBytes = new byte[IV_LENGTH];
            fis.read(ivBytes);
            IvParameterSpec iv = new IvParameterSpec(ivBytes);
            
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
            
            try (FileOutputStream fos = new FileOutputStream(outputFile);
                 CipherOutputStream cos = new CipherOutputStream(fos, cipher)) {
                
                byte[] buffer = new byte[BUFFER_SIZE];
                long totalBytes = inputFile.length() - IV_LENGTH;
                long processedBytes = 0;
                int bytesRead;
                
                while ((bytesRead = fis.read(buffer)) != -1) {
                    cos.write(buffer, 0, bytesRead);
                    processedBytes += bytesRead;
                    int progress = (int) ((processedBytes * 100) / totalBytes);
                    System.out.print("\rDecrypting: " + progress + "%");
                }
            }
            System.out.println("\nFile decrypted successfully: " + outputFile.getPath());
        }
    }

    public static void main(String[] args) throws Exception {
        Console console = System.console();
        if (console == null) {
            System.out.println("Couldn't get Console instance");
            return;
        }

        try {
            System.out.println("=== File Encryption Tool ===");

            // รับ password จากผู้ใช้งาน
            char[] passwordChars = console.readPassword("Enter password: ");
            String password = new String(passwordChars);
            Arrays.fill(passwordChars, '\0'); // Clear password from memory

            // สร้าง SecretKey
            SecretKey secretKey = generateKey(password);
            // String encodedKey = Base64.getEncoder().encodeToString(secretKey.getEncoded());
            // System.out.println("Key generated successfully (Base64): " + encodedKey);

            // // สร้าง IV เพื่อสุ่มการเข้ารหัสทุกครั้ง
            // IvParameterSpec iv = generateIv();
            // String encodedIv = Base64.getEncoder().encodeToString(iv.getIV());
            // System.out.println("IV generated successfully (Base64): " + encodedIv);
            
            System.out.print("Enter mode (encrypt/decrypt): ");
            String mode = console.readLine().toLowerCase();
            System.out.print("Enter input file path: ");
            String inputFilePath = console.readLine();
            System.out.print("Enter output file path: ");
            String outputFilePath = console.readLine();

            File inputFile = new File(inputFilePath);
            File outputFile = new File(outputFilePath);
            // encryptFile(inputFile, outputFile, secretKey);
            // decryptFile(inputFile, outputFile, secretKey);

            if (outputFile.exists()) {
                System.out.print("Output file exists. Overwrite? (Y/N): ");
                if (!console.readLine().equalsIgnoreCase("Y")) {
                    System.out.println("Operation cancelled");
                    return;
                }
            }

            if (mode.equals("encrypt")) {
                encryptFile(inputFile, outputFile, secretKey);
            } else if (mode.equals("decrypt")) {
                decryptFile(inputFile, outputFile, secretKey);
            } else {
                System.out.println("Invalid mode. Please enter 'encrypt' or 'decrypt'.");
            }

        } catch (Exception e) {
            System.out.println("\nAn error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
