package com.example.attendance.constants;

public enum JobPosition {
	
	//把這邊的代碼想像成權限的一種
	ADMIN(99,"Admin"),// 
	SUPERVISOR(21,"Supervisor"),// 單位主管
	DIRECTOR(20,"Director"),//
	SENIOR(2,"Senior"),//
	GENERAL(1,"General");
		
	private int permission;
	
	private String title;

	private JobPosition(int permission, String title) {
		this.permission = permission;
		this.title = title;
	}

	//只需要用到get
	public int getPermission() {
		return permission;
	}

	public String getTitle() {
		return title;
	}
	
	
	//在enum要呼叫方法都要改為靜態方法static才可呼叫
	
	//parser根據字段取得權限，parser 確認外面呼叫取的權限時，外部帶入的職等名稱是否為在內部設定的職等名稱
	//根據外部傳進來的title參數值取得對應權限(permission)
	//可以確認title參數值是否定義在此JobPosition中
	public static int parser(String title) { //加上static(靜態方法)才可呼叫方法
		for(JobPosition item : JobPosition.values()) {
			if(title.equalsIgnoreCase(item.getTitle())) {
				return item.getPermission();
			}
		}
		return 0;
	}
	
	
	private  static int reviewPermission = 20;
	
	public static boolean hasReviewPermission(String title) {
		int callerPermission = parser(title); //parser呼叫上面自己的方法
		//如果有回true沒有回false 為三元式寫法
		return callerPermission >= reviewPermission ? true : false; 
	}

}
