package com.nv.manager;

import java.io.File;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;

import com.ip2location.IP2Location;
import com.ip2location.IPResult;
import com.nv.commons.constant.SystemConstant;
import com.nv.commons.dto.LocationElement;
import com.nv.util.LogUtils;
import com.nv.util.Validator;

public class CountryLookup {

	private static CountryLookup instance = new CountryLookup();

	public static CountryLookup getInstance() {
		return instance;
	}

	private final String IP_SPLIT_KEY = ":";

	private IP2Location ip2l = null;

	private CountryLookup() {
		super();
	}

	public void init() {
		File bin = new File(System.getProperty("catalina.base") + File.separator + "lib" + File.separator
			+ "IPV6-COUNTRY-REGION-CITY-ISP.BIN");
		File license = new File(
			System.getProperty("catalina.base") + File.separator + "lib" + File.separator + "license.key");

		if (bin.exists() && license.exists()) {
			ip2l = new IP2Location();
			ip2l.IPDatabasePath = bin.getAbsolutePath();
			ip2l.IPLicensePath = license.getAbsolutePath();
			LogUtils.system.info("[IP2Locaiton][INIT]loc.IPDatabasePath=" + ip2l.IPDatabasePath);
			LogUtils.system.info("[IP2Locaiton][INIT]loc.IPLicensePath=" + ip2l.IPLicensePath);
			return;
		}
		bin = new File(System.getProperty("catalina.home") + File.separator + "lib" + File.separator
			+ "IPV6-COUNTRY-REGION-CITY-ISP.BIN");
		license = new File(
			System.getProperty("catalina.home") + File.separator + "lib" + File.separator + "license.key");
		if (bin.exists() && license.exists()) {
			ip2l = new IP2Location();
			ip2l.IPDatabasePath = bin.getAbsolutePath();
			ip2l.IPLicensePath = license.getAbsolutePath();
			LogUtils.system.info("[IP2Locaiton][INIT]loc.IPDatabasePath=" + ip2l.IPDatabasePath);
			LogUtils.system.info("[IP2Locaiton][INIT]loc.IPLicensePath=" + ip2l.IPLicensePath);
			return;
		}
		throw new RuntimeException("IP2Location init fail: files not exist");
	}

	// for test
	public void update(IP2Location ip2l) {
		try {
			this.ip2l = ip2l;
			LogUtils.system.info("[IP2Locaiton][INIT]loc.IPDatabasePath=" + ip2l.IPDatabasePath);
			LogUtils.system.info("[IP2Locaiton][INIT]loc.IPLicensePath=" + ip2l.IPLicensePath);
		} catch (Exception ex) {
			LogUtils.system.error("IP2Location init fail.", ex);
			this.ip2l = null;
		}
	}

	/**
	 * Get Country Code By New IP2Location
	 * 
	 * @param ip
	 * @return String CountryShort 不符合格式或無法解析的IP，一律回傳XX
	 */
	public String getCountryV2(String ip) {
		return Optional.of(ip).filter(x -> StringUtils.indexOf(x, IP_SPLIT_KEY) == -1)
			.filter(x -> Validator.isIpAddress(x)).map(x -> {
				try {
					return Optional.ofNullable(ip2l.IPQuery(x)).map(y -> y.getCountryShort())
						.orElse(SystemConstant.NO_COUNTRY_RESULT);
				} catch (Exception e) {
					LogUtils.err.error("new>>ip=" + ip + " lookup fail.", e);
					return SystemConstant.NO_COUNTRY_RESULT;
				}
			}).orElse(SystemConstant.NO_COUNTRY_RESULT);
	}

	public String getCity(String ip) {
		if (ip.indexOf(IP_SPLIT_KEY) > -1) {
			return SystemConstant.NO_REGION_RESULT;
		}
		try {
			IPResult result = ip2l.IPQuery(ip);
			if (result == null) {
				return SystemConstant.NO_CITY_RESULT;
			} else {
				return result.getCity();
			}
		} catch (Exception ex) {
			LogUtils.err.error("new>>ip=" + ip + " lookup fail.", ex);
		}
		return SystemConstant.NO_CITY_RESULT;
	}

	public LocationElement getLocationAttributes(String ip) {
		if (ip.indexOf(IP_SPLIT_KEY) > -1) {
			return new LocationElement(SystemConstant.NO_COUNTRY_RESULT, SystemConstant.NO_REGION_RESULT,
				SystemConstant.NO_CITY_RESULT);
		}
		try {
			IPResult result = ip2l.IPQuery(ip);

			if (result == null) {
				return new LocationElement(SystemConstant.NO_COUNTRY_RESULT, SystemConstant.NO_REGION_RESULT,
					SystemConstant.NO_CITY_RESULT);
			}
			return new LocationElement(result.getCountryShort(), result.getRegion(), result.getCity());
		} catch (Exception ex) {
			LogUtils.err.error("new>>ip=" + ip + " lookup fail.", ex);
		}
		return new LocationElement(SystemConstant.NO_COUNTRY_RESULT, SystemConstant.NO_REGION_RESULT,
			SystemConstant.NO_CITY_RESULT);
	}
}
