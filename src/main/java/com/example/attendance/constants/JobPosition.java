package com.example.attendance.constants;

public enum JobPosition {
	
	//��o�䪺�N�X�Q�����v�����@��
	ADMIN(99,"Admin"),// 
	SUPERVISOR(21,"Supervisor"),// ���D��
	DIRECTOR(20,"Director"),//
	SENIOR(2,"Senior"),//
	GENERAL(1,"General");
		
	private int permission;
	
	private String title;

	private JobPosition(int permission, String title) {
		this.permission = permission;
		this.title = title;
	}

	//�u�ݭn�Ψ�get
	public int getPermission() {
		return permission;
	}

	public String getTitle() {
		return title;
	}
	
	
	//�benum�n�I�s��k���n�אּ�R�A��kstatic�~�i�I�s
	
	//parser�ھڦr�q���o�v���Aparser �T�{�~���I�s�����v���ɡA�~���a�J��¾���W�٬O�_���b�����]�w��¾���W��
	//�ھڥ~���Ƕi�Ӫ�title�ѼƭȨ��o�����v��(permission)
	//�i�H�T�{title�ѼƭȬO�_�w�q�b��JobPosition��
	public static int parser(String title) { //�[�Wstatic(�R�A��k)�~�i�I�s��k
		for(JobPosition item : JobPosition.values()) {
			if(title.equalsIgnoreCase(item.getTitle())) {
				return item.getPermission();
			}
		}
		return 0;
	}
	
	
	private  static int reviewPermission = 20;
	
	public static boolean hasReviewPermission(String title) {
		int callerPermission = parser(title); //parser�I�s�W���ۤv����k
		//�p�G���^true�S���^false ���T�����g�k
		return callerPermission >= reviewPermission ? true : false; 
	}

}
