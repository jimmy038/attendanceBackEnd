package com.example.attendance.vo;

import java.util.List;

import com.example.attendance.entity.Departments;

public class DepartmentsCreateReq { //Req請求

	private List<Departments> depList;  //用List 因為一次想新增多個部門

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
