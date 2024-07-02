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




//����
@Service
public class DempartmentsServiceImpl implements DepartmentsService{

	@Autowired
	private DepartmentsDao dao;

	@Override //�Ĥ@����ˬd�ѼƬO�_����
	public DepartmentsCreateRes create (DepartmentsCreateReq req) {
		//�ˬd�@��List��map�O�_���� CollectionUtils
		if(CollectionUtils.isEmpty(req.getDepList()) ) {
			return new DepartmentsCreateRes(RtnCode.PARAM_ERROR);
		}
		//�ˬdDepartments���� �o��id�]���r��AID&Name�O�_���šA�����ŮɱNid�[�JidList��
		List<String> idList = new ArrayList<>();
		//�j��M��list
		for(Departments item : req.getDepList()) {
			if(!StringUtils.hasText(item.getId()) || !StringUtils.hasText(item.getName())) {
				return new DepartmentsCreateRes(RtnCode.PARAM_ERROR);
			}
			idList.add(item.getId());
		}
		//�ˬd�`���쪺ID�O�_�s�b �AidList���`���쪺ID
		if(dao.existsByIdIn(idList)) {
			return new DepartmentsCreateRes(RtnCode.ID_HAS_EXISTED);
		}		
		//�ˬd���S���D��dao�s����saveall
		dao.saveAll(req.getDepList());
		return new DepartmentsCreateRes(RtnCode.SUCCESSFUL);
	}
	
	
	
	
}
