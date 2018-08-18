package clonegod.spider.core.page;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PageExtract implements Serializable {
	
	private static final long serialVersionUID = -5772542862232652040L;

	// 请求成功后返回页面中的特性字段
	List<Field> successFields = new ArrayList<>();
	
	// 请求失败后返回页面中的特性字段 - 提取登陆失败后的错误提示（账号错误、密码错误、验证码错误等）
	List<Field> failedFields = new ArrayList<>();
	
	// 辅助字段，为后续请求页面提供参数
	List<Field> extFields = new ArrayList<>();

	// 业务数据字段
	List<Field> businessDataFields = new ArrayList<>();
	
	public List<Field> getSuccessFields() {
		return successFields;
	}

	public void setSuccessFields(List<Field> successFields) {
		this.successFields = successFields;
	}

	public List<Field> getFailedFields() {
		return failedFields;
	}

	public void setFailedFields(List<Field> failedFields) {
		this.failedFields = failedFields;
	}

	public List<Field> getBusinessDataFields() {
		return businessDataFields;
	}

	public void setBusinessDataFields(List<Field> businessDataFields) {
		this.businessDataFields = businessDataFields;
	}

	public List<Field> getExtFields() {
		return extFields;
	}

	public void setExtFields(List<Field> extFields) {
		this.extFields = extFields;
	}

}
