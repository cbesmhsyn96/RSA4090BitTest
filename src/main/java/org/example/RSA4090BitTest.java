package org.example;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import javax.crypto.Cipher;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class RSA4090BitTest {
    private static final int KEY_SIZE = 4090;
    private static final int NUM_TESTS = 500;
    private static List<Long> times = new ArrayList<>();
    private static long total =0;
    public static KeyPair measureKeyGenerationTime() {
        long startTime = System.currentTimeMillis(); // Başlangıç zamanı

        KeyPair keyPair = null;
        try {
            // RSA anahtar çiftini oluştur
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(KEY_SIZE); // Anahtar uzunluğunu belirt
            keyPair = keyGen.generateKeyPair();
        } catch (Exception e) {
            e.printStackTrace();
        }

        long endTime = System.currentTimeMillis();
        long term = endTime-startTime;
        times.add(term);
        System.out.println("-------------------------------------------------------------------------------------------------------------------------------------");
        System.out.println("                                              Oluşturulma süresi :"+term+" ms");// Bitiş zamanı
        return keyPair; // Geçen süreyi milisaniye cinsinden döndür
    }
    public static void main(String[] args) {
        int sameCount = 0;  // Aynı olan testlerin sayısı
        int differentCount = 0; // Farklı olan testlerin sayısı
        try {
            for (int i = 0; i < NUM_TESTS; i++) {
                KeyPair keyPair = measureKeyGenerationTime();
                PublicKey publicKey = keyPair.getPublic();
                PrivateKey privateKey = keyPair.getPrivate();
                String originalText = "Hello, RSA 4090-bit!";
                Cipher cipher = Cipher.getInstance("RSA");
                cipher.init(Cipher.ENCRYPT_MODE, publicKey);
                byte[] encryptedBytes = cipher.doFinal(originalText.getBytes());
                String encryptedText = Base64.getEncoder().encodeToString(encryptedBytes);
                cipher.init(Cipher.DECRYPT_MODE, privateKey);
                byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedText));
                String decryptedText = new String(decryptedBytes);
                if (originalText.equals(decryptedText)) {
                    System.out.println((i+1)+". RSA testinde decript başarılı.                                                      "+ originalText+" = "+decryptedText);
                    sameCount++;
                } else {
                    System.out.println((i+1)+". RSA testinde decript başarılı.                                                      "+originalText+" != "+decryptedText);
                    differentCount++;
                }
                total = total + times.get(i);
            }
            System.out.println("-------------------------------------------------------------------------------------------------------------------------------------");
            System.out.println("                                              Ortalama anahtar oluşturma süresi "+(total/NUM_TESTS)+" ms.");
            System.out.println("                                              Aynı olan testlerin sayısı: " + sameCount);
            System.out.println("                                              Farklı olan testlerin sayısı: " + differentCount);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
