package com.nv.test.dto;

import java.io.IOException;
import java.io.StringWriter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.nv.commons.dto.result.BaseResultObj;
import com.nv.util.JSONUtils;

public class TestResultObj extends BaseResultObj {

	public String getJSONObject(int tableId, long deliverTime) throws IOException {

		try (StringWriter out = new StringWriter();
			JsonGenerator jGenerator = JSONUtils.getFactory().createGenerator(out)) {

			jGenerator.writeStartObject();
			jGenerator.writeNumberField("tableID", tableId);
			jGenerator.writeStringField("dealerId", this.getDealerId());
			jGenerator.writeStringField("dealerSerialNo", this.getDealerSerialNo());
			jGenerator.writeStringField("managerId", this.getManagerId());
			jGenerator.writeNumberField("roundStartTime", this.getRoundStartTime());
			jGenerator.writeNumberField("roundEndTime", this.getRoundEndTime());
			jGenerator.writeNumberField("gameRound", this.getGameRound());
			jGenerator.writeNumberField("gameShoe", this.getGameShoe());
			jGenerator.writeNumberField("iTime", this.getiTime());
			jGenerator.writeNumberField("gameState", this.getGameState());
			jGenerator.writeNumberField("lockReason", this.getLockReason());
			jGenerator.writeNumberField("deliverTime", deliverTime);
			jGenerator.writeStringField("uuId", this.getUuId());
			jGenerator.writeEndObject();
			jGenerator.flush();
			return out.toString();
		}
	}
}
