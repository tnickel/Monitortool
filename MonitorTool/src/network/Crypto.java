package network;


import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Arrays;

public class Crypto {

    // Methode zur Ableitung eines AES-Schl�ssels aus einer Passphrase
    private SecretKey getKeyFromPassword(String password) throws Exception {
        // Passwort in Byte-Array umwandeln
        byte[] key = password.getBytes(StandardCharsets.UTF_8);
        // SHA-256-Hash des Passworts erstellen
        MessageDigest sha = MessageDigest.getInstance("SHA-256");
        key = sha.digest(key);
        // Nur die ersten 128 Bit (16 Bytes) verwenden
        key = Arrays.copyOf(key, 16);
        // SecretKeySpec mit dem AES-Algorithmus erstellen
        return new SecretKeySpec(key, "AES");
    }

    // Methode zur Verschl�sselung einer Datei
    public void encryptFile(String originalFile, String encryptedFile, String password) throws Exception {
        // AES-Schl�ssel aus dem Passwort ableiten
        SecretKey key = getKeyFromPassword(password);
        // Cipher-Instanz f�r AES/ECB/PKCS5Padding erstellen
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        // Cipher im Verschl�sselungsmodus initialisieren
        cipher.init(Cipher.ENCRYPT_MODE, key);

        // Dateien einlesen und verschl�sseln
        try (InputStream in = new FileInputStream(originalFile);
             CipherOutputStream out = new CipherOutputStream(new FileOutputStream(encryptedFile), cipher)) {
            byte[] byteBuffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(byteBuffer)) != -1) {
                out.write(byteBuffer, 0, bytesRead);
            }
        }
    }

    // Methode zur Entschl�sselung einer Datei
    public void decryptFile(String encryptedFile, String decryptedFile, String password) throws Exception {
        // AES-Schl�ssel aus dem Passwort ableiten
        SecretKey key = getKeyFromPassword(password);
        // Cipher-Instanz f�r AES/ECB/PKCS5Padding erstellen
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        // Cipher im Entschl�sselungsmodus initialisieren
        cipher.init(Cipher.DECRYPT_MODE, key);

        // Dateien einlesen und entschl�sseln
        try (CipherInputStream in = new CipherInputStream(new FileInputStream(encryptedFile), cipher);
             OutputStream out = new FileOutputStream(decryptedFile)) {
            byte[] byteBuffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(byteBuffer)) != -1) {
                out.write(byteBuffer, 0, bytesRead);
            }
        }
    }
}
