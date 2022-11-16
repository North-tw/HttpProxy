package com.nv.commons.model.database;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

import com.nv.util.ExceptionUtils;
import com.nv.util.LogUtils;
import com.nv.util.token.TokenUtils;

public class RedisManager {

	private static final RedisManager redisManager = new RedisManager();
	private static RedissonClient redissonClient;

	private RedisManager() {
		super();
	}

	/**
	 * 用於AppInitListener預先init
	 */
	public static void init() {
		try {
			redisManager.initClient();
		} catch (Exception e) {
			LogUtils.err.error("RedisManager", e);
			throw ExceptionUtils.amendToUncheckedException(e);
		}
	}

	public static RedissonClient get() {
		return redissonClient;
	}

	public static void shutdown() {
		if (redissonClient != null) {
			redissonClient.shutdown();
		}
	}

	public void initClient() {
		try (InputStream in = RedisManager.class.getResourceAsStream("/redis_encrypt.property")) {
			Config config = Config
				.fromYAML(TokenUtils.decryptServerProfile(IOUtils.toString(in, StandardCharsets.UTF_8)));
			redissonClient = Redisson.create(config);
			LogUtils.system.info("Codec:" + config.getCodec());
		} catch (Exception e) {
			LogUtils.err.error("RedisManager", e);
			throw ExceptionUtils.amendToUncheckedException(e);
		}
	}
}