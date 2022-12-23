package com.nv.commons.type;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public enum SiteNameType {

	Staging(0), DEV(1), LCS(2), Venus(3), LCSA(4), Validate(5), Promo(6);

	private final int unique;

	SiteNameType(int unique) {
		this.unique = unique;
	}

	public int unique() {
		return unique;
	}
	
	public final static List<SiteNameType> VALUES = Collections
		.unmodifiableList(Arrays.asList(SiteNameType.values()));

	private static int[] uniques = null;

	static {
		uniques = new int[VALUES.size()];
		for (int i = 0; i < VALUES.size(); i++) {
			uniques[i] = VALUES.get(i).unique;
		}
	}
	
	public static Optional<SiteNameType> get(int unqiue) {
		return VALUES.stream().filter(x -> x.unique() == unqiue).findFirst();
	}

	public static Optional<SiteNameType> get(String name) {
		return VALUES.stream().filter(x -> x.name().equalsIgnoreCase(name)).findFirst();
	}
}
