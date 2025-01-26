import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.util.Arrays;
import java.io.Console;

public class fed {
    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";

    // สร้าง SecretKey จาก password
    public static SecretKey generateKey(String password) throws Exception {
        byte[] key = MessageDigest.getInstance("SHA-256").digest(password.getBytes("UTF-8"));
        return new SecretKeySpec(Arrays.copyOf(key, 16), "AES");
    }

    public static void main(String[] args) throws Exception {
        Console console = System.console();
        if (console == null) {
            System.out.println("Couldn't get Console instance");
            return;
        }
        
        try {
            System.out.println("=== File Encryption Tool ===");

            // รับ passwordจากผู้ใช้งาน
            char[] passwordChars = console.readPassword("Enter password: ");
            String password = new String(passwordChars);
            Arrays.fill(passwordChars, '\0'); 

            // สร้าง SecretKey
            SecretKey secretKey = generateKey(password);
            System.out.println("Key generated successfully: " + secretKey);

        } catch (Exception e) {
            System.out.println("\nAn error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
