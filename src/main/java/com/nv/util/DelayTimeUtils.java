package com.nv.util;

import java.util.concurrent.TimeUnit;

import com.nv.commons.dto.properties.DealerProxyPropertiesView;
import com.nv.commons.type.GameEventType;
import com.nv.manager.cache.properties.DealerProxyCache;

public class DelayTimeUtils {

	public static void delayEvent(GameEventType eventType) {
		DealerProxyPropertiesView dealerProxyProperties = DealerProxyCache.getInstance().get();
		if (dealerProxyProperties.getSendDataPending() > 0) {
			try {
				TimeUnit.MILLISECONDS.sleep(dealerProxyProperties.getSendDataPending());
			} catch (InterruptedException e) {
				LogUtils.err.error(e.getMessage(), e);
			}
		}
		if (dealerProxyProperties.getSendWinnerDataPending() > 0 && GameEventType.GP_WINNER == eventType) {
			try {
				TimeUnit.MILLISECONDS.sleep(dealerProxyProperties.getSendWinnerDataPending());
			} catch (InterruptedException e) {
				LogUtils.err.error(e.getMessage(), e);
			}
		}
	}

	public static long getDeliverTime(GameEventType eventType) {
		DealerProxyPropertiesView dealerProxyProperties = DealerProxyCache.getInstance().get();
		long time = System.currentTimeMillis();
		if (dealerProxyProperties.getSendDataPending() > 0) {
			time += dealerProxyProperties.getSendDataPending();
		}
		if (dealerProxyProperties.getSendWinnerDataPending() > 0 && GameEventType.GP_WINNER == eventType) {
			time += dealerProxyProperties.getSendWinnerDataPending();
		}
		return time;
	}

	public static int getCalcBetTime(int originalBetTime) {
		DealerProxyPropertiesView dealerProxyProperties = DealerProxyCache.getInstance().get();
		int calcBetTime = originalBetTime;
		if (dealerProxyProperties.getSendDataPending() > 0
			&& dealerProxyProperties.getAdditionalBetTime() > 0) {
			calcBetTime = originalBetTime - ((int) dealerProxyProperties.getSendDataPending() / 1000
				- dealerProxyProperties.getAdditionalBetTime());
		}
		return calcBetTime;
	}

	public static int getCalcInsuranceBetTime(int originalInsuranceBetTime) {
		DealerProxyPropertiesView dealerProxyProperties = DealerProxyCache.getInstance().get();
		int calcInsuranceBetTime = originalInsuranceBetTime;
		if (dealerProxyProperties.getSendDataPending() > 0
			&& dealerProxyProperties.getAdditionalInsuranceBetTime() > 0) {
			calcInsuranceBetTime = originalInsuranceBetTime
				- ((int) dealerProxyProperties.getSendDataPending() / 1000
					- dealerProxyProperties.getAdditionalInsuranceBetTime());
		}
		return calcInsuranceBetTime;
	}
}
