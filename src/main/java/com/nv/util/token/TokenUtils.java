package com.nv.util.token;

import com.nv.expandUtil.util.ExceptionUtils;
import com.nv.util.EncryptUtils;

public class TokenUtils {

	// Server Profile Token
	public final static String SERVER_PROFILE_TOKEN_ENCRYPT_KEY = "nJyWAfPOckz_i-pSTkqf";
	public final static String SERVER_PROFILE_TOKEN_IV_SPEC = "14363445425740998";

	public static String decryptServerProfile(String token) {
		try {
			return EncryptUtils.decrypt(TokenUtils.SERVER_PROFILE_TOKEN_IV_SPEC, token,
				TokenUtils.SERVER_PROFILE_TOKEN_ENCRYPT_KEY);
		} catch (Exception e) {
			throw ExceptionUtils.amendToUncheckedException("token=" + token, e);
		}
	}
}