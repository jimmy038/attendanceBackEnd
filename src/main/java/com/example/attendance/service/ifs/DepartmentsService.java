package com.example.attendance.service.ifs;

import com.example.attendance.vo.DempartmentsCreateReq;
import com.example.attendance.vo.DepartmentsCreateRes;
import com.example.attendance.vo.BasicRes;

public interface DepartmentsService {

	public DepartmentsCreateRes create(DempartmentsCreateReq req);
}
