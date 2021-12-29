package com.nv.commons.dto;

public class LocationElement {

	private String country;

	private String city;

	private String region;

	public LocationElement(String country, String region, String city) {
		super();

		this.country = country;
		this.region = region;
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public String getCity() {
		return city;
	}

	public String getRegion() {
		return region;
	}
}