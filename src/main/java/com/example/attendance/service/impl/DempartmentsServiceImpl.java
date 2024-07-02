package com.example.attendance.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.example.attendance.constants.RtnCode;
import com.example.attendance.entity.Departments;
import com.example.attendance.repository.DepartmentsDao;
import com.example.attendance.service.ifs.DepartmentsService;
import com.example.attendance.vo.DepartmentsCreateReq;
import com.example.attendance.vo.DepartmentsCreateRes;




//部門
@Service
public class DempartmentsServiceImpl implements DepartmentsService{

	@Autowired
	private DepartmentsDao dao;

	@Override //第一件事檢查參數是否為空
	public DepartmentsCreateRes create (DepartmentsCreateReq req) {
		//檢查一個List或map是否為空 CollectionUtils
		if(CollectionUtils.isEmpty(req.getDepList()) ) {
			return new DepartmentsCreateRes(RtnCode.PARAM_ERROR);
		}
		//檢查Departments內的 這邊id也為字串，ID&Name是否為空，不為空時將id加入idList內
		List<String> idList = new ArrayList<>();
		//迴圈遍歷list
		for(Departments item : req.getDepList()) {
			if(!StringUtils.hasText(item.getId()) || !StringUtils.hasText(item.getName())) {
				return new DepartmentsCreateRes(RtnCode.PARAM_ERROR);
			}
			idList.add(item.getId());
		}
		//檢查蒐集到的ID是否存在 ，idList為蒐集到的ID
		if(dao.existsByIdIn(idList)) {
			return new DepartmentsCreateRes(RtnCode.ID_HAS_EXISTED);
		}		
		//檢查完沒問題用dao存全部saveall
		dao.saveAll(req.getDepList());
		return new DepartmentsCreateRes(RtnCode.SUCCESSFUL);
	}
	
	
	
	
}
