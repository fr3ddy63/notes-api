package de.home.micronaut.rest.notes.common;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.inject.Singleton;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;
import java.util.Base64;

@Singleton
public class PasswordHash {

    private final Integer keyLength = 128;
    private final SecretKeyFactory factory;

    public PasswordHash() throws NoSuchAlgorithmException {
        this.factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
    }

    public String generate(String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        int iterations = 65536;
        byte[] salt = getSalt();

        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, iterations, keyLength);
        byte[] hash = this.factory.generateSecret(spec).getEncoded();

        return iterations + ":"
                + Base64.getEncoder().encodeToString(salt) + ":"
                + Base64.getEncoder().encodeToString(hash);
    }

    public boolean validate(String original, String stored) throws NoSuchAlgorithmException, InvalidKeySpecException {
        String[] parts = stored.split(":");
        int iterations = Integer.parseInt(parts[0]);
        byte[] salt = Base64.getDecoder().decode(parts[1]);
        byte[] hash = Base64.getDecoder().decode(parts[2]);

        KeySpec spec = new PBEKeySpec(original.toCharArray(), salt, iterations, keyLength);
        byte[] originalHash = this.factory.generateSecret(spec).getEncoded();

        return Arrays.equals(hash, originalHash);
    }

    private byte[] getSalt() throws NoSuchAlgorithmException {
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return salt;
    }
}
