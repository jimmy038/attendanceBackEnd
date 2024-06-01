package com.example.attendance.vo;


import com.example.attendance.constants.RtnCode;
import com.example.attendance.entity.Employee;

public class EmployeeRes extends BasicRes{

	private Employee employee;

	public EmployeeRes() {
		super();
		// TODO Auto-generated constructor stub
	}

	public EmployeeRes(RtnCode rtnCode,Employee employee) {
		super(rtnCode);
		this.employee = employee;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}
	
	
	
}
