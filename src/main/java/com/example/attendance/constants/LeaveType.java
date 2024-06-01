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


	//�P�_�e�ݱa�i�Ӫ���ƻP��ݸ�ƬO�_�۲� �n�g�@��parser
	//�P�_�а����O�M�e�ݬO�_�۲�(���ۦP������)	static�R�A��k
	public static String parser(String type) {
		for(ReviewType item : ReviewType.values()) {
			if(type.equalsIgnoreCase(item.getType())) {
				return item.getType();
			}
		}
		return null;
	}
	
	//static�R�A��k ����k�Φb�O�_�ݭn�ҩ����
	public static boolean needCertification(String type) {
		if(type.equalsIgnoreCase(LeaveType.OFFICIAL.getType())
				|| type.equalsIgnoreCase(LeaveType.SICK.getType())) {
			return true;
		}
		return false;
	}
}
