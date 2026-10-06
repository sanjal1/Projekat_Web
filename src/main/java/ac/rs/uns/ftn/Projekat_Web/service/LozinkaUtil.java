package ac.rs.uns.ftn.Projekat_Web.service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LozinkaUtil {

    public static String hesuj(String lozinka) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(lozinka.getBytes());

            StringBuilder sb = new StringBuilder();
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Greška pri hešovanju lozinke.");
        }
    }

    public static boolean proveri(String unetaLozinka, String hesovanaLozinka) {
        return hesuj(unetaLozinka).equals(hesovanaLozinka);
    }
}
