package com.example.attendance.constants; //constants�`�Ʊ`�q���N��

//enum�榡�������C�|�ҥH�����ݼgSUCCESSFUL(200,"OK!")���\�άO���~���T���X��
//���~�T�����|�g�b�o��	RtnCode��returnCode���N��
public enum RtnCode { //���إ�package�A�إ�enum(�C�|),���WRtnCode,�o�����~�T��,�N���~�T�����󦡦C�X
	
//	SUCCESSFUL�����\,()�����dHTTP���A�X,�D�n��200,400,401,403,404, 200�����\,�T�w��,�v����������401&403,404�T�w�N���䤣��,�ѤU�k����400
	SUCCESSFUL(200,"SUCCESSFUL!!"),//  			�b�r���᭱�[�W���ѲŸ����_�檺�ηN
	PARAM_ERROR(400,"Param_error!!"),// �q�`�r�ꤺ�e���e���T�����p�g
	ID_HAS_EXISTED(400,"ID HAS EXISTED!!"),//
	DEPARTMENTS_NOT_FOUND(404,"DEPARTMENTS NOT FOUND!!"),//
	ID_NOT_FOUND(404,"ID NOT FOUND!!"),//
	PASSWORD_ERROR(400,"PASSWORD ERROR!!"),//
	EMPLOYEE_CREATE_ERROR(400,"EMPLOYEE CREATE ERROR!!"),//
	PLEASE_LOGIN_FIRST(400,"PLEASE_LOGIN_FIRST"),// �Х��n�J
	UNAUTHORIZATED(401,"Unauthorizted"),// �v������
	CHANGE_PASSWORD_ERROR(400,"CHANGE_PASSWORD_ERROR"),//
	OLD_PASSWORD_AND_NEW_PASSWORD_ARE_IDENITCAL(400,"OLD_PASSWORD_AND_NEW_PASSWORD_ARE_IDENITCAL"),//
	FORGOT_PASSWORD_ERROR(400,"FORGOT_PASSWORD_ERROR"),//
	AUTH_CODE_NOT_MATCHED(400,"AUTH_Code_NOT_MATCHED"),//
	AUTH_CODE_EXPIRED(400,"AUTH_Code_AUTH_CODE_EXPIRED"),//
	UPDATE_FAILED(400,"Update Failed!!"),//
	UPDATE_ERROR(400,"Update Error!!"),//
	ACCOUNT_DEACTIVATE(400,"ACCOUNT_DEACTIVATE"),//
	LEAVE_TYPE_ERROR(400,"Leave Type Error!!"),// ���O���~
	LEAVE_APPLIED_DATETIME_ERROR(400,"LEAVE APPLIED DATETIME ERROR!!"),// �а����ӽЮɶ����~
	LEAVE_REASON_CANNOT_BE_EMPTY(400,"LEAVE_RASON_CANNOT_BE_EMPTY"),// 	  �а���]���ର��
	LEAVE_REVIEW_ID_CANNOT_BE_EMPTY(400,"LEAVE_REVIEW_ID_CANNOT_BE_EMPTY"),// 
	LEAVE_REVIEWID_ID_NOT_FOUND(400,"LEAVE_REVIEWID_ID_NOT_FOUND"),//
	PERMISSION_DENIDE(403,"Permission Denyied"),// �v������
	LEAVE_APPLACTION_ERROR(400,"LEAVE__APPLACTION_ERROR"),//
	LEAVE_APPLACTION_NOT_FOUND(400,"LEAVE_APPLACTION_NOT_FOUND"),//
	LACK_CERTIFICATION(400,"LACK_CERTIFICATION")
	;
	
	private int code; //�o�䪺code�����O�N�X,�^�Ǥ@�ӥN�X
	
	private String message;

	private RtnCode(int code, String message) { //���ͱa���Ѽƪ��غc��k
		this.code = code;
		this.message = message;
	}

	public int getCode() {	//�o��u�|�Ψ�get�]���u�ݭn����Get
		return code;
	}

	public String getMessage() {
		return message;
	}
	
}
