package com.example.attendance.repository;

import java.time.LocalDate;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.attendance.entity.Employee;

@Repository
public interface EmployeeDao extends JpaRepository<Employee, String>{

	//找Email是否存在
	public Employee findByEmail(String email);
	
	//update 回傳資料型態int 成功回傳 => 儲存成功更新1筆資料
	/**clearAutomatically = true: 消除持久化上下文，即清除暫存資料**/
	@Modifying(clearAutomatically = true)
	@Transactional
	@Query(value = "update Employee set active = :inputActive where id = :inputId")
	public int updateActivate(@Param("inputId")String employeeId,@Param("inputActive")boolean active);
	
	@Modifying(clearAutomatically = true)
	@Transactional
	@Query(value = "update Employee as e set" 
			+ " department  = case when :inputDepartment  is null then e.department  else :inputDepartment  end,"
			+ " name 	    = case when :inputName        is null then e.name  		 else :inputName 	    end,"
			+ " email 	    = case when :inputEmail       is null then e.email 		 else :inputEmail 	    end,"
			+ " jobPosition = case when :inputJobPosition is null then e.jobPosition else :inputJobPosition end,"
			+ " birthDate   = case when :inputBirthDate   is null then e.birthDate 	 else :inputBirthDate   end,"
			+ " arrivalDate = case when :inputArrivalDate is null then e.arrivalDate else :inputArrivalDate end,"
			+ " annualLeave = case when :inputAnnualLeave = 0    then e.annualLeave else :inputAnnualLeave end,"
			+ " sickLeave   = case when :inputSickLeave   = 0    then e.sickLeave   else :inputSickLeave   end"
			+ " where e.id  = :inputId")
	public int updateInfo(
			@Param("inputId")String id,//
			@Param("inputDepartment")String department,//
			@Param("inputName")String name,//
			@Param("inputEmail")String email,//
			@Param("inputJobPosition")String jobPosition,//
			@Param("inputBirthDate")LocalDate birthDate,
			@Param("inputArrivalDate")LocalDate arrivalDate,//
			@Param("inputAnnualLeave")int annualLeave,//
			@Param("inputSickLeave")int sickLeave);
	
	
	@Modifying(clearAutomatically = true) //coalesce函式
	@Transactional
	@Query(value = "update Employee as e set" 
			+ " department  = coalesce 	(:inputDepartment,  e.department),"
			+ " name 	    = coalesce  (:inputName, 	    e.name),"
			+ " email 	    = coalesce  (:inputEmail,       e.email),"
			+ " jobPosition = coalesce  (:inputJobPosition, e.jobPosition),"
			+ " birthDate   = coalesce	(:inputBirthDate,   e.birthDate),"
			+ " arrivalDate = coalesce  (:inputArrivalDate, e.arrivalDate),"
			+ " annualLeave = case when :inputAnnualLeave = 0   then e.annualLeave else :inputAnnualLeave end,"
			+ " sickLeave   = case when :inputSickLeave   = 0   then e.sickLeave   else :inputSickLeave   end"
			+ " where e.id  = :inputId")
	public int updateInfo1(
			@Param("inputId")String id,//
			@Param("inputDepartment")String department,//
			@Param("inputName")String name,//
			@Param("inputEmail")String email,//
			@Param("inputJobPosition")String jobPosition,//
			@Param("inputBirthDate")LocalDate birthDate,
			@Param("inputArrivalDate")LocalDate arrivalDate,//
			@Param("inputAnnualLeave")int annualLeave,//
			@Param("inputSickLeave")int sickLeave);
	
}
