package keywords;

import org.openqa.selenium.support.ui.Select;

public class ValidationKeywords extends GenericKeywords {
	
	public void validateTitle() {
		log("Validating title");
		
	}
	
	public void validateText() {
		
	}
	
	public void validateElementPresent(String locator) {
		boolean result = isElementPresent(locator);
		//reportFailure("Element not found "+locator,true);
	}
	
	public void validateLogin() {
		
	}
	
	public void validateSelectedValueInDropDown(String locatorKey, String option) {
		Select s = new Select(getElement(locatorKey));
		String text = s.getFirstSelectedOption().getText();
		if (!text.equals(option))
			reportFailure(option+" profile not present in Drop Down "+locatorKey, true);
	}
	
	public void validateSelectedValueNotInDropDown(String locatorKey, String option) {
		Select s = new Select(getElement(locatorKey));
		String text = s.getFirstSelectedOption().getText();
		if (text.equals(option))
			reportFailure(option+" profile not present in Drop Down "+locatorKey, true);
	}


}
