package testcases.rediff;

import org.json.simple.JSONObject;
import org.testng.ITestContext;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;

import testbase.BaseTest;

public class PortfolioManagement extends BaseTest {
	
	@Test
	public void createPortfolio(ITestContext context) {
		JSONObject data = (JSONObject) context.getAttribute("data");
		String portfolioName = (String) data.get("portfolioname");
		app.log("Creating Portfolio");
		app.click("createPortfolio_id");
		app.clear("portfolioname_id");
		app.type("portfolioname_id",portfolioName);
		app.click("createPortfolioButton_css");
		app.waitForPageToLoad();
		app.validateSelectedValueInDropDown("portfolioid_dropdown_id",portfolioName);
	}
	
	@Test
	public void deletePortfolio(ITestContext context) {
		JSONObject data = (JSONObject) context.getAttribute("data");
		String portfolioName = (String) data.get("portfolioname");
		app.log("Deleting Portfolio");
		app.selectByVisibleText("portfolioid_dropdown_id",portfolioName);
		app.waitForPageToLoad();
		app.click("deletePortfolio_id");
		app.acceptAlert();
		app.waitForPageToLoad();
		app.validateSelectedValueNotInDropDown("portfolioid_dropdown_id", portfolioName);
	}
	
	@Test
	public void selectPortfolio(ITestContext context) {
		JSONObject data = (JSONObject) context.getAttribute("data");
		String portfolioName = (String) data.get("portfolioname");
		app.log("Selecting Portfolio");
		app.selectByVisibleText("portfolioid_dropdown_id", portfolioName);
		app.waitForPageToLoad();
	}

}
