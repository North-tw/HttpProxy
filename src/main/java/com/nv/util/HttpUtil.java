package com.nv.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

public class HttpUtil {

	private HttpUtil() {
	}

	/**
	 * 找出本機上與 pattern 相符的 InetAddress.
	 *
	 * @param pattern
	 * @return
	 */
	public static InetAddress findInetAddress(String pattern) {
		InetAddress result = null;
		if (pattern != null && !pattern.isEmpty()) {
			for (InetAddress address : retrieveAllInetAddresses()) {
				if (address.getHostAddress().startsWith(pattern)) {
					result = address;
					break;
				}
			}
		}
		return result;
	}

	/**
	 * 找出本機上某張網卡與 pattern 相符的 InetAddress.
	 *
	 * @param networkName
	 * @param pattern
	 * @return
	 */
	public static InetAddress findInetAddress(String networkName, String pattern) {
		InetAddress result = null;
		NetworkInterface ni = null;
		if (networkName != null && !networkName.isEmpty()) {
			try {
				ni = NetworkInterface.getByName(networkName);
			} catch (SocketException ex) {
				LogUtils.gameErrorLogger.error(ex.getMessage(), ex);
			}
		}
		if (ni != null) {
			for (InetAddress address : Collections.list(ni.getInetAddresses())) {
				if (address.getHostAddress().startsWith(pattern)) {
					result = address;
					break;
				}
			}
		}
		return result;
	}

	/**
	 * 找出本機所有的 InetAddress.
	 *
	 * @return
	 */
	private static List<InetAddress> retrieveAllInetAddresses() {
		List<InetAddress> addresses = new ArrayList<>();
		try {
			Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();
			for (NetworkInterface nInterface : Collections.list(nets)) {
				addresses.addAll(Collections.list(nInterface.getInetAddresses()));
			}
		} catch (SocketException ex) {
			LogUtils.gameErrorLogger.error(ex.getMessage(), ex);
		}
		return addresses;
	}
}