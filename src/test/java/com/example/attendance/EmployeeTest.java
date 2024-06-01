package com.example.attendance;

import java.time.LocalDate;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.Assert;

import com.example.attendance.entity.Employee;
import com.example.attendance.repository.EmployeeDao;

@SpringBootTest
public class EmployeeTest {

	@Autowired
	private EmployeeDao employeeDao;
	
	@Value("${authcode.expired.time}") //�Y��]�w�ɪ��n��$ {} �i�H���w�]�� �Ϋ_�� : ����
	private int authCodeExpiredTime;
	
	@Test
	public void createAdminTest() {
		//�K�X�[�K
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		Employee employee = employeeDao.save(new Employee("Admin","ADMIN","AdMin",
				encoder.encode("$Admin^_^0tp"),"admin@G","99",LocalDate.now(),
				LocalDate.now(),true));
		Assert.isTrue(employee != null,"Create employee error!!");
		}
	
	
	@Test
	public void parmTest() {
		System.out.println(authCodeExpiredTime);
	}
	
	
	
}
