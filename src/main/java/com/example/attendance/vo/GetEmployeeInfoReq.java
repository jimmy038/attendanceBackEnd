package com.example.attendance.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GetEmployeeInfoReq {

	@JsonProperty("coller_id")//一般員工ID
	private String collerId;
	
	@JsonProperty("target_id")//部門主管ID
	private String targetId;

	public String getCollerId() {
		return collerId;
	}

	public void setCollerId(String collerId) {
		this.collerId = collerId;
	}

	public String getTargetId() {
		return targetId;
	}

	public void setTargetId(String targetId) {
		this.targetId = targetId;
	}
	
	
}
