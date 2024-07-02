package com.example.attendance;


import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import com.example.attendance.entity.Departments;
import com.example.attendance.service.ifs.DepartmentsService;
import com.example.attendance.vo.DepartmentsCreateReq;
import com.example.attendance.vo.DepartmentsCreateRes;
import com.example.attendance.vo.BasicRes;

@SpringBootTest
public class DepartmentsTest {

	@Autowired
	private DepartmentsService service;
	
	//only for initial(初始建立部門使用)
	@Test
	public void adminDepTest() {
		//方法帶參數
		Departments dep = new Departments("99","ADMIN");
		DepartmentsCreateReq req = new DepartmentsCreateReq(Arrays.asList(dep));
		DepartmentsCreateRes res = service.create(req);
		Assert.isTrue(res.getRtnCode().getCode() == 200,"Departments create error!!");
	}
	
	
	@Test
	public void createDepTest() {
		DepartmentsCreateReq req = new DepartmentsCreateReq(Arrays.asList(
				new Departments("01", "HR"),new Departments("02", "IT")));
		DepartmentsCreateRes  res = service.create(req);
		Assert.isTrue(res.getRtnCode().getCode() == 200, "Departments Create error!!" );
	}
	
}
