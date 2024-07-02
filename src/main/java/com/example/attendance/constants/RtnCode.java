package com.example.attendance.constants; //constants常數常量的意思

//enum格式本身為列舉所以都先需寫SUCCESSFUL(200,"OK!")成功或是錯誤的訊息出來
//錯誤訊息都會寫在這邊	RtnCode為returnCode的意思
public enum RtnCode { //先建立package再建立enum(列舉),取名RtnCode,這邊放錯誤訊息,將錯誤訊息條件式列出
	
//	SUCCESSFUL為成功,()內的查HTTP狀態碼,主要用200,400,401,403,404, 200為成功,固定的,權限有相關的401&403,404固定就為找不到,剩下歸類到400
	SUCCESSFUL(200,"SUCCESSFUL!!"),//  			在逗號後面加上註解符號有斷行的用意
	PARAM_ERROR(400,"Param_error!!"),// 通常字串內容為前面訊息的小寫
	ID_HAS_EXISTED(400,"ID HAS EXISTED!!"),//
	DEPARTMENTS_NOT_FOUND(404,"DEPARTMENTS NOT FOUND!!"),//
	ID_NOT_FOUND(404,"ID NOT FOUND!!"),//
	PASSWORD_ERROR(400,"PASSWORD ERROR!!"),//
	EMPLOYEE_CREATE_ERROR(400,"EMPLOYEE CREATE ERROR!!"),//
	PLEASE_LOGIN_FIRST(400,"PLEASE_LOGIN_FIRST"),// 請先登入
	UNAUTHORIZATED(401,"Unauthorizted"),// 權限不足
	CHANGE_PASSWORD_ERROR(400,"CHANGE_PASSWORD_ERROR"),//
	OLD_PASSWORD_AND_NEW_PASSWORD_ARE_IDENITCAL(400,"OLD_PASSWORD_AND_NEW_PASSWORD_ARE_IDENITCAL"),//
	FORGOT_PASSWORD_ERROR(400,"FORGOT_PASSWORD_ERROR"),//
	AUTH_CODE_NOT_MATCHED(400,"AUTH_Code_NOT_MATCHED"),//
	AUTH_CODE_EXPIRED(400,"AUTH_Code_AUTH_CODE_EXPIRED"),//
	UPDATE_FAILED(400,"Update Failed!!"),//
	UPDATE_ERROR(400,"Update Error!!"),//
	ACCOUNT_DEACTIVATE(400,"ACCOUNT_DEACTIVATE"),//
	LEAVE_TYPE_ERROR(400,"Leave Type Error!!"),// 假別錯誤
	LEAVE_APPLIED_DATETIME_ERROR(400,"LEAVE APPLIED DATETIME ERROR!!"),// 請假的申請時間錯誤
	LEAVE_REASON_CANNOT_BE_EMPTY(400,"LEAVE_RASON_CANNOT_BE_EMPTY"),// 	  請假原因不能為空
	LEAVE_REVIEW_ID_CANNOT_BE_EMPTY(400,"LEAVE_REVIEW_ID_CANNOT_BE_EMPTY"),// 
	LEAVE_REVIEWID_ID_NOT_FOUND(400,"LEAVE_REVIEWID_ID_NOT_FOUND"),//
	PERMISSION_DENIDE(403,"Permission Denyied"),// 權限不足
	LEAVE_APPLACTION_ERROR(400,"LEAVE__APPLACTION_ERROR"),//
	LEAVE_APPLACTION_NOT_FOUND(400,"LEAVE_APPLACTION_NOT_FOUND"),//
	LACK_CERTIFICATION(400,"LACK_CERTIFICATION")
	;
	
	private int code; //這邊的code指的是代碼,回傳一個代碼
	
	private String message;

	private RtnCode(int code, String message) { //產生帶有參數的建構方法
		this.code = code;
		this.message = message;
	}

	public int getCode() {	//這邊只會用到get因此只需要產生Get
		return code;
	}

	public String getMessage() {
		return message;
	}
	
}
