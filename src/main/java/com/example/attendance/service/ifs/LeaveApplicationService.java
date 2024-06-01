package com.example.attendance.service.ifs;

import org.springframework.stereotype.Service;

import com.example.attendance.vo.BasicRes;
import com.example.attendance.vo.LeaveApplicationReq;

@Service
public interface LeaveApplicationService {

	public BasicRes apply(LeaveApplicationReq req);
	
	public BasicRes review(String reviewId,String applicationNo);
} 
