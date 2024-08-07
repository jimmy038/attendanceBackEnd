package com.example.attendance.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.attendance.entity.Departments;

@Repository
public interface DepartmentsDao extends JpaRepository<Departments, String>{

	/** 檢查list的id用in(包含範圍) **/
	public boolean existsByIdIn(List<String> ids);
	
	/** 檢查name是否存在 **/
	public boolean existsByName(String name); 
}
