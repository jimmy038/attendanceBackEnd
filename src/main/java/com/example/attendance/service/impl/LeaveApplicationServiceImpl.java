package com.example.attendance.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.example.attendance.constants.JobPosition;
import com.example.attendance.constants.LeaveType;
import com.example.attendance.constants.ReviewType;
import com.example.attendance.constants.RtnCode;
import com.example.attendance.entity.Employee;
import com.example.attendance.entity.LeaveApplication;
import com.example.attendance.repository.EmployeeDao;
import com.example.attendance.repository.LeaveApplicationDao;
import com.example.attendance.service.ifs.LeaveApplicationService;
import com.example.attendance.vo.BasicRes;
import com.example.attendance.vo.LeaveApplicationReq;


@Service
public class LeaveApplicationServiceImpl implements LeaveApplicationService{

	//import Logger slf4j�o�� �ALoggerFactory�]�Oimport slf4j�o�ӡALogger�Φbı�o�|�X�����a��άO�Φbı�o���Ϊ���T
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private EmployeeDao employeeDao;
	
	@Autowired
	private LeaveApplicationDao dao;
	
	
	@Override
	public BasicRes apply(LeaveApplicationReq req) {
		if(ReviewType.parser(req.getEmployeeDepartment()) == null){
			return new BasicRes(RtnCode.LEAVE_TYPE_ERROR);
		}
		//�P�_���O�A�а����}�l�ɶ�����b�����ɶ�����
		if(req.getLeaveStartDatetime().isAfter(req.getLeaveEndDatetime())
				|| req.getAppliedDatetime() == null) {
			return new BasicRes(RtnCode.LEAVE_APPLIED_DATETIME_ERROR);
		}
		if(!StringUtils.hasText(req.getLeaveReason())) {
			return new BasicRes(RtnCode.LEAVE_REASON_CANNOT_BE_EMPTY);
		}
		if(!StringUtils.hasText(req.getReviewId())) {
			return new BasicRes(RtnCode.LEAVE_REVIEW_ID_CANNOT_BE_EMPTY);
		}
		if(LeaveType.needCertification(req.getLeaveType())
				&& req.getCertification() == null) {
			return new BasicRes(RtnCode.LACK_CERTIFICATION);
		}
		Optional<Employee> op = employeeDao.findById(req.getReviewId());
		if(op.isEmpty()) {
			return new BasicRes(RtnCode.LEAVE_REVIEWID_ID_NOT_FOUND);
		}
		Employee reviewer = op.get();
		if(!JobPosition.hasReviewPermission(reviewer.getJobPosition())) {
			return new BasicRes(RtnCode.PERMISSION_DENIDE);
		}
		LocalDateTime now = LocalDateTime.now();
		req.setApplicationNo(now.toString().replaceAll("[-T:.]", ""));
		req.setUpdateDatetime(now);
		req.setUpdateDatetime(LocalDateTime.now());
		try {
			dao.save((LeaveApplication)req);
			//TODO �H�eemail��reviewer : �H�󤺮e�n������s��
		} catch (Exception e) {
			logger.error(e.getMessage());
			return new BasicRes(RtnCode.LEAVE_APPLACTION_ERROR);
		}
		return new BasicRes(RtnCode.SUCCESSFUL);
	}


	@Override
	public BasicRes review(String reviewId, String applicationNo) {
		//���ΧP�_reviewId�O�_���šA�]������k�OLOGIN��~��ϥΡA�bLOGIN�w�����P�_
		if(!StringUtils.hasText(applicationNo)) {
			return new BasicRes(RtnCode.PARAM_ERROR);
		}
		List<LeaveApplication> list = dao.findByApplicationNo(applicationNo);
		if(CollectionUtils.isEmpty(list)) {
			return new BasicRes(RtnCode.LEAVE_APPLACTION_NOT_FOUND);
		}
		//���o�̷s�@�������T�A���o�̫�@��list.size() -1
		LeaveApplication application = list.get(list.size() -1 );
		Employee reviewer = employeeDao.findById(reviewId).get();		
		//����ӽЪ̭n�P�f�֪̦P����
		if(!application.getEmployeeDepartment().equalsIgnoreCase(reviewer.getDepartment())) {
			return new BasicRes(RtnCode.PERMISSION_DENIDE);
		}
		//����f�֪̥��ݭn�O���D���v���H�W(�t)�H�W
		if(!JobPosition.hasReviewPermission(reviewer.getJobPosition())) {
			return new BasicRes(RtnCode.PERMISSION_DENIDE);
		}
		//���]�ݭn�ҩ����A�ҩ����S��
		if(LeaveType.needCertification(application.getLeaveType())
				&& application.getCertification() == null) {
			application.setReviewStatus(ReviewType.REJECT.getType());
			application.setRejectReason(RtnCode.LACK_CERTIFICATION.getMessage());
		}else {
			application.setReviewStatus(ReviewType.PASS.getType());
		}
		LocalDateTime now = LocalDateTime.now();
		application.setReviewDatetime(now);
//		application.setReviewStatus(ReviewType.PASS.getType());
		application.setUpdateDatetime(now);
		try {
			dao.save(new LeaveApplication(application));
			//TODO �H�eEmail��reviewer : �H�󤺮e�n������s��
		} catch (Exception e) {
			logger.error(e.getMessage());
			return new BasicRes(RtnCode.LEAVE_APPLACTION_ERROR);
		}
		return new BasicRes(RtnCode.SUCCESSFUL);
	}



	
	
	

}
