package com.example.attendance.vo;

import java.util.List;

import com.example.attendance.entity.Departments;

public class DepartmentsCreateReq { //Req�ШD

	private List<Departments> depList;  //��List �]���@���Q�s�W�h�ӳ���

	public DepartmentsCreateReq() {
		super();
		// TODO Auto-generated constructor stub
	}

	public DepartmentsCreateReq(List<Departments> depList) {
		super();
		this.depList = depList;
	}

	public List<Departments> getDepList() {
		return depList;
	}

	public void setDepList(List<Departments> depList) {
		this.depList = depList;
	}

	
		
}