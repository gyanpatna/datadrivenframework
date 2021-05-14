package keywords;

import java.io.FileInputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import org.openqa.selenium.By;
import org.testng.asserts.SoftAssert;

import com.aventstack.extentreports.ExtentTest;


public class ApplicationKeywords extends ValidationKeywords {
	
	
	
	public ApplicationKeywords() {
		String path = System.getProperty("user.dir")+"//src//test//resources//env.properties";
		prop = new Properties();
		envProp = new Properties();
		try {
			FileInputStream fs = new FileInputStream(path);
			prop.load(fs);
			String env = prop.getProperty("env")+".properties";
			path = System.getProperty("user.dir")+"//src//test//resources//"+env;
			fs = new FileInputStream(path);
			envProp.load(fs);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		softAssert = new SoftAssert();
		
	}
	
	public void defaultLogin() {
		openBrowser("Chrome");
		navigate("url");
		type("username_css",envProp.getProperty("admin_user_name"));
		//failure
		//app.reportFailure("First Failure - Non Critical",false);
		type("password_xpath", envProp.getProperty("admin_password"));
		validateElementPresent("login_submit_id");
		click("login_submit_id");
		waitForPageToLoad();
		wait(2);
	}
	
	public void selectDateFromCalendar(String d) {
		log("Selecting date "+d);
		try {
			Date current = new Date();
			System.out.println(current);
			SimpleDateFormat sd = new SimpleDateFormat("d-MM-yyyy");
			Date selected = sd.parse(d);
			System.out.println(selected);
			String day = new SimpleDateFormat("d").format(selected);
			String month = new SimpleDateFormat("MMMM").format(selected);
			String year = new SimpleDateFormat("yyyy").format(selected);
			System.out.println(day+" ---- "+month+" ---- "+year);
			String desiredMonthYear = month+" "+year;
			
			while(true) {
				String displayedMonthYear = getElement("monthyear_css").getText();
				if (desiredMonthYear.equals(displayedMonthYear)) {
					//select the day
					driver.findElement(By.xpath("//td[text()='"+day+"']")).click();
					break;
				}else {
					if (selected.compareTo(current) > 0)
						driver.findElement(By.xpath("//*[@id='datepicker']/table/tbody/tr[1]/td[4]/button")).click();
					else if (selected.compareTo(current) < 0)
						driver.findElement(By.xpath("//*[@id='datepicker']/table/tbody/tr[1]/td[2]/button")).click(); 
					
				}
			}
			
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public int findCurrentStockQuantity(String companyName) {
		log("Finding current stock quantity for "+companyName);
		int row = getRowNumWithCellData("stocktable_css",companyName);
		if(row==-1) {
			log("Current Stock Quantity is 0 as Stock not present in list");
			return 0;
		}
		String quantity = driver.findElement(By.cssSelector(prop.getProperty("stocktable_css")+" >tr:nth-child("+row+") >td:nth-child(4)")).getText();
		log("Current stock Quantity "+quantity);
		return Integer.parseInt(quantity);
	}
	
	public void goToBuySell(String companyName) {
		log("Selecting the company row "+companyName );
		int row = getRowNumWithCellData("stocktable_css", companyName);
		if(row==-1) {
			log("Stock not present in list");
		}
		driver.findElement(By.cssSelector(prop.getProperty("stocktable_css")+" > tr:nth-child("+row+") >td:nth-child(1)")).click();
		driver.findElement(By.cssSelector(prop.getProperty("stocktable_css")+"  tr:nth-child("+row+") input.buySell" )).click();
		
	}
	
	public void goToTransactionHistory(String companyName) {
		log("Selecting the company row "+companyName);
		int row = getRowNumWithCellData("stocktable_css", companyName);
		if (row == -1) {
			reportFailure("Stock not present in list", true);
		}
		driver.findElement(By.cssSelector(prop.getProperty("stocktable_css")+" >tr:nth-child("+row+") >td:nth-child(1)")).click();
		driver.findElement(By.cssSelector(prop.getProperty("stocktable_css")+" tr:nth-child("+row+") input.equityTransaction")).click();
	}
	
	public void setReport(ExtentTest test) {
		this.test = test;
	}

}
