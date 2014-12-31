package com.tpv.selenium.urtracker;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

public class Urtracker {
	private String mUrl = null;
	public Urtracker(String url){
		mUrl = url;
	}
	private WebDriver mDriver = null;
	private void createWebDriverHtmlUnit(){
		mDriver = new HtmlUnitDriver(true);
		
	}
	private void createWebDriverChrome(){
		 mDriver = new ChromeDriver();
	}
	public void login(String user, String pass){
		 mDriver.get(mUrl);
		 // fill the user info
		 WebElement  u = mDriver.findElement(By.name("txtEmail"));
		 u.sendKeys(user);
		 WebElement p = mDriver.findElement(By.name("txtPassword"));
		 p.sendKeys(pass);
		 WebElement b = mDriver.findElement(By.name("btnLogin"));
		 b.click();
		 
		 try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 		
		 
		 // check if login success
		 String homeUrl = "http://fwtrack.tpvaoc.com/pts/home.aspx";
		 System.out.println(mDriver.getCurrentUrl());
		 System.out.println(mDriver.getPageSource());
	}
	
	/*
	 * get the issue with name
	 */
	public void getIssue(String issueName){
		String id = "ctl00_Siteheader1_txtProblemID";
		//String input = "ctl00$Siteheader1$txtProblemID";
		WebElement i = mDriver.findElement(By.id(id));
		i.sendKeys(issueName);
		
		WebElement c = mDriver.findElement(By.name("ctl00$Siteheader1$btnGoToProblem"));
		c.click();
		
	}

	/**
	 * add comment to the current issue
	 * @param comment
	 */
	public void inputComment(String comment){	
		// switch to the editor frame
		List<WebElement> iframes = mDriver.findElements(By.tagName("iframe"));
		WebElement editorFrame = iframes.get(0);
		System.out.println("Total iframes: "+iframes.size());
		System.out.println("class name: "+editorFrame.getAttribute("className"));
		
		mDriver.switchTo().frame(editorFrame);
		System.out.println("body: "+mDriver.findElement(By.tagName("body")).getAttribute("class"));
		
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}	
		WebElement ta = mDriver.findElement(By.tagName("body"));
		ta.sendKeys(comment);
		
		mDriver.switchTo().defaultContent();
		
		String submit = "ctl00$CP1$btnSubbmit";
		WebElement button = mDriver.findElement(By.name(submit));
		button.click();
		
		mDriver.switchTo().alert().accept();
		mDriver.switchTo().defaultContent();
		
		
	}
	public void clickAddComment(){
		String id = "ctl00_CP1_IssuePageToolbar2_lnkAddComment";
		WebElement e = mDriver.findElement(By.id(id));
		e.click();
		
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}
	public void submitComment(){
		mDriver.switchTo().defaultContent();
		
		String submit = "ctl00$CP1$btnSubbmit";
		WebElement button = mDriver.findElement(By.name(submit));
		button.click();
		
		mDriver.switchTo().alert().accept();
		mDriver.switchTo().defaultContent();

	}
	public void inputCommentXM(String comment){
		String frameId = "ctl00_CP1_editorContent_editor___Frame";
		mDriver.switchTo().frame(frameId);
		List<WebElement> lw = mDriver.findElements(By.tagName("iframe"));
		System.out.println("XM: find "+lw.size()+" frames");
		mDriver.switchTo().frame(lw.get(0));
		WebElement body = mDriver.findElement(By.tagName("body"));
		body.sendKeys(comment);
		
	}
	public static void main(String args[]){
		
		System.setProperty("webdriver.chrome.driver", "D:/lib/selenium-2.44.0/chromedriver.exe");
		//Urtracker tracker = new Urtracker("http://172.16.144.100");
		Urtracker tracker = new Urtracker("http://fwtrack.tpvaoc.com");
		//tracker.createWebDriverHtmlUnit();
		tracker.createWebDriverChrome();
		tracker.login("jingxian.lin", "!q2w3e");
		tracker.getIssue("TF413PHICNPAN03-1158");
		tracker.clickAddComment();
		tracker.inputComment("comment");
		tracker.submitComment();
		
	}
}
