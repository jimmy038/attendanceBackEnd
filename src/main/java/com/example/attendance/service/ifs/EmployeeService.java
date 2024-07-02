package com.example.attendance.service.ifs;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Service;

import com.example.attendance.entity.Employee;
import com.example.attendance.vo.BasicRes;
import com.example.attendance.vo.EmployeeCreateReq;
import com.example.attendance.vo.EmployeeRes;

@Service
public interface EmployeeService {

	//新增員工資料
	public BasicRes create(EmployeeCreateReq req); 
	 
	//登入 				  員編        密碼			 加入HttpSession session，做暫存資料用
	public BasicRes login(String id, String pwd, HttpSession session);
	
	//改密碼
	public BasicRes changePassword(String id,String oldPwd,String newPwd);
	
	//用AuthCode改密碼
	public BasicRes changePasswordByAuthCode(String id,String authCode, String newPwd);
	
	//忘記密碼
	public BasicRes forgotPassword(String id,String email);
	
	//帳號啟用
	public BasicRes activate(String executroId,String employeeId);
	
	//帳號停用
	public BasicRes deactivate(String executroId,String employeeId);
	
	//同時可達到，帳號啟用 activate 和 帳號停用 deactivate的效果
	public BasicRes updateActivate(String executroId,String employeeId, boolean isActivate);
	
	//離職方法 => 修改狀態
	public BasicRes updateResign(String executroId, String employeeId);
	
	//預計要離職員工填寫 離職申請
	public BasicRes resignAppliction(String employeeId);
	
	//
	public BasicRes updatereInfo(String executroId, Employee employee);
	
	//找自己的ID
	public EmployeeRes findByEmployeeId(String employeeId);
	
	//找員工資訊的ID 							  一般員工ID		  部門主管ID
	public EmployeeRes findByStaffInfo(String collerId,String targetId);

}
