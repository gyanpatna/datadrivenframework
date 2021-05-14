package runner;

import java.util.ArrayList;
import java.util.List;

public class Runner {

	public static void main(String[] args) {
		TestNGRunner testNG = new TestNGRunner(1);
		testNG.createSuite("Stock Management", false);
		testNG.addListener("listener.MyTestNGListener");
		testNG.addTest("Add new Stock Test");
		testNG.addTestParameter("action", "addstock");
		List<String> includedMethodNames = new ArrayList<String>();
		includedMethodNames.add("selectPortfolio");
		testNG.addTestClass("testcases.rediff.PortfolioManagement", includedMethodNames);
		includedMethodNames.add("addNewStock");
		includedMethodNames.add("verifyStockPresent");
		includedMethodNames.add("verifyStockQuantity");
		includedMethodNames.add("verifyTransactionHistory");
		testNG.addTestClass("testcases.rediff.StockManagement", includedMethodNames);
		testNG.run();
	}

}
