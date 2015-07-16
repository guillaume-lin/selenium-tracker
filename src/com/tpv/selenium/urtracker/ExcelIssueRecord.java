package com.tpv.selenium.urtracker;

import pw.agiledev.e2e.annotation.ExcelEntity;
import pw.agiledev.e2e.annotation.ExcelProperty;

@ExcelEntity
public class ExcelIssueRecord {
	@ExcelProperty(value="事务编码")
	private String issueCode;
	
	@ExcelProperty(value="review")
	private String issueReview;
	
	public String getIssueCode(){
		return issueCode;
	}
	public String getIssueReview(){
		return issueReview;
	}
	public void setIssueCode(String code){
		issueCode = code;
	}
	public void setIssueReview(String review){
		issueReview = review;
	}
	public void dump(){
		System.out.println("IssueCode: "+issueCode);
		System.out.println("IssueReview: "+issueReview);
	}
}
