package com.example.attendance.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.attendance.constants.RtnCode;
import com.example.attendance.service.ifs.EmployeeService;
import com.example.attendance.vo.BasicRes;
import com.example.attendance.vo.ChangePasswordReq;
import com.example.attendance.vo.EmployeeCreateReq;
import com.example.attendance.vo.EmployeeRes;
import com.example.attendance.vo.ForgotPasswordReq;
import com.example.attendance.vo.GetEmployeeInfoReq;
import com.example.attendance.vo.LoginReq;

@RestController
public class EmployeeController {
	
	//@RequestBody參數包成物件帶入 @RequestParam帶參數即可

	@Autowired  //Controller與Service做介接
	private EmployeeService service;
	
	//用PostMapping的login
	@PostMapping(value = "api/attendance/post_login")
	public BasicRes login(@RequestBody LoginReq req, HttpSession session) {
		//假設根據key值(id)取得對應的value值，等於等於null時表示未成功登入過
		if(session.getAttribute(req.getId()) == null) {
			return service.login(req.getId(),req.getPwd(), session);
		}
		//如果成功登入回傳RtnCode.SUCCESSFUL
		return new BasicRes(RtnCode.SUCCESSFUL);
	}
	
	//用GetMapping的login
	@GetMapping(value = "api/attendance/get_login1")
	public BasicRes login1(
			@RequestParam(value = "id")String id,//
			@RequestParam(value = "password")String pwd,//
			HttpSession session) {
		//假設根據key值(id)取得對應的value值，如果id等於等於null時表示未成功登入過-->要求登入帳號
		if(session.getAttribute(id) == null) {
			return service.login(id, pwd, session);
		}
		//如果成功登入回傳RtnCode.SUCCESSFUL
		return new BasicRes(RtnCode.SUCCESSFUL);
	}
	
	
	//登出，登出需要把原本暫存的資料清除()內要帶入HttpSession session，session屬於資料快取類的，快取類資料都會暫存在記憶體內
	@GetMapping(value = "api/attendance/logout")
	public BasicRes logout(HttpSession session) {
		//讓整個session(暫存)失效 session.invalidate
		session.invalidate(); 
		return new BasicRes(RtnCode.SUCCESSFUL);
	}
	
	
	//employee create 新增員工資料的API方法，假設現在要登入成功後才能使用新增的方法，
	@PostMapping(value = "api/attendance/employee/create")
	public BasicRes create(@RequestBody EmployeeCreateReq req, HttpSession session) {
		//第一個防呆防外部使用者打API進入，要有登入帳密才能新增資訊    req.getCreatorId()為登入的使用者ID
		if(session.getAttribute(req.getCreatorId()) == null ) {
			return new BasicRes(RtnCode.PLEASE_LOGIN_FIRST);
		}
		//第二個防呆防使用者權限進入，權限必須為Admin才能新增員工資料
		if(session.getAttribute(req.getCreatorId()).toString().equalsIgnoreCase("Admin")) {
			return new BasicRes(RtnCode.UNAUTHORIZATED);
		}
		return service.create(req);
	}
	
	
	//更改密碼
	@PostMapping(value = "api/attendance/employee/change_password")
	public BasicRes changePassword(@RequestBody ChangePasswordReq req, HttpSession session) {
		//要更改密碼請先登入
		if(session.getAttribute(req.getId()) == null ) {
			return new BasicRes(RtnCode.PLEASE_LOGIN_FIRST);
		}
		return service.changePassword(req.getId(), req.getOldPwd(), req.getNewPwd());
	}
	
	//忘記密碼
	@PostMapping(value = "api/attendance/employee/forgot_password")
	public BasicRes forgotPassword(@RequestBody ForgotPasswordReq req) {
		return service.forgotPassword(req.getId(), req.getEmail());
	}
	
	
	//更改密碼
	@PostMapping(value = "api/attendance/employee/change_password_by_auth_code")
	public BasicRes changePasswordByAuthCode(@RequestBody ChangePasswordReq req) {
		return service.changePasswordByAuthCode(req.getId(), req.getAuthCode(), req.getNewPwd());
	}
	
	
	//找員工自己的資料ID，只取得自己的資訊
	@PostMapping(value = "api/attendance/employee/get_info")
	public EmployeeRes findByEmployeeId(@RequestBody GetEmployeeInfoReq req, HttpSession session){
		//判斷是否登入成功過
		if(session.getAttribute(req.getCollerId()) == null ) {
			return new EmployeeRes(RtnCode.PLEASE_LOGIN_FIRST,null);
		}
		return service.findByEmployeeId(req.getCollerId());
	}
	
	
}
