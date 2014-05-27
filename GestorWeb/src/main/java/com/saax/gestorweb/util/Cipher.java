package com.saax.gestorweb.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import sun.misc.BASE64Encoder;


/**
 *
 * @author Rodrigo
 */
public class Cipher {

    private MessageDigest messageDigest;
    private BASE64Encoder encoder;

    private void useAlgortithm(String algorithm) throws NoSuchAlgorithmException {
        if (messageDigest == null || (messageDigest.getAlgorithm() == null ? algorithm != null : !messageDigest.getAlgorithm().equals(algorithm))) {
            messageDigest = MessageDigest.getInstance(algorithm);
        }
        if (encoder == null) {
            encoder = new BASE64Encoder();
        }

    }

    private String encryptByAlgorithm(String algorithm, String value) throws NoSuchAlgorithmException {
        if (value == null) {
            throw new IllegalArgumentException("Valor null");
        }

        useAlgortithm(algorithm);
        byte[] hash = messageDigest.digest(value.getBytes());
        return encoder.encode(hash);
    }

    public String md5Sum(String value) throws NoSuchAlgorithmException {
        return encryptByAlgorithm("MD5", value);
    }

}
