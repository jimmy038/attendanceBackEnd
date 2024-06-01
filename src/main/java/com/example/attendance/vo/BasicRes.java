package com.example.attendance.vo;

import com.example.attendance.constants.RtnCode;

public class BasicRes { //≠ÏEmployeeCreateRes

	private RtnCode rtnCode;

	public BasicRes() {
		super();
	}

	public BasicRes(RtnCode rtnCode) {
		super();
		this.rtnCode = rtnCode;
	}

	public RtnCode getRtnCode() {
		return rtnCode;
	}

	public void setRtnCode(RtnCode rtnCode) {
		this.rtnCode = rtnCode;
	}
	
	
}
