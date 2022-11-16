package com.nv.util;

import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class EncryptUtils {

	private static final String TRANSFORMATION = "AES/CBC/PKCS5Padding";
	private static final String ALGORITHM = "AES";

	public static String decrypt(String IV, String cipherText, String encryptionKey) throws Exception {
		Cipher cipher = Cipher.getInstance(TRANSFORMATION, "SunJCE");
		SecretKeySpec key = new SecretKeySpec(getHash("MD5", encryptionKey), ALGORITHM);
		cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(getHash("MD5", IV)));
		return new String(cipher.doFinal(java.util.Base64.getDecoder().decode(cipherText)), "UTF-8");
	}

	private static byte[] getHash(final String algorithm, final String text) {
		try {
			return getHash(algorithm, text.getBytes("UTF-8"));
		} catch (final Exception ex) {
			throw new RuntimeException(ex.getMessage());
		}
	}

	private static byte[] getHash(final String algorithm, final byte[] data) {
		try {
			final MessageDigest digest = MessageDigest.getInstance(algorithm);
			digest.update(data);
			return digest.digest();
		} catch (final Exception ex) {
			throw new RuntimeException(ex.getMessage());
		}
	}
}