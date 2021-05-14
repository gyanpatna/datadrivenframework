package testcases.rediff;

import org.json.simple.JSONObject;
import org.testng.ITestContext;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import testbase.BaseTest;

public class StockManagement extends BaseTest {
	
	@Test
	public void addNewStock(ITestContext context) {
		JSONObject data = (JSONObject) context.getAttribute("data");
		String companyName = (String) data.get("stockname");
		String selectionDate = (String) data.get("date");
		String stockQuantity = (String) data.get("quantity");
		String stockPrice = (String) data.get("price");
		
		app.log("Adding "+stockQuantity+" stocks of "+companyName);
		//find quantity
		int quantityBeforeModification = app.findCurrentStockQuantity(companyName);
		context.setAttribute("quantityBeforeModification", quantityBeforeModification);
		
		app.click("addStock_id");
		app.type("addstockname_id", companyName);
		app.wait(1);
		app.clickEnterButton("addstockname_id");
		app.click("stockPurchaseDate_id");
		app.selectDateFromCalendar(selectionDate);
		app.type("addstockqty_id", stockQuantity);
		app.type("addstockprice_id", stockPrice);
		app.click("addStockButton_id");
		app.waitForPageToLoad();
		app.log("Stocks added successfully");
	}
	
	@Parameters({"action"})
	@Test
	public void modifyStock(String action, ITestContext context) {
		JSONObject data = (JSONObject) context.getAttribute("data");
		String companyName = (String) data.get("stockname");
		String selectionDate = (String) data.get("date");
		String stockQuantity = (String) data.get("quantity");
		String stockPrice = (String) data.get("price");
		if(action.equals("sellstock"))
			app.log("Selling "+stockQuantity +" of company "+ companyName);
		else
			app.log("Buying "+stockQuantity +" of company "+ companyName);
		int quantityBeforeModification = app.findCurrentStockQuantity(companyName);
		context.setAttribute("quantityBeforeModification", quantityBeforeModification);
		
		app.goToBuySell(companyName);
		if(action.equals("sellstock"))
		   app.selectByVisibleText("equityaction_id", "Sell");
		else
			app.selectByVisibleText("equityaction_id", "Buy");
		
		app.click("buySellCalendar_id");
		app.log("Selecting Date "+ selectionDate);
		app.selectDateFromCalendar(selectionDate);
		app.type("buysellqty_id", stockQuantity);
		app.type("buysellprice_id", stockPrice);
		app.click("buySellStockButton_id");
		app.waitForPageToLoad();
		app.wait(5);
		if(action.equals("sellstock"))
			app.log("Stock Sold ");
		else
			app.log("Stock bought");
		
	}
	
	@Test
	public void verifyStockPresent(ITestContext context) {
		JSONObject data = (JSONObject) context.getAttribute("data");
		String companyName = (String) data.get("stockname");
		int row = app.getRowNumWithCellData("stocktable_css", companyName);
		if(row==-1) {
			app.reportFailure("Stock Not Present "+companyName, true);
			
		app.log(companyName +" found in the list");
		}
	}
	
	@Parameters({"action"})
	@Test
	public void verifyStockQuantity(String action, ITestContext context) {
		JSONObject data = (JSONObject) context.getAttribute("data");
		String companyName = (String) data.get("stockname");
		String selectionDate = (String) data.get("date");
		String stockQuantity = (String) data.get("quantity");
		String stockPrice = (String) data.get("price");
		
		app.log("Verifying stock quantity after action - "+action);
		int quantityAfterModification = app.findCurrentStockQuantity(companyName);
		
		int modifiedQuantity = Integer.parseInt(stockQuantity);
		int expectedModifiedQuantity = 0;
		int quantityBeforeModification =  (Integer) context.getAttribute("quantityBeforeModification");
	
		if (action.equals("addstock"))
			expectedModifiedQuantity = quantityAfterModification-quantityBeforeModification;
		else if(action.equals("sellstock"))
			expectedModifiedQuantity = quantityBeforeModification-quantityAfterModification;
		
		app.log("Old stock quantity "+quantityAfterModification);
		app.log("New stock quantity "+quantityAfterModification);
		if (modifiedQuantity !=expectedModifiedQuantity)
			app.reportFailure("Quantity did not match", true);
		app.log("Stock quantity changed as per expected "+modifiedQuantity);
	}
	
	@Parameters({"action"})
	@Test
	public void verifyTransactionHistory(String action, ITestContext context) {
		JSONObject data = (JSONObject) context.getAttribute("data");
		String companyName = (String) data.get("stockname");
		String selectionDate = (String) data.get("date");
		String stockQuantity = (String) data.get("quantity");
		String stockPrice = (String) data.get("price");
		
		app.log("Verifying transaction history for "+action+"for quantity "+stockQuantity);
		app.goToTransactionHistory(companyName);
		String changeQuantityDisplayed = app.getText("latestShareChangeQuantity_xpath");
		app.log("Got changed quantity "+changeQuantityDisplayed);
		
		if(action.equals("sellstock"))
			stockQuantity = "-"+stockQuantity;
		
		if(!changeQuantityDisplayed.equals(stockQuantity))
			app.reportFailure("Got changed quantity in transaction history as "+changeQuantityDisplayed, true);
		
		app.log("Transaction History OK");
	}
}
