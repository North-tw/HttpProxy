package com.nv.test.listener;

import java.util.Optional;

import com.nv.commons.dto.GameEvent;
import com.nv.commons.dto.properties.DealerServerPropertiesView;
import com.nv.commons.listener.IEventListener;
import com.nv.commons.type.DealerServerType;
import com.nv.commons.type.GameEventType;
import com.nv.manager.ResultServerManager;
import com.nv.manager.cache.properties.DealerServerCache;
import com.nv.test.dto.TestBody;
import com.nv.test.dto.TestResultObj;
import com.nv.test.util.SingleTestUtils;
import com.nv.util.DelayTimeUtils;
import com.nv.util.JSONUtils;

public class TestEventListener implements IEventListener {

	@Override
	public void handleEvent(String body) throws Exception {

		TestBody message = JSONUtils.jsonToInstance(body, TestBody.class);
		DealerServerPropertiesView dealerServer = DealerServerCache.getInstance().get(message.getTableID());

		Optional<DealerServerType> serverTypeOp = DealerServerType
			.getInstanceOf(dealerServer.getDealerServerType());
		if (serverTypeOp.isPresent()) {

			long deliverTime = DelayTimeUtils.getDeliverTime(getGameEvent());

			TestResultObj result = new TestResultObj();
			result.setDealerId(message.getDealerId());
			result.setDealerSerialNo(message.getDealerId());
			result.setGameRound(message.getGameRound());
			result.setGameShoe(message.getGameShoe());
			result.setGameState((byte) 0);
			result.setiTime(5);
			result.setLockReason(1);
			result.setManagerId("999");
			result.setRoundStartTime(System.currentTimeMillis());
			result.setRoundEndTime(result.getRoundStartTime() + 5000);
			result.setUuId("1000");

			String data = result.getJSONObject(message.getTableID(), deliverTime);
			SingleTestUtils.getDataUtil().setIn(data);

			GameEvent gameEvent = new GameEvent(message.getTableID(), message.getGameShoe(),
				message.getGameRound(), getGameEvent(), deliverTime, data);

			ResultServerManager.getInstance().addEvent(gameEvent);
		}
	}

	@Override
	public GameEventType getGameEvent() {
		return GameEventType.GP_INIT;
	}

}
