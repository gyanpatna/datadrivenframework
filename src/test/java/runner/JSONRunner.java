package runner;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JSONRunner {
// string,jsonarray,jsonobject
	public static void main(String[] args) throws FileNotFoundException, IOException, ParseException {
		Map<String,String> classMethods=new DataUtil().loadClassMethods();
		String path=System.getProperty("user.dir")+"//src//test//resources//jsons//testconfig.json";
		JSONParser parser = new JSONParser();
		JSONObject json=(JSONObject)parser.parse(new FileReader(new File(path)));
		String parallelSuites = (String)json.get("parallelsuites");
		TestNGRunner testNG = new TestNGRunner(Integer.parseInt(parallelSuites));
		JSONArray testSuites = (JSONArray)json.get("testsuites");
		
		for(int sId=0;sId<testSuites.size();sId++) {
			JSONObject testSuite = (JSONObject)testSuites.get(sId);
			String runMode=(String)testSuite.get("runmode");
			if(runMode.equals("Y")) {
				String name=(String)testSuite.get("name");
				String testdatajsonfile=System.getProperty("user.dir")+"//src//test//resources//jsons//"+(String)testSuite.get("testdatajsonfile");
				String suitefilename=(String)testSuite.get("suitefilename");
				String paralleltests=(String)testSuite.get("paralleltests");
				System.out.println(runMode +" -- "+ name);
				boolean pTests=false;
				if(paralleltests.equals("Y"))
					pTests=true;
				testNG.createSuite(name, pTests);
				//testNG.addListener("listener.MyTestNGListener");

				String pathSuiteJSON=System.getProperty("user.dir")+"//src//test//resources//jsons//"+suitefilename;
				JSONParser suiteParser = new JSONParser();
				JSONObject suiteJSON=(JSONObject)suiteParser.parse(new FileReader(new File(pathSuiteJSON)));
				JSONArray suiteTestCases =(JSONArray)suiteJSON.get("testcases");
				for(int sTId=0;sTId<suiteTestCases.size();sTId++) {
					JSONObject suiteTestCase = (JSONObject)suiteTestCases.get(sTId);
					
					String tName = (String)suiteTestCase.get("name");
					JSONArray parameternames = (JSONArray)suiteTestCase.get("parameternames");
					JSONArray executions = (JSONArray)suiteTestCase.get("executions");
					for(int eId=0;eId<executions.size();eId++) {
						JSONObject testCase = (JSONObject)executions.get(eId);
						String tRunMode = (String)testCase.get("runmode");
						if(tRunMode !=null && tRunMode.equals("Y")) {
						String executionname=(String)testCase.get("executionname");
						String dataflag=(String)testCase.get("dataflag");

						int dataSets = new DataUtil().getTestDataSets(testdatajsonfile, dataflag);
						//int dataSets = new ReadingXLS().getTestDataSets(testdatajsonfile, dataflag,name);
						// how many sets of data are there
						for(int dSId=0;dSId<dataSets;dSId++) {
							JSONArray parametervalues=(JSONArray)testCase.get("parametervalues");
							JSONArray methods=(JSONArray)testCase.get("methods");
							System.out.println(tName+"-"+executionname);
							System.out.println(parameternames+"-"+parametervalues);
							System.out.println(methods);
							// add to testng
							//Add New Stock - New Stock -It.1
							//Add New Stock - New Stock -It.2
							testNG.addTest(tName+"-"+executionname+"-It."+(dSId+1));
							for(int pId=0;pId<parameternames.size();pId++) {
								testNG.addTestParameter((String)parameternames.get(pId), (String)parametervalues.get(pId));
							}
							
							testNG.addTestParameter("datafilepath", testdatajsonfile);
							testNG.addTestParameter("dataflag", dataflag);
							testNG.addTestParameter("iteration", String.valueOf(dSId));
							//testNG.addTestParameter("suitename", name);// xls
	
							List<String> includedMethods = new ArrayList<String>();;
							 
							 for(int mId=0;mId<methods.size();mId++) {
								 String method = (String)methods.get(mId);
								 String methodClass=classMethods.get(method);
								 //System.out.println(method +" -- "+ methodClass);
									 if(mId==methods.size()-1 || !((String)classMethods.get((String)methods.get(mId+1))).equals(methodClass)) {
										 // next method is from different class
										 includedMethods.add(method);
										 testNG.addTestClass(methodClass, includedMethods);
										 includedMethods = new ArrayList<String>();;
									 }else {
										 // same class
										 includedMethods.add(method);
									 }	 
							 }
	
							System.out.println("-----------------------------");
						}
					}
					}
				
					
				}
				

			}
		}
		testNG.run();
	}

}
