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

	@Autowired  //Controller�PService������
	private EmployeeService service;
	
	//��PostMapping��login
	@PostMapping(value = "api/attendance/login")
	public BasicRes login(@RequestBody LoginReq req, HttpSession session) {
		//���]�ھ�key��(id)���o������value�ȡA���󵥩�null�ɪ�ܥ����\�n�J�L
		if(session.getAttribute(req.getId()) == null) {
			return service.login(req.getId(),req.getPwd(), session);
		}
		//�p�G���\�n�J�^��RtnCode.SUCCESSFUL
		return new BasicRes(RtnCode.SUCCESSFUL);
	}
	
	//��GetMapping��login
	@GetMapping(value = "api/attendance/login1")
	public BasicRes login1(
			@RequestParam(value = "id")String id,//
			@RequestParam(value = "password")String pwd,//
			HttpSession session) {
		//���]�ھ�key��(id)���o������value�ȡA�p�Gid���󵥩�null�ɪ�ܥ����\�n�J�L-->�n�D�n�J�b��
		if(session.getAttribute(id) == null) {
			return service.login(id, pwd, session);
		}
		//�p�G���\�n�J�^��RtnCode.SUCCESSFUL
		return new BasicRes(RtnCode.SUCCESSFUL);
	}
	
	
	//�n�X�A�n�X�ݭn��쥻�Ȧs����ƲM��()���n�a�JHttpSession session�Asession�ݩ��Ƨ֨������A�֨�����Ƴ��|�Ȧs�b�O���餺
	@GetMapping(value = "api/attendance/logout")
	public BasicRes logout(HttpSession session) {
		//�����session(�Ȧs)���� session.invalidate
		session.invalidate(); 
		return new BasicRes(RtnCode.SUCCESSFUL);
	}
	
	
	//employee create �s�W���u��ƪ�API��k�A���]�{�b�n�n�J���\��~��ϥηs�W����k�A
	@PostMapping(value = "api/attendance/employee/create")
	public BasicRes create(@RequestBody EmployeeCreateReq req, HttpSession session) {
		//�Ĥ@�Ө��b���~���ϥΪ̥�API�i�J�A�n���n�J�b�K�~��s�W��T    req.getCreatorId()���n�J���ϥΪ�ID
		if(session.getAttribute(req.getCreatorId()) == null ) {
			return new BasicRes(RtnCode.PLEASE_LOGIN_FIRST);
		}
		//�ĤG�Ө��b���ϥΪ��v���i�J�A�v��������Admin�~��s�W���u���
		if(session.getAttribute(req.getCreatorId()).toString().equalsIgnoreCase("Admin")) {
			return new BasicRes(RtnCode.UNAUTHORIZATED);
		}
		return service.create(req);
	}
	
	
	//���K�X
	@PostMapping(value = "api/attendance/employee/change_password")
	public BasicRes changePassword(@RequestBody ChangePasswordReq req, HttpSession session) {
		//�n���K�X�Х��n�J
		if(session.getAttribute(req.getId()) == null ) {
			return new BasicRes(RtnCode.PLEASE_LOGIN_FIRST);
		}
		return service.changePassword(req.getId(), req.getOldPwd(), req.getNewPwd());
	}
	
	//�ѰO�K�X
	@PostMapping(value = "api/attendance/employee/forgot_password")
	public BasicRes forgotPassword(@RequestBody ForgotPasswordReq req) {
		return service.forgotPassword(req.getId(), req.getEmail());
	}
	
	
	//���K�X
	@PostMapping(value = "api/attendance/employee/change_password_by_auth_code")
	public BasicRes changePasswordByAuthCode(@RequestBody ChangePasswordReq req) {
		return service.changePasswordByAuthCode(req.getId(), req.getAuthCode(), req.getNewPwd());
	}
	
	
	//����u�ۤv�����ID�A�u���o�ۤv����T
	@PostMapping(value = "api/attendance/employee/get_info")
	public EmployeeRes findByEmployeeId(@RequestBody GetEmployeeInfoReq req, HttpSession session){
		//�P�_�O�_�n�J���\�L
		if(session.getAttribute(req.getCollerId()) == null ) {
			return new EmployeeRes(RtnCode.PLEASE_LOGIN_FIRST,null);
		}
		return service.findByEmployeeId(req.getCollerId());
	}
	
	
}
