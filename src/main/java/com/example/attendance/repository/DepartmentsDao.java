package com.example.attendance.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.attendance.entity.Departments;

@Repository
public interface DepartmentsDao extends JpaRepository<Departments, String>{

	/** �ˬdlist��id��in(�]�t�d��) **/
	public boolean existsByIdIn(List<String> ids);
	
	/** �ˬdname�O�_�s�b **/
	public boolean existsByName(String name); 
}
