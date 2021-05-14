package testbase;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.testng.ITestContext;
import org.testng.SkipException;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

import keywords.ApplicationKeywords;
import reports.ExtentManager;
import runner.DataUtil;

//acceptable failure,critical failure, unexpected failure

public class BaseTest {
	
	public ApplicationKeywords app;
	public ExtentReports rep;
	public ExtentTest test;
	
	@BeforeTest(alwaysRun = true)
	public void beforeTest(ITestContext context) throws NumberFormatException, FileNotFoundException, IOException, ParseException {
		System.out.println("---------------Before Test------------------");
		String datafilepath = context.getCurrentXmlTest().getParameter("datafilepath");
		String dataFlag = context.getCurrentXmlTest().getParameter("dataflag");
		String iteration = context.getCurrentXmlTest().getParameter("iteration");
		//System.out.println(datafilepath);
		//System.out.println(dataFlag);
		//System.out.println(iteration);
		JSONObject data = new DataUtil().getTestData(datafilepath,dataFlag,Integer.parseInt(iteration));
		context.setAttribute("data", data);
		String runmode = (String)data.get("runmode");
		
		
		//init the reporting for the test
		rep = ExtentManager.getReports();
		test = rep.createTest(context.getCurrentXmlTest().getName());
		test.log(Status.INFO, "Starting Test "+context.getCurrentXmlTest().getName());
		test.log(Status.INFO, "Data "+data.toString());
		context.setAttribute("report", rep);
		context.setAttribute("test", test);
		if(!runmode.equals("Y")) {
			test.log(Status.SKIP, "Skipping as data runmode is N"); //skip in extent report
			throw new SkipException("Skipping as data runmode is N");//skip in testng
		}
		

		//init and share it with all tests
		app = new ApplicationKeywords();
		context.setAttribute("app", app);
		app.setReport(test);
		app.defaultLogin();
	
	}
	
	@BeforeMethod(alwaysRun = true)
	public void beforeMethod(ITestContext context) {
		System.out.println("***************Before Method***************");
		test = (ExtentTest) context.getAttribute("test");
		
		String criticalFailure = (String)context.getAttribute("criticalFailure");
		if (criticalFailure!=null && criticalFailure.equals("Y")) {
			test.log(Status.SKIP, "Critical Failure in Previous Tests"); //skip in reports
			throw new SkipException("Critical Failure in Previous Tests"); //skip in testNG
		}
		app = (ApplicationKeywords)context.getAttribute("app");
		rep = (ExtentReports) context.getAttribute("report");
		
	}
	
	@AfterTest(alwaysRun = true)
	public void quit(ITestContext context) {
		app = (ApplicationKeywords)context.getAttribute("app");
		if (app!=null)
			app.quit();
		rep = (ExtentReports)context.getAttribute("report");
		if (rep!=null)
			rep.flush();
	}

}
