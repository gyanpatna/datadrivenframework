package testcases;

import org.testng.annotations.Test;

import keywords.ApplicationKeywords;

public class CreatePortFolioTest {
	
	@Test
	public void createPortFolioTest() {
		
		
		ApplicationKeywords app = new ApplicationKeywords(); //init prop
		app.openBrowser("Chrome");
		app.navigate("url");
		app.type("username_css","gyanphoton@rediffmail.com");
		app.type("password_xpath", "Password123!");
		app.validateElementPresent("login_submit_id");
		app.click("login_submit_id");
		app.validateLogin();
	}

}
