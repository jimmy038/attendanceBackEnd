package com.example.attendance.vo;

import com.example.attendance.entity.Employee;

			 //EmployeeReq�~��Employee���O �Y�i������ݩ�
public class EmployeeCreateReq extends Employee{ //Employee���u  Req�ШD

	//���ͤ@�� creatorId�ϥΪ�ID
	private String creatorId;

	public String getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(String creatorId) {
		this.creatorId = creatorId;
	}


	
	
}
