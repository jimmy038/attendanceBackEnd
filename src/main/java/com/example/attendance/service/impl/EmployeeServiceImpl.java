package com.example.attendance.service.impl;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import javax.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.example.attendance.constants.JobPosition;
import com.example.attendance.constants.RtnCode;
import com.example.attendance.entity.AuthCode;
import com.example.attendance.entity.Employee;
import com.example.attendance.entity.ResignApplication;
import com.example.attendance.repository.AuthCodeDao;
import com.example.attendance.repository.DepartmentsDao;
import com.example.attendance.repository.EmployeeDao;
import com.example.attendance.repository.ResignApplicationDao;
import com.example.attendance.service.ifs.EmployeeService;
import com.example.attendance.vo.EmployeeCreateReq;
import com.example.attendance.vo.EmployeeRes;

import net.bytebuddy.utility.RandomString;

import com.example.attendance.vo.BasicRes;

@Service
public class EmployeeServiceImpl implements EmployeeService{
	
	//直接宣告成private,在下面再使用encoder變數,就不用重新new 設定密碼改為亂碼
	private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

	@Value("${authcode.expired.time}")
	private int authCodeExpiredTime;
	
	@Autowired
	private EmployeeDao dao;
	
	@Autowired
	private DepartmentsDao departmentsDao; 
	
	@Autowired
	private AuthCodeDao authCodeDao;
	
	@Autowired
	private ResignApplicationDao resignApplicationDao;
	
	//import Logger slf4j這個 ，LoggerFactory也是import slf4j這個，Logger用在覺得會出錯的地方或是用在覺得有用的資訊
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Override //新增員工資料
	public BasicRes create(EmployeeCreateReq req) {
		//檢查這些必填資料不得為空
		if(!StringUtils.hasText(req.getId()) 
				|| !StringUtils.hasText(req.getDepartment())
				|| !StringUtils.hasText(req.getName()) 
				|| !StringUtils.hasText(req.getPwd())
				|| !StringUtils.hasText(req.getEmail()) 
				|| !StringUtils.hasText(req.getJobPosition())
				|| req.getBirthDate() == null 
				|| req.getArrivalDate() == null) {
				return new BasicRes(RtnCode.PARAM_ERROR);
		}
		//檢查ID 假設ID存在
		if(dao.existsById(req.getId())) {
			return new BasicRes(RtnCode.ID_HAS_EXISTED);
		}
		//check 檢查 departments_name -> 要確認帶入部門名稱是否存在
		if(departmentsDao.existsByName(req.getDepartment())) {
			return new BasicRes(RtnCode.DEPARTMENTS_NOT_FOUND);
		} 
		//密碼加密改為亂碼 加密後存入
		req.setPwd(encoder.encode(req.getPwd()));
		//覺得可能出錯的地方↓會用到Logger
		try {
			dao.save((Employee)req); 
		} catch (Exception e) {
			//記錄一般資訊用logger.info 錯誤用logger.error
			logger.error(e.getMessage()); //將錯誤訊息記錄到logger內
			return new BasicRes(RtnCode.EMPLOYEE_CREATE_ERROR);
		}
		return new BasicRes(RtnCode.SUCCESSFUL);
	}

	
	@Override //登入
	public BasicRes login(String id, String pwd, HttpSession session) {
		//檢查參數(檢查帳密)，沒有帳號密碼時，回傳RtnCode.PARAM_ERROR
		if(!StringUtils.hasText(id) || !StringUtils.hasText(pwd)) {
			return new BasicRes(RtnCode.PARAM_ERROR);
		}
		//檢查ID 跟 PWD
		Optional<Employee> op = dao.findById(id);
		//假設op為空 為空時回傳RtnCode.ID_NOT_FOUND
		if(op.isEmpty()) {
			return new BasicRes(RtnCode.ID_NOT_FOUND);
		}
		//如果有時ID 取出ID
		Employee employee = op.get();
		//假設比對沒有matches的話 matches(輸入的原密碼，比對加密後的密碼(資料庫內的密碼))
		if(!encoder.matches(pwd, employee.getPwd())) {
			return new BasicRes(RtnCode.PASSWORD_ERROR);
		}
		//確認帳號Active狀態停用
		if(!employee.getActive()) {
			return new BasicRes(RtnCode.ACCOUNT_DEACTIVATE);
		}
		//存入帳密在HttpSession session(暫存)
		session.setAttribute(id, employee.getDepartment());//("A01",部門IT)
		//設定時間暫存登入時間，預設為30分鐘1800秒，預設單位為秒
		session.setMaxInactiveInterval(3000); //設定3000秒10分鐘
		//記錄一般資訊用logger.info 錯誤用logger.error
		logger.info("login successful");
		return new BasicRes(RtnCode.SUCCESSFUL);
	}


	@Override //更改密碼
	public BasicRes changePassword(String id, String oldPwd, String newPwd) {
		if(!StringUtils.hasText(id) || !StringUtils.hasText(oldPwd) || !StringUtils.hasText(newPwd)) {
			return new BasicRes(RtnCode.PARAM_ERROR);
		}
		//判斷新的密碼不能與舊密碼相同，equals兩個值比對，舊密碼比對新密碼
		if(oldPwd.equals(newPwd)){
			//如果舊密碼與新的密碼相同拋錯誤訊息回去
			return new BasicRes(RtnCode.OLD_PASSWORD_AND_NEW_PASSWORD_ARE_IDENITCAL);
		}
		//不用判斷是否為空，因為此方法必須是login之後才能使用，在login方法已有做判斷
		Employee employee = dao.findById(id).get();
		//
		if(!encoder.matches(oldPwd, employee.getPwd())) {
			return new BasicRes(RtnCode.PASSWORD_ERROR);
		}
		//設定新密碼為加密後的密碼
		employee.setPwd(encoder.encode(newPwd));
		try {		//try catch抓取是否有錯誤，若沒錯就執行try裡面的，錯誤跑進catch
			//存入新密碼
			dao.save(employee);
		} catch (Exception e) {
			logger.error(e.getMessage()); //將錯誤訊息記錄到logger內
			return new BasicRes(RtnCode.CHANGE_PASSWORD_ERROR);
		}
		return new BasicRes(RtnCode.SUCCESSFUL);
	}


	@Override //忘記密碼的方法
	public BasicRes forgotPassword(String id, String email) {
		//檢查ID或email最少一定要有一個有值
		if(!StringUtils.hasText(id) && !StringUtils.hasText(email)) {
			return new BasicRes(RtnCode.PARAM_ERROR);
		}
		//設為null，這麼做是為了確保即使沒有符合的條件找到相對應的 Employee 物件，變數 employee 也有一個初始值。這樣做的目的是為了在後續的程式碼中進行判斷和避免潛在的空指針異常。
		Employee employee = null;
		//假設ID有值
		if(StringUtils.hasText(id)) {
			Optional<Employee> op = dao.findById(id);
			//判斷ID如果為空回傳ID_NOT_FOUND
			if(op.isEmpty()) {
				return new BasicRes(RtnCode.ID_NOT_FOUND);
			}
			employee = op.get();
		}else {
			employee = dao.findByEmail(email);
			if(employee == null) {
				return new BasicRes(RtnCode.ID_NOT_FOUND);
			}
		}
		//RandomString.make產生包含大小寫英文數字都有的亂數，()內的12為產生12碼的亂數的密碼
		String randomPwd = RandomString.make(12);
		employee.setPwd(encoder.encode(randomPwd));
		//產生驗證碼，產生6碼的驗證碼，有效時間30分鐘
		String authCode = RandomString.make(6);
		LocalDateTime now = LocalDateTime.now();
		try {
			//儲存employee資料
			dao.save(employee);
			//儲存ID 驗證碼 現在時間到AuthCode							加上有效時間30分鐘
			authCodeDao.save(new AuthCode(employee.getId(),authCode,now.plusMinutes(authCodeExpiredTime)));
		} catch (Exception e) {
			logger.error(e.getMessage());
			return new BasicRes(RtnCode.FORGOT_PASSWORD_ERROR);
		}
		//介接 email並寄送email(把randomPwd傳入email)(要補上)
		return new BasicRes(RtnCode.SUCCESSFUL);
	}


	@Override
	public BasicRes changePasswordByAuthCode(String id, String authCode,String newPwd) {
		if(!StringUtils.hasText(id) || !StringUtils.hasText(authCode) || !StringUtils.hasText(newPwd)) {
			return new BasicRes(RtnCode.PARAM_ERROR);
		}
		//檢查AuthCode
		Optional<AuthCode> op = authCodeDao.findById(id);
		if(op.isEmpty()) {
			return new BasicRes(RtnCode.ID_NOT_FOUND);
		}
		AuthCode authCodeEntity = op.get();
		if(!authCodeEntity.getAuthCode().equals(authCode)) {
			return new BasicRes(RtnCode.AUTH_CODE_NOT_MATCHED);
		}
		//檢查當前時間跟DB內時間，判斷驗證碼時效30分鐘
		LocalDateTime now = LocalDateTime.now();
		//假設當前時間在有效時間30分鐘之後
		if(now.isAfter(authCodeEntity.getAuthDatetime())) {
			return new BasicRes(RtnCode.AUTH_CODE_EXPIRED);
		}
		//用ID把員工資料撈回來
		Employee employee = dao.findById(id).get();
		//設定新的加密密碼
		employee.setPwd(encoder.encode(newPwd));
		try {
			dao.save(employee);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return new BasicRes(RtnCode.CHANGE_PASSWORD_ERROR);
		}
		return new BasicRes(RtnCode.SUCCESSFUL);
	}

	@Deprecated //@Deprecated 此方法棄用，因由updateActivate取代，但還是可以使用此呼叫方法，只是槓掉做提醒
	@Override   //帳號啟用(僅管理者和人資部門)
	public BasicRes activate(String executroId, String employeeId) {
		if(!StringUtils.hasText(executroId) || !StringUtils.hasText(employeeId)
				|| executroId.equals(employeeId)) {
			return new BasicRes(RtnCode.PARAM_ERROR);
		}
		//不用判斷是否為空，因為此方法必須是login之後才能使用，在login方法已有做判斷
		Employee executor = dao.findById(executroId).get(); 
		//假設Department 是ADMIN跟HR才能使用，如果不屬於ADMIN或HR無法使用
		if(!executor.getDepartment().equalsIgnoreCase("ADMIN") 
				&& !executor.getDepartment().equalsIgnoreCase("HR")) {
			return new BasicRes(RtnCode.UNAUTHORIZATED);
		}
		//假設更新結果不等於1 為更新失敗，如果儲存成功，回傳1(除存資料成功的筆數)
		if(dao.updateActivate(employeeId, true) != 1) {
			return new BasicRes(RtnCode.UPDATE_FAILED);
		}
		return new BasicRes(RtnCode.SUCCESSFUL);
	}

	@Deprecated //@Deprecated 此方法棄用，因由updateActivate取代，但還是可以使用此呼叫方法，只是槓掉做提醒
	@Override   //帳號停用(僅管理者和人資部門)
	public BasicRes deactivate(String executroId, String employeeId) {
		if(!StringUtils.hasText(executroId) || !StringUtils.hasText(employeeId)
				|| executroId.equals(employeeId)) {
			return new BasicRes(RtnCode.PARAM_ERROR);
		}
		//把ID撈出來
		//不用判斷是否為空，因為此方法必須是login之後才能使用，在login方法已有做判斷
		Employee executor = dao.findById(executroId).get(); 
		//假設Department 是ADMIN跟HR才能使用，如果不屬於ADMIN或HR無法使用
		if(!executor.getDepartment().equalsIgnoreCase("ADMIN") 
				&& !executor.getDepartment().equalsIgnoreCase("HR")) {
			return new BasicRes(RtnCode.UNAUTHORIZATED);
		}
		//假設更新結果不等於1 為更新失敗，如果儲存成功，回傳1(除存資料成功的筆數)
		if(dao.updateActivate(employeeId, false) != 1) {
			return new BasicRes(RtnCode.UPDATE_FAILED);
		}
		return new BasicRes(RtnCode.SUCCESSFUL);
	}


	@Override //同時可達到，帳號啟用 activate 和 帳號停用 deactivate的效果
	public BasicRes updateActivate(String executroId, String employeeId, boolean isActivate) {
		//不用判斷executro是否為空，因為此方法必須是login之後才能使用，在login方法已有做判斷
		if(!StringUtils.hasText(employeeId) || executroId.equals(employeeId)) {
			return new BasicRes(RtnCode.PARAM_ERROR);
		}
		//把ID撈出來
		//不用判斷是否為空，因為此方法必須是login之後才能使用，在login方法已有做判斷
		Employee executro = dao.findById(executroId).get(); 
		//Department是 ADMIN 或是 HR才能update，只要不是ADMIN或HR就沒有權限修改
		if(!executro.getDepartment().equalsIgnoreCase("ADMIN")
				&& !executro.getDepartment().equalsIgnoreCase("HR")) {
			return new BasicRes(RtnCode.UNAUTHORIZATED);
		}
		try {
			int res = dao.updateActivate(employeeId, isActivate);
			if(res != 1) {
				return new BasicRes(RtnCode.UPDATE_FAILED);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		//假設更新結果不等於1 為更新失敗
		if(dao.updateActivate(employeeId, isActivate) != 1) {
			return new BasicRes(RtnCode.UPDATE_FAILED);
		}
		return new BasicRes(RtnCode.SUCCESSFUL);
	}


	@Override //離職方法
	public BasicRes updateResign(String executroId, String employeeId) {
		//不用判斷executro是否為空，因為此方法必須是login之後才能使用，在login方法已有做判斷
		if(!StringUtils.hasText(employeeId)|| executroId.equals(employeeId)) {
			return new BasicRes(RtnCode.PARAM_ERROR);
		}
		//不用判斷是否為空，因為此方法必須是login之後才能使用，在login方法已有做判斷
		Employee executro = dao.findById(executroId).get();
		if(!executro.getDepartment().equalsIgnoreCase("HR")) {
			return new BasicRes(RtnCode.UNAUTHORIZATED);
		}
		ResignApplication application = resignApplicationDao.findByEmployeeId(employeeId);
		Employee employee = dao.findById(employeeId).get();
		employee.setResignationDate(application.getResignationDate());
		employee.setQuitReson(application.getQuitReason());
		try {
			dao.save(employee);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return new BasicRes(RtnCode.UPDATE_FAILED);
		}	
		return new BasicRes(RtnCode.SUCCESSFUL);
	}


	@Override //離職員工填寫的申請表單
	public BasicRes resignAppliction(String employeeId) {
		//不用判斷employeeId是否為空，因為此方法必須是login之後才能使用，在login方法已有做判斷
		Employee employee = dao.findById(employeeId).get();
		try {
			resignApplicationDao.save(new ResignApplication(employeeId, employee.getDepartment(),
					LocalDate.now().plusMonths(1),"不幹了"));
		} catch (Exception e) {
			logger.error(e.getMessage());
			return new BasicRes(RtnCode.UPDATE_ERROR);
		}
		return new BasicRes(RtnCode.SUCCESSFUL);
	}


	@Override //更新員工資訊
	public BasicRes updatereInfo(String executroId, Employee employee) {
		//不用判斷executro是否為空，因為此方法必須是login之後才能使用，在login方法已有做判斷
		if(!StringUtils.hasText(employee.getId()) || executroId.equals(employee.getId())) {
			return new BasicRes(RtnCode.PARAM_ERROR);
		}
		try {
			int res = dao.updateInfo(employee.getId(), employee.getDepartment(), employee.getName(), 
					employee.getEmail(), employee.getJobPosition(), 
					employee.getBirthDate(), employee.getArrivalDate(), 
					employee.getAnnualLeave(),employee.getSickLeave());
			if(res != 1) {
				return new BasicRes(RtnCode.UPDATE_FAILED);
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			return new BasicRes(RtnCode.UPDATE_ERROR);
		}
		return new BasicRes(RtnCode.SUCCESSFUL);
	}


	@Override //找自己的資料
	public EmployeeRes findByEmployeeId(String employeeId) {
		//不用判斷executro是否為空，因為此方法必須是login之後才能使用，在login方法已有做判斷
		Employee employee = dao.findById(employeeId).get();
		return new EmployeeRes(RtnCode.SUCCESSFUL, employee);
	}


	@Override //按員工資料查找
	public EmployeeRes findByStaffInfo(String collerId, String targetId) {
		//不用判斷collerId是否為空，因為此方法必須是login之後才能使用，在login方法已有做判斷
		if(!StringUtils.hasText(targetId)) {  //需判斷targetId
			return new EmployeeRes(RtnCode.PARAM_ERROR,null);
		}
		//
		Optional<Employee> op = dao.findById(collerId);
		if(op.isEmpty()) {
			return new EmployeeRes(RtnCode.ID_NOT_FOUND,null);
		}
		Employee targetInfo = op.get();
		Employee collerInfo = dao.findById(collerId).get();
		String collerdepartment = collerInfo.getDepartment();
		//1.判斷同部門且coller是單位主管，2.coller是HR部門
		if((collerdepartment.equals(targetInfo.getDepartment()) 
				&& JobPosition.hasReviewPermission(collerInfo.getJobPosition()))
				|| collerdepartment.equalsIgnoreCase("HR")){
			return new EmployeeRes(RtnCode.SUCCESSFUL, targetInfo);
		}
		return new EmployeeRes(RtnCode.PERMISSION_DENIDE, null);
	}
		
	
}
