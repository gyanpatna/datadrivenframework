package keywords;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Reporter;
import org.testng.asserts.SoftAssert;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

import reports.ExtentManager;

public class GenericKeywords {
	
	public WebDriver driver;
	public Properties prop;
	public Properties envProp;
	public ExtentTest test;
	public SoftAssert softAssert;
	
	public void openBrowser(String browser) {
		System.out.println("Opening the browser "+browser);
		System.setProperty("webdriver.ie.driver", "D:\\Learning Java\\New\\Driver\\IEDriverServer_Win32_3.150.1\\IEDriverServer.exe");
		System.setProperty("webdriver.chrome.driver", "D:\\Learning Java\\New\\Driver\\chromedriver_win32\\chromedriver.exe");
		System.setProperty("webdriver.gecko.driver", "D:\\Learning Java\\New\\Driver\\geckodriver-v0.29.1-win64\\geckodriver.exe");
		System.setProperty("webdriver.edge.driver", "D:\\Learning Java\\New\\Driver\\edgedriver_win64\\msedgedriver.exe");
		
		if (browser.equals("Edge")) {
			driver = new EdgeDriver();
		}else if (browser.equals("Mozilla")) {
			System.setProperty(FirefoxDriver.SystemProperty.BROWSER_LOGFILE, "logs\\firefox.log");
			FirefoxOptions options = new FirefoxOptions();
			options.setPageLoadStrategy(PageLoadStrategy.EAGER);
			//options.setBinary("C:\\Program Files\\Mozilla Firefox\\firefox.exe");
			//ProfilesIni allprof = new ProfilesIni();
			FirefoxProfile prof = new FirefoxProfile();
			//notification
			prof.setPreference("dom.webnotifications.enabled", false);
			options.setProfile(prof);
			driver = new FirefoxDriver(options);
		}else if (browser.equals("Chrome")) {
			//System.setProperty(ChromeDriverService.CHROME_DRIVER_LOG_PROPERTY, "logs\\chrome.log");
			System.setProperty(ChromeDriverService.CHROME_DRIVER_SILENT_OUTPUT_PROPERTY, "true");
			ChromeOptions ops = new ChromeOptions();
			//ops.setPageLoadStrategy(PageLoadStrategy.EAGER);
			ops.addArguments("--disable-notifications");
			ops.addArguments("--start-maximized");
			ops.addArguments("ignore-certificate-errors");
			driver = new ChromeDriver(ops);
		}
		//implicit wait
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
	}
	
	public void navigate(String urlKey) {
		log("Navigating to "+urlKey);
		driver.get(envProp.getProperty(urlKey));
		
	}
	
	public void click(String locatorKey) {
		log("Clicking on"+locatorKey);
		getElement(locatorKey).click();
		
	}
	
	public void type(String locatorKey, String data) {
		log("Typing in "+locatorKey+" . Data "+data);
		getElement(locatorKey).sendKeys(data);
		
	}
	
	public void clear(String locatorKey) {
		log("Clearning the text in "+locatorKey);
		getElement(locatorKey).clear();
		
	}
	
	public void clickEnterButton(String locatorKey) {
		log("Clicking enter button");
		getElement(locatorKey).sendKeys(Keys.ENTER);
		
	}
	
	public void selectByVisibleText(String locatorKey, String data) {
		Select s = new Select(getElement(locatorKey));
		s.selectByVisibleText(data);
		
	}
	
	public String getText(String locatorKey) {
		log("Getting the text");
		String s = getElement(locatorKey).getText();
		return s;
	}
	
	//central functions to extract elements
	public WebElement getElement (String locatorKey) {
		//check the presence
		if (!isElementPresent(locatorKey)) {
			//report failure
			log("Element not present "+locatorKey);
		}
		//check the visibility
		if (!isElementVisible(locatorKey)) {
			//report failure
			log("Element not visible "+locatorKey);
		}
		WebElement e = driver.findElement(getLocator(locatorKey));
		return e;
	}
	
	//true - present
	//false - not present
	public boolean isElementPresent(String locatorKey) {
		log("Checking presence of "+locatorKey);
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		try {
			wait.until(ExpectedConditions.presenceOfElementLocated(getLocator(locatorKey)));
		}catch (Exception e) {
			return false;
		}
		return true;
	}
	
	//true - visible
	//false - not visible
	public boolean isElementVisible(String locatorKey) {
		log("Checking visibility of "+locatorKey);
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		try {
			wait.until(ExpectedConditions.visibilityOfElementLocated(getLocator(locatorKey)));
		}catch (Exception e) {
			return false;
		}
		return true;
			
	}
	
	public By getLocator(String locatorKey) {
		By by = null;
		if (locatorKey.endsWith("_css"))
			by = by.cssSelector(prop.getProperty(locatorKey));
		else if (locatorKey.endsWith("_xpath"))
			by = by.xpath(prop.getProperty(locatorKey));
		else if (locatorKey.endsWith("_id"))
			by = by.id(prop.getProperty(locatorKey));
		else if (locatorKey.endsWith("_name"))
			by = by.name(prop.getProperty(locatorKey));
		
		return by;
	}
	
	//reporting functions
	public void log(String msg) {
		System.out.println(msg);
		test.log(Status.INFO, msg);
	}
	
	public void reportFailure (String failureMsg, boolean stopOnFailure) {
		System.out.println(failureMsg);
		test.log(Status.FAIL, failureMsg); //failure in extent report
		takeScreenShot();//put the screenshot in reports
		softAssert.fail(failureMsg); //failure in TestNG report
		
		if(stopOnFailure) {
			Reporter.getCurrentTestResult().getTestContext().setAttribute("criticalFailure", "Y");
			assertAll(); //report all the failures
		}
			
	}
	
	public void assertAll() {
		
		softAssert.assertAll();
	}
	
	public void takeScreenShot() {
		//filename of the screenshot
		Date d = new Date();
		String screenshotFile = d.toString().replace(":","_").replace(" ", "_")+".png";
		//take screenshot
		File srcFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		try {
			//get the dynamic folder name
			FileUtils.copyFile(srcFile, new File(ExtentManager.screenshotFolderPath+"//"+screenshotFile));
			//put screenshot file in reports
			test.log(Status.INFO, "Screenshot-> "+ test.addScreenCaptureFromPath(ExtentManager.screenshotFolderPath+"//"+screenshotFile));
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void waitForPageToLoad() {
		JavascriptExecutor js = (JavascriptExecutor)driver;
		//check for document ready or page load state
		int i = 0;
		while (i!=10) {
		String state = (String) js.executeScript("return document.readyState");
		System.out.println(state);
		if (state.equals("complete"))
			break;
		else
			wait(1);
		i++;
		}
		
		//check for jQuery state
		i=0;
		while(i!=10) {
		Long d = (Long) js.executeScript("return jQuery.active");
		System.out.println(d);
		if(d.longValue()==0)
			break;
		else
			wait(1);
		i++;
		}
		
	}
	
	public void wait(int time) {
		try {
			Thread.sleep(time*1000);
		}catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void acceptAlert(){
		test.log(Status.INFO, "Switching to alert");
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.alertIsPresent());
		try{
			driver.switchTo().alert().accept();
			driver.switchTo().defaultContent();
			test.log(Status.INFO, "Alert accepted successfully");
		}catch(Exception e){
				reportFailure("Alert not found when mandatory",true);
		}
		
	}
	
	public int getRowNumWithCellData(String tableLocator, String data) {
		WebElement table = getElement(tableLocator);
		List<WebElement> rows = table.findElements(By.tagName("tr"));
		for (int rNum=0;rNum<rows.size();rNum++) {
			WebElement row = rows.get(rNum);
			List<WebElement> cells = row.findElements(By.tagName("td"));
			for (int cNum=0;cNum<cells.size();cNum++) {
				WebElement cell = cells.get(cNum);
				if(!cell.getText().trim().equals(""))
					if (data.startsWith(cell.getText()))
						return rNum+1;
			}
		}
		//no data found
		return -1;
	}
	
	public void quit() {
		driver.quit();
	}

}
