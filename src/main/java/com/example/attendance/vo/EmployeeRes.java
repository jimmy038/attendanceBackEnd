package com.example.attendance.vo;


import com.example.attendance.constants.RtnCode;
import com.example.attendance.entity.Employee;

public class EmployeeRes extends BasicRes{

	private Employee employee;
	
	private RtnCode rtnCode;
	
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

	public EmployeeRes(RtnCode rtnCode) {
		super(rtnCode);
		// TODO Auto-generated constructor stub
	}

	public EmployeeRes(Employee employee, RtnCode rtnCode) {
		super();
		this.employee = employee;
		this.rtnCode = rtnCode;
	}

	public RtnCode getRtnCode() {
		return rtnCode;
	}

	public void setRtnCode(RtnCode rtnCode) {
		this.rtnCode = rtnCode;
	}
	
	
	
}
