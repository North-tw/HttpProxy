package com.nv.commons.type;

import java.util.Arrays;
import java.util.Optional;

public enum DealerServerType {

	BaccaratAndDragonTiger(0),
	SicboAndFishPrawnCrab(1),
	Roulette(2),
	TeenPattiT20(3),
	TeenPattiOneDay(4),
	AndarBahar(5);
	
	private final static DealerServerType[] VALUES;

	static {
		int maxCode = Arrays.stream(DealerServerType.values()).mapToInt(e -> e.unique).max().getAsInt();
		
		VALUES = new DealerServerType[maxCode + 1];
		for (DealerServerType e : DealerServerType.values()) {
			VALUES[e.unique] = e;
		}
	}
	
	public static Optional<DealerServerType> getInstanceOf(int unique) {
		return Optional.ofNullable(VALUES[unique]);
	}
	
	private int unique;
	
	private DealerServerType(int unique) {
		this.unique = unique;
	}

	public int unique() {
		return unique;
	}
}
