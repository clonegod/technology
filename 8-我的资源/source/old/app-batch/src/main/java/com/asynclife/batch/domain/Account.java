package com.asynclife.batch.domain;

public class Account {
	
	private String elecAccountNo;
	private String name;
	private int age;
	private String certNo;
	private String balance;
	
	public String getElecAccountNo() {
		return elecAccountNo;
	}
	public void setElecAccountNo(String elecAccountNo) {
		this.elecAccountNo = elecAccountNo;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String getCertNo() {
		return certNo;
	}
	public void setCertNo(String certNo) {
		this.certNo = certNo;
	}
	public String getBalance() {
		return balance;
	}
	public void setBalance(String balance) {
		this.balance = balance;
	}
	@Override
	public String toString() {
		return "Account [elecAccountNo=" + elecAccountNo + ", name=" + name
				+ ", age=" + age + ", certNo=" + certNo + ", balance="
				+ balance + "]";
	}
	
}
