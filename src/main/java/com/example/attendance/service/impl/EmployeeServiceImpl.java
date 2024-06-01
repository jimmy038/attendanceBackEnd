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
	
	//�����ŧi��private,�b�U���A�ϥ�encoder�ܼ�,�N���έ��snew �]�w�K�X�אּ�ýX
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
	
	//import Logger slf4j�o�� �ALoggerFactory�]�Oimport slf4j�o�ӡALogger�Φbı�o�|�X�����a��άO�Φbı�o���Ϊ���T
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Override //�s�W���u���
	public BasicRes create(EmployeeCreateReq req) {
		//�ˬd�o�ǥ����Ƥ��o����
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
		//�ˬdID ���]ID�s�b
		if(dao.existsById(req.getId())) {
			return new BasicRes(RtnCode.ID_HAS_EXISTED);
		}
		//check �ˬd departments_name -> �n�T�{�a�J�����W�٬O�_�s�b
		if(departmentsDao.existsByName(req.getDepartment())) {
			return new BasicRes(RtnCode.DEPARTMENTS_NOT_FOUND);
		} 
		//�K�X�[�K�אּ�ýX �[�K��s�J
		req.setPwd(encoder.encode(req.getPwd()));
		//ı�o�i��X�����a����|�Ψ�Logger
		try {
			dao.save((Employee)req); 
		} catch (Exception e) {
			//�O���@���T��logger.info ���~��logger.error
			logger.error(e.getMessage()); //�N���~�T���O����logger��
			return new BasicRes(RtnCode.EMPLOYEE_CREATE_ERROR);
		}
		return new BasicRes(RtnCode.SUCCESSFUL);
	}

	
	@Override //�n�J
	public BasicRes login(String id, String pwd, HttpSession session) {
		//�ˬd�Ѽ�(�ˬd�b�K)�A�S���b���K�X�ɡA�^��RtnCode.PARAM_ERROR
		if(!StringUtils.hasText(id) || !StringUtils.hasText(pwd)) {
			return new BasicRes(RtnCode.PARAM_ERROR);
		}
		//�ˬdID �� PWD
		Optional<Employee> op = dao.findById(id);
		//���]op���� ���Ůɦ^��RtnCode.ID_NOT_FOUND
		if(op.isEmpty()) {
			return new BasicRes(RtnCode.ID_NOT_FOUND);
		}
		//�p�G����ID ���XID
		Employee employee = op.get();
		//���]���S��matches���� matches(��J����K�X�A���[�K�᪺�K�X(��Ʈw�����K�X))
		if(!encoder.matches(pwd, employee.getPwd())) {
			return new BasicRes(RtnCode.PASSWORD_ERROR);
		}
		//�T�{�b��Active���A����
		if(!employee.getActive()) {
			return new BasicRes(RtnCode.ACCOUNT_DEACTIVATE);
		}
		//�s�J�b�K�bHttpSession session(�Ȧs)
		session.setAttribute(id, employee.getDepartment());//("A01",����IT)
		//�]�w�ɶ��Ȧs�n�J�ɶ��A�w�]��30����1800��A�w�]��쬰��
		session.setMaxInactiveInterval(3000); //�]�w3000��10����
		//�O���@���T��logger.info ���~��logger.error
		logger.info("login successful");
		return new BasicRes(RtnCode.SUCCESSFUL);
	}


	@Override //���K�X
	public BasicRes changePassword(String id, String oldPwd, String newPwd) {
		if(!StringUtils.hasText(id) || !StringUtils.hasText(oldPwd) || !StringUtils.hasText(newPwd)) {
			return new BasicRes(RtnCode.PARAM_ERROR);
		}
		//�P�_�s���K�X����P�±K�X�ۦP�Aequals��ӭȤ��A�±K�X���s�K�X
		if(oldPwd.equals(newPwd)){
			//�p�G�±K�X�P�s���K�X�ۦP�߿��~�T���^�h
			return new BasicRes(RtnCode.OLD_PASSWORD_AND_NEW_PASSWORD_ARE_IDENITCAL);
		}
		//���ΧP�_�O�_���šA�]������k�����Ologin����~��ϥΡA�blogin��k�w�����P�_
		Employee employee = dao.findById(id).get();
		//
		if(!encoder.matches(oldPwd, employee.getPwd())) {
			return new BasicRes(RtnCode.PASSWORD_ERROR);
		}
		//�]�w�s�K�X���[�K�᪺�K�X
		employee.setPwd(encoder.encode(newPwd));
		try {		//try catch����O�_�����~�A�Y�S���N����try�̭����A���~�]�icatch
			//�s�J�s�K�X
			dao.save(employee);
		} catch (Exception e) {
			logger.error(e.getMessage()); //�N���~�T���O����logger��
			return new BasicRes(RtnCode.CHANGE_PASSWORD_ERROR);
		}
		return new BasicRes(RtnCode.SUCCESSFUL);
	}


	@Override //�ѰO�K�X����k
	public BasicRes forgotPassword(String id, String email) {
		//�ˬdID��email�̤֤@�w�n���@�Ӧ���
		if(!StringUtils.hasText(id) && !StringUtils.hasText(email)) {
			return new BasicRes(RtnCode.PARAM_ERROR);
		}
		//�]��null�A�o�򰵬O���F�T�O�Y�ϨS���ŦX��������۹����� Employee ����A�ܼ� employee �]���@�Ӫ�l�ȡC�o�˰����ت��O���F�b���򪺵{���X���i��P�_�M�קK��b���ū��w���`�C
		Employee employee = null;
		//���]ID����
		if(StringUtils.hasText(id)) {
			Optional<Employee> op = dao.findById(id);
			//�P�_ID�p�G���Ŧ^��ID_NOT_FOUND
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
		//RandomString.make���ͥ]�t�j�p�g�^��Ʀr�������üơA()����12������12�X���üƪ��K�X
		String randomPwd = RandomString.make(12);
		employee.setPwd(encoder.encode(randomPwd));
		//�������ҽX�A����6�X�����ҽX�A���Įɶ�30����
		String authCode = RandomString.make(6);
		LocalDateTime now = LocalDateTime.now();
		try {
			//�x�semployee���
			dao.save(employee);
			//�x�sID ���ҽX �{�b�ɶ���AuthCode							�[�W���Įɶ�30����
			authCodeDao.save(new AuthCode(employee.getId(),authCode,now.plusMinutes(authCodeExpiredTime)));
		} catch (Exception e) {
			logger.error(e.getMessage());
			return new BasicRes(RtnCode.FORGOT_PASSWORD_ERROR);
		}
		//���� email�ñH�eemail(��randomPwd�ǤJemail)(�n�ɤW)
		return new BasicRes(RtnCode.SUCCESSFUL);
	}


	@Override
	public BasicRes changePasswordByAuthCode(String id, String authCode,String newPwd) {
		if(!StringUtils.hasText(id) || !StringUtils.hasText(authCode) || !StringUtils.hasText(newPwd)) {
			return new BasicRes(RtnCode.PARAM_ERROR);
		}
		//�ˬdAuthCode
		Optional<AuthCode> op = authCodeDao.findById(id);
		if(op.isEmpty()) {
			return new BasicRes(RtnCode.ID_NOT_FOUND);
		}
		AuthCode authCodeEntity = op.get();
		if(!authCodeEntity.getAuthCode().equals(authCode)) {
			return new BasicRes(RtnCode.AUTH_CODE_NOT_MATCHED);
		}
		//�ˬd��e�ɶ���DB���ɶ��A�P�_���ҽX�ɮ�30����
		LocalDateTime now = LocalDateTime.now();
		//���]��e�ɶ��b���Įɶ�30��������
		if(now.isAfter(authCodeEntity.getAuthDatetime())) {
			return new BasicRes(RtnCode.AUTH_CODE_EXPIRED);
		}
		//��ID����u��Ƽ��^��
		Employee employee = dao.findById(id).get();
		//�]�w�s���[�K�K�X
		employee.setPwd(encoder.encode(newPwd));
		try {
			dao.save(employee);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return new BasicRes(RtnCode.CHANGE_PASSWORD_ERROR);
		}
		return new BasicRes(RtnCode.SUCCESSFUL);
	}

	@Deprecated //@Deprecated ����k��ΡA�]��updateActivate���N�A���٬O�i�H�ϥΦ��I�s��k�A�u�O�b��������
	@Override   //�b���ҥ�(�Ⱥ޲z�̩M�H�곡��)
	public BasicRes activate(String executroId, String employeeId) {
		if(!StringUtils.hasText(executroId) || !StringUtils.hasText(employeeId)
				|| executroId.equals(employeeId)) {
			return new BasicRes(RtnCode.PARAM_ERROR);
		}
		//���ΧP�_�O�_���šA�]������k�����Ologin����~��ϥΡA�blogin��k�w�����P�_
		Employee executor = dao.findById(executroId).get(); 
		//���]Department �OADMIN��HR�~��ϥΡA�p�G���ݩ�ADMIN��HR�L�k�ϥ�
		if(!executor.getDepartment().equalsIgnoreCase("ADMIN") 
				&& !executor.getDepartment().equalsIgnoreCase("HR")) {
			return new BasicRes(RtnCode.UNAUTHORIZATED);
		}
		//���]��s���G������1 ����s���ѡA�p�G�x�s���\�A�^��1(���s��Ʀ��\������)
		if(dao.updateActivate(employeeId, true) != 1) {
			return new BasicRes(RtnCode.UPDATE_FAILED);
		}
		return new BasicRes(RtnCode.SUCCESSFUL);
	}

	@Deprecated //@Deprecated ����k��ΡA�]��updateActivate���N�A���٬O�i�H�ϥΦ��I�s��k�A�u�O�b��������
	@Override   //�b������(�Ⱥ޲z�̩M�H�곡��)
	public BasicRes deactivate(String executroId, String employeeId) {
		if(!StringUtils.hasText(executroId) || !StringUtils.hasText(employeeId)
				|| executroId.equals(employeeId)) {
			return new BasicRes(RtnCode.PARAM_ERROR);
		}
		//��ID���X��
		//���ΧP�_�O�_���šA�]������k�����Ologin����~��ϥΡA�blogin��k�w�����P�_
		Employee executor = dao.findById(executroId).get(); 
		//���]Department �OADMIN��HR�~��ϥΡA�p�G���ݩ�ADMIN��HR�L�k�ϥ�
		if(!executor.getDepartment().equalsIgnoreCase("ADMIN") 
				&& !executor.getDepartment().equalsIgnoreCase("HR")) {
			return new BasicRes(RtnCode.UNAUTHORIZATED);
		}
		//���]��s���G������1 ����s���ѡA�p�G�x�s���\�A�^��1(���s��Ʀ��\������)
		if(dao.updateActivate(employeeId, false) != 1) {
			return new BasicRes(RtnCode.UPDATE_FAILED);
		}
		return new BasicRes(RtnCode.SUCCESSFUL);
	}


	@Override //�P�ɥi�F��A�b���ҥ� activate �M �b������ deactivate���ĪG
	public BasicRes updateActivate(String executroId, String employeeId, boolean isActivate) {
		//���ΧP�_executro�O�_���šA�]������k�����Ologin����~��ϥΡA�blogin��k�w�����P�_
		if(!StringUtils.hasText(employeeId) || executroId.equals(employeeId)) {
			return new BasicRes(RtnCode.PARAM_ERROR);
		}
		//��ID���X��
		//���ΧP�_�O�_���šA�]������k�����Ologin����~��ϥΡA�blogin��k�w�����P�_
		Employee executro = dao.findById(executroId).get(); 
		//Department�O ADMIN �άO HR�~��update�A�u�n���OADMIN��HR�N�S���v���ק�
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
		//���]��s���G������1 ����s����
		if(dao.updateActivate(employeeId, isActivate) != 1) {
			return new BasicRes(RtnCode.UPDATE_FAILED);
		}
		return new BasicRes(RtnCode.SUCCESSFUL);
	}


	@Override //��¾��k
	public BasicRes updateResign(String executroId, String employeeId) {
		//���ΧP�_executro�O�_���šA�]������k�����Ologin����~��ϥΡA�blogin��k�w�����P�_
		if(!StringUtils.hasText(employeeId)|| executroId.equals(employeeId)) {
			return new BasicRes(RtnCode.PARAM_ERROR);
		}
		//���ΧP�_�O�_���šA�]������k�����Ologin����~��ϥΡA�blogin��k�w�����P�_
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


	@Override //��¾���u��g���ӽЪ��
	public BasicRes resignAppliction(String employeeId) {
		//���ΧP�_employeeId�O�_���šA�]������k�����Ologin����~��ϥΡA�blogin��k�w�����P�_
		Employee employee = dao.findById(employeeId).get();
		try {
			resignApplicationDao.save(new ResignApplication(employeeId, employee.getDepartment(),
					LocalDate.now().plusMonths(1),"���F�F"));
		} catch (Exception e) {
			logger.error(e.getMessage());
			return new BasicRes(RtnCode.UPDATE_ERROR);
		}
		return new BasicRes(RtnCode.SUCCESSFUL);
	}


	@Override //��s���u��T
	public BasicRes updatereInfo(String executroId, Employee employee) {
		//���ΧP�_executro�O�_���šA�]������k�����Ologin����~��ϥΡA�blogin��k�w�����P�_
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


	@Override //��ۤv�����
	public EmployeeRes findByEmployeeId(String employeeId) {
		//���ΧP�_executro�O�_���šA�]������k�����Ologin����~��ϥΡA�blogin��k�w�����P�_
		Employee employee = dao.findById(employeeId).get();
		return new EmployeeRes(RtnCode.SUCCESSFUL, employee);
	}


	@Override //�����u��Ƭd��
	public EmployeeRes findByStaffInfo(String collerId, String targetId) {
		//���ΧP�_collerId�O�_���šA�]������k�����Ologin����~��ϥΡA�blogin��k�w�����P�_
		if(!StringUtils.hasText(targetId)) {  //�ݧP�_targetId
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
		//1.�P�_�P�����Bcoller�O���D�ޡA2.coller�OHR����
		if((collerdepartment.equals(targetInfo.getDepartment()) 
				&& JobPosition.hasReviewPermission(collerInfo.getJobPosition()))
				|| collerdepartment.equalsIgnoreCase("HR")){
			return new EmployeeRes(RtnCode.SUCCESSFUL, targetInfo);
		}
		return new EmployeeRes(RtnCode.PERMISSION_DENIDE, null);
	}
		
	
}
