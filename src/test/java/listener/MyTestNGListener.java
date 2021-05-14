package listener;

import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.Reporter;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

public class MyTestNGListener implements ITestListener {
	
	public void onTestFailure(ITestResult result) {
		System.out.println("------Test Failed------ "+result.getName());
		//System.out.println(result.getName());
		//System.out.println(result.getStatus());
		System.out.println(result.getThrowable().getMessage());
		ExtentTest test = (ExtentTest) result.getTestContext().getAttribute("test");
		//Reporter.getCurrentTestResult().getTestContext().setAttribute("criticalFailure", "Y");
		test.log(Status.FAIL, result.getThrowable().getMessage());
	}
	
	public void onTestSuccess(ITestResult result) {
		System.out.println("-------Test Passed----- "+result.getName());
		//System.out.println(result.getThrowable().getMessage());
		ExtentTest test = (ExtentTest) result.getTestContext().getAttribute("test");
		test.log(Status.PASS, result.getName()+" - Test Passed");
	}
	
	public void onTestSkipped(ITestResult result) {
		System.out.println("-------Test Skipped----- "+result.getName());
		//System.out.println(result.getThrowable().getMessage());
		ExtentTest test = (ExtentTest) result.getTestContext().getAttribute("test");
		test.log(Status.SKIP, result.getName()+" - Test Skipped");
	}

}
