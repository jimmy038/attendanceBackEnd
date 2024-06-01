package com.example.attendance.vo;

import com.example.attendance.entity.Employee;

			 //EmployeeReq繼承Employee類別 即可拿到當中屬性
public class EmployeeCreateReq extends Employee{ //Employee員工  Req請求

	//產生一個 creatorId使用者ID
	private String creatorId;

	public String getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(String creatorId) {
		this.creatorId = creatorId;
	}


	
	
}
