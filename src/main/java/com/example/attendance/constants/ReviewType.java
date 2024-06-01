package com.example.attendance.constants;

public enum ReviewType {

	PASS("Pass"),//
	REVIEWING("REVIEWING"),//
	REJECT("REJECT"),//
	REVOKE("REVOKE");
	
	private String type;

	private ReviewType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}


	//�P�_�а����O�M�e�ݬO�_�۲�(���ۦP������)
	public static String parser(String type) {
		for(ReviewType item : ReviewType.values()) {
			if(type.equalsIgnoreCase(item.getType())) {
				return item.getType();
			}
		}
		return null;
	}
	
	
	
}
