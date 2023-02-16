package com.nv.commons.type;

import java.util.Arrays;
import java.util.Optional;

import org.apache.commons.lang3.ArrayUtils;

public enum SiteNameType {

	Staging(0,"Staging"), 
	DEV(1,"DEV"), 
	LCS(2,"LCS"), 
	Venus(3,"Venus"), 
	LCSA(4,"LCSA"), 
	Validate(5,"Validate"), 
	Promo(6,"Promo");

	private int unique;
	private String name;

	SiteNameType(int unique, String name) {
		this.unique = unique;
		this.name = name;
	}

	public int unique() {
		return unique;
	}

	public String getName() {
		return name;
	}

	public final static SiteNameType[] VALUES;

	static {
		int maxCode = Arrays.stream(SiteNameType.values()).mapToInt(e -> e.unique).max().getAsInt();

		VALUES = new SiteNameType[maxCode + 1];
		for (SiteNameType e : SiteNameType.values()) {
			VALUES[e.unique] = e;
		}
	}

	public static Optional<SiteNameType> getInstanceOf(int unqiue) {
		if (ArrayUtils.isArrayIndexValid(VALUES, unqiue)) {
			return Optional.empty();
		} else {
			return Optional.ofNullable(VALUES[unqiue]);
		}
	}

	public static Optional<SiteNameType> getInstanceOf(String name) {
		return Arrays.stream(VALUES).filter(x -> x.getName().equalsIgnoreCase(name)).findFirst();
	}
}
