package com.nv.test;

import com.nv.commons.dto.Record;
import com.nv.expandUtil.util.JSONUtils;

public class TestParserRecord {

	public static void main(String[] args) {
		String str = "{\"project\":\"LCS\",\"websiteIdx\":0,\"website\":\"Home\",\"userId\":\"000tony01\"}";
		Record record = JSONUtils.jsonToInstance(str, Record.class);
		System.out.println(record.getProject());
		System.out.println(record.getWebsite());
		System.out.println(record.getUserId());
	}
}
