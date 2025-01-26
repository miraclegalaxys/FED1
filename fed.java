import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Base64;
import java.io.Console;
import javax.crypto.spec.IvParameterSpec;
import java.security.SecureRandom;

public class fed {
    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";
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
            String encodedKey = Base64.getEncoder().encodeToString(secretKey.getEncoded());
            System.out.println("Key generated successfully (Base64): " + encodedKey);

            // สร้าง IV
            IvParameterSpec iv = generateIv();
            String encodedIv = Base64.getEncoder().encodeToString(iv.getIV());
            System.out.println("IV generated successfully (Base64): " + encodedIv);

        } catch (Exception e) {
            System.out.println("\nAn error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
