package com.example.attendance.constants;

public enum LeaveType {
	
	PERSONAL("Personal"),//
	SICK("Sick"),//
	OFFICIAL("Official"),//
	ANNUAL("Annual");
	 
	private String type;

	private LeaveType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}


	//判斷前端帶進來的資料與後端資料是否相符 要寫一個parser
	//判斷請假類別和前端是否相符(有相同的項目)	static靜態方法
	public static String parser(String type) {
		for(ReviewType item : ReviewType.values()) {
			if(type.equalsIgnoreCase(item.getType())) {
				return item.getType();
			}
		}
		return null;
	}
	
	//static靜態方法 此方法用在是否需要證明文件
	public static boolean needCertification(String type) {
		if(type.equalsIgnoreCase(LeaveType.OFFICIAL.getType())
				|| type.equalsIgnoreCase(LeaveType.SICK.getType())) {
			return true;
		}
		return false;
	}
}
