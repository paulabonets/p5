package edu.comillas.icai.gitt.pat.spring.p5.util;

import org.springframework.stereotype.Component;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * TODO#14
 * Utiliza esta clase para guardar en BD y comparar los passwords de forma cifrada:
 * <a href="https://en.wikipedia.org/wiki/Cryptographic_hash_function#Password_verification">Password verification</a>
 * Para ello modifica las partes necesarias en el código creado anteriormente en UserService
 */
@Component
public class Hashing {

    /**
     * @param string cadena a hashear
     * @return cadena hasheada
     */
    public String hash(String string) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = digest.digest(string.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hashedBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("No se pudo aplicar SHA-256", e);
        }
    }

    /**
     * @param raw     contraseña sin cifrar introducida por el usuario
     * @param hashed  contraseña ya cifrada guardada en la base de datos
     * @return true si coinciden, false si no
     */
    public boolean check(String raw, String hashed) {
        return hash(raw).equals(hashed);
    }
}
