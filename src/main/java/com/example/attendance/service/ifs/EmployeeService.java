package com.example.attendance.service.ifs;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Service;

import com.example.attendance.entity.Employee;
import com.example.attendance.vo.BasicRes;
import com.example.attendance.vo.EmployeeCreateReq;
import com.example.attendance.vo.EmployeeRes;

@Service
public interface EmployeeService {

	//�s�W���u���
	public BasicRes create(EmployeeCreateReq req); 
	 
	//�n�J 				  ���s        �K�X			 �[�JHttpSession session�A���Ȧs��ƥ�
	public BasicRes login(String id, String pwd, HttpSession session);
	
	//��K�X
	public BasicRes changePassword(String id,String oldPwd,String newPwd);
	
	//��AuthCode��K�X
	public BasicRes changePasswordByAuthCode(String id,String authCode, String newPwd);
	
	//�ѰO�K�X
	public BasicRes forgotPassword(String id,String email);
	
	//�b���ҥ�
	public BasicRes activate(String executroId,String employeeId);
	
	//�b������
	public BasicRes deactivate(String executroId,String employeeId);
	
	//�P�ɥi�F��A�b���ҥ� activate �M �b������ deactivate���ĪG
	public BasicRes updateActivate(String executroId,String employeeId, boolean isActivate);
	
	//��¾��k => �ק窱�A
	public BasicRes updateResign(String executroId, String employeeId);
	
	//�w�p�n��¾���u��g ��¾�ӽ�
	public BasicRes resignAppliction(String employeeId);
	
	//
	public BasicRes updatereInfo(String executroId, Employee employee);
	
	//��ۤv��ID
	public EmployeeRes findByEmployeeId(String employeeId);
	
	//����u��T��ID 							  �@����uID		  �����D��ID
	public EmployeeRes findByStaffInfo(String collerId,String targetId);

}
