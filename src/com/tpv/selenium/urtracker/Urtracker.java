package com.tpv.selenium.urtracker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.testng.internal.annotations.ListenersAnnotation;

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
	private void createWebDriverFirefox(){
		mDriver = new FirefoxDriver();
	}
	private boolean waitForPage(String url, int timeout){
		timeout = timeout*10;
		while(timeout > 0){
			if(mDriver.getCurrentUrl().contains(url))
				return true;
			timeout -= 100;
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return false;
	}
	public void login(String user, String pass){
		 mDriver.get(mUrl);
		 // need wait the page to loaded
		 
		 // fill the user info
		 WebElement  u = mDriver.findElement(By.name("txtEmail"));
		 u.sendKeys(user);
		 WebElement p = mDriver.findElement(By.name("txtPassword"));
		 p.sendKeys(pass);
		 WebElement b = mDriver.findElement(By.name("btnLogin"));
		 b.click();
		 	
		 
		 // check if login success
		 String homeUrl = "http://fwtrack.tpvaoc.com/pts/home.aspx";
		 waitForPage(homeUrl, 5);
		 System.out.println(mDriver.getCurrentUrl());
		 
	}
	
	public class ProcessRecord {
		public int mRecordNo = 0;
		public String mTime;
		public String mActor;
		public String mOperation;
		public String mState;
		public String mAssignee;
		public void dump(){
			System.out.println("RecordNo: "+mRecordNo);
			System.out.println("time: "+mTime);
			System.out.println("actor: "+mActor);
			System.out.println("operation: "+mOperation);
			System.out.println("state: "+mState);
			System.out.println("assignee: "+mAssignee);
		}
	}
	private class IssueRecord {
		public String mProject;
		public String mIssueCode;  // something like: XF115CITCNMS1-1019(321980)
		public int    mIssueId;  //
		public String mAssignee;
		public String mCreateTime;
		public String mCreator;
		public String mState;
		public String mSubject;
		public String mDescription;
		public List<ProcessRecord> mProcessRecords;
		
		public void dump(){
			System.out.println("mProject:   " + mProject);
			System.out.println("mCreator:   " + mCreator);
			System.out.println("mCreateTime:" + mCreateTime);
			System.out.println("mSubject:   " + mSubject);
			System.out.println("mDescription:"+mDescription);
		}
	}
	public List<ProcessRecord> getProcessRecord(){
		List<ProcessRecord> lpr = new ArrayList<ProcessRecord>();
			
		String id = "record_list";
		WebElement i = mDriver.findElement(By.id(id));
		List<WebElement> ltr = i.findElements(By.tagName("tr"));
		for(WebElement tr:ltr ){
			List<WebElement> ltd = tr.findElements(By.tagName("td"));
			if(ltd.size() < 5){
				continue; // not a process record
			}
			ProcessRecord pr = new ProcessRecord();
			String time_creator = ltd.get(1).getText();
			int sep = time_creator.indexOf(":");
			if(sep == -1){
				continue; // this is table head
			}
			String No = ltd.get(0).getText();
			pr.mRecordNo = Integer.parseInt(No.substring(No.indexOf('#')+1).trim());
			pr.mTime = time_creator.substring(0,sep+3);
			pr.mTime.trim();
			pr.mActor = time_creator.substring(sep+3);
			pr.mActor.trim();
			pr.mOperation = ltd.get(2).getText();
			pr.mOperation.trim();
			pr.mState = ltd.get(3).getText();
			pr.mState.trim();
			pr.mAssignee = ltd.get(4).getText();
			pr.mAssignee.trim();
			pr.dump();
		}
		
		return lpr;
	}
	
	/*
	 * get the issue with name
	 */
	public void navigateToIssueByName(String issueName){
		String id = "ctl00_Siteheader1_txtProblemID";
		//String input = "ctl00$Siteheader1$txtProblemID";
		WebElement i = mDriver.findElement(By.id(id));
		i.sendKeys(issueName);
		
		WebElement c = mDriver.findElement(By.name("ctl00$Siteheader1$btnGoToProblem"));
		c.click();
		System.out.println(mDriver.getCurrentUrl());
		
	}
	public void navigateToIssueById(int id){
		//String mUrl = "http://fwtrack.tpvaoc.com/"
		// something like this "http://fwtrack.tpvaoc.com/Pts/ViewProblem.aspx?problem=189834"
		String issueUrl = mUrl+"/Pts/ViewProblem.aspx?prolem="+id;
		mDriver.get(issueUrl);
		waitForPage(issueUrl,5);
	}
	private HashMap<String,String> mTranslation = new HashMap<>();
	private String getTranslation(String in){
		if(mTranslation.isEmpty()){
			mTranslation.put("Issue", "�������");
			mTranslation.put("State", "״̬");
			mTranslation.put("Project", "������Ŀ");
			mTranslation.put("Assignee","������");
			mTranslation.put("Create", "����");
		}
		return mTranslation.get(in);
	}
	public IssueRecord parseIssue(){
		
		IssueRecord record = new IssueRecord();
		System.out.println("retrieve issue code");
		String id = "ctl00_CP1_tblProblemAttributes";
		WebElement i = mDriver.findElement(By.id(id));
		List<WebElement> ltr = i.findElements(By.tagName("tr"));
		for(WebElement tr:ltr ){
			List<WebElement> ltd = tr.findElements(By.tagName("td"));
				String n = ltd.get(0).getText();
				String v = ltd.get(1).getText();
				if(n.contains(getTranslation("Issue"))){
					record.mIssueCode = v;
				}else if(n.contains(getTranslation("State"))){
					record.mState = v;
				}else if(n.contains(getTranslation("Project"))){
					record.mProject = v;
				}else if(n.contains(getTranslation("Assignee"))){
					record.mAssignee = v;
				}else if(n.contains(getTranslation("Create"))){
					// �½���&nbsp;&nbsp;2014-12-31 16:49
					record.mCreator = v.substring(0,v.indexOf("&")+1);
					record.mCreateTime = v.substring(v.lastIndexOf(";")+1);
				}

		}
		
		id = "ctl00_CP1_tblProblemInfo";
		i = mDriver.findElement(By.id(id));
		ltr = i.findElements(By.tagName("tr"));
		for(WebElement tr:ltr ){
			List<WebElement> ltd = tr.findElements(By.tagName("td"));
				String n = ltd.get(0).getText();
				String v = ltd.get(1).getText();
				if(n.contains("Subject")){
					record.mSubject = v;
				}else if(n.contains("Description")){
					record.mDescription = v;
				}
		}

		record.dump();
		record.mProcessRecords = getProcessRecord();
		return record;
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
		
		WebElement ta = mDriver.findElement(By.tagName("body"));
		ta.sendKeys(comment);
		
		
	}
	public void clickAddComment(){
		String id = "ctl00_CP1_IssuePageToolbar2_lnkAddComment";
		WebElement e = mDriver.findElement(By.id(id));
		e.click();
		waitForPage("AddComment", 5);
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
		
		List<ExcelIssueRecord> records = ExcelIssueParser.parseIssueRecord("d:/ProblemList11.xls");
		for(ExcelIssueRecord r : records){
			r.dump();
		}
		Urtracker tracker = new Urtracker("http://172.16.144.100");
		//Urtracker tracker = new Urtracker("http://fwtrack.tpvaoc.com");
		//tracker.createWebDriverHtmlUnit();
		tracker.createWebDriverChrome();
		//tracker.createWebDriverFirefox();
		tracker.login("jingxian.lin", "%t6y7u");
		tracker.navigateToIssueByName("C_Project-537");
		tracker.parseIssue();
		tracker.clickAddComment();
		tracker.inputCommentXM("comment");
		tracker.submitComment();
		
	}
}
