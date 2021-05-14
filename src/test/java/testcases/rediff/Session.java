package testcases.rediff;

import org.testng.ITestContext;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;

import keywords.ApplicationKeywords;
import testbase.BaseTest;

public class Session extends BaseTest {
	
	@Test
	public void doLogin(ITestContext context) {
		//test.log(Status.INFO,"Logging In");
		app.log("Logging In");
		app.defaultLogin();
	}
	
	public void doLogout() {
		test.log(Status.INFO,"Logging out");
		
	}

}
