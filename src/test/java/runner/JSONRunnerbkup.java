package runner;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

public class JSONRunnerbkup {

	public static void main(String[] args) throws JsonIOException, JsonSyntaxException, IOException, ParseException {
				Map<String,String> classMethods = new DataUtil().loadClassMethods();
				String path = System.getProperty("user.dir")+"//src//test//resources//jsons//testconfig.json";
				JSONParser parser = new JSONParser();
				JSONObject json = (JSONObject)parser.parse(new FileReader(new File(path)));
				String parallelSuites = (String)json.get("parallelsuites");
				TestNGRunner testNG = new TestNGRunner(Integer.parseInt(parallelSuites));
				
				JSONArray testSuites= (JSONArray)json.get("testsuites");
				for(int sId=0;sId<testSuites.size();sId++) {
					JSONObject testSuite = (JSONObject)testSuites.get(sId);
					String runMode = (String)testSuite.get("runmode");
					if(runMode.equals("Y")) {
						String name = (String)testSuite.get("name");
						String testdatajsonfile = (String)testSuite.get("testdatajsonfile");
						String suitefilename = (String)testSuite.get("suitefilename");
						String paralleltests = (String)testSuite.get("paralleltests");
						System.out.println(runMode+" ------- "+name);
						boolean pTests=false;
						if(paralleltests.equals("Y"))
							pTests=true;
						testNG.createSuite(name, pTests);
						testNG.addListener("listener.MyTestNGListener");
						String pathSuiteJSON = System.getProperty("user.dir")+"//src//test//resources//jsons//"+suitefilename;
						JSONParser suiteParser = new JSONParser();
						JSONObject suiteJSON = (JSONObject)suiteParser.parse(new FileReader(new File(pathSuiteJSON)));
						JSONArray suiteTestCases= (JSONArray)suiteJSON.get("testcases");
						for(int sTId=0;sTId<suiteTestCases.size();sTId++) {
							JSONObject suiteTestCase = (JSONObject)suiteTestCases.get(sTId);
							
							
							String tName = (String)suiteTestCase.get("name");
							JSONArray parameterNames = (JSONArray)suiteTestCase.get("parameternames");
							JSONArray executions = (JSONArray)suiteTestCase.get("executions");
							for(int eId=0;eId<executions.size();eId++) {
								JSONObject testCase = (JSONObject)executions.get(eId);
								String tRunMode = (String)testCase.get("runmode");
								if(tRunMode!=null && tRunMode.equals("Y")) {
								String executionName = (String)testCase.get("executionname");
								String dataflag = (String)testCase.get("dataflag");
								JSONArray parametervalues = (JSONArray)testCase.get("parametervalues");
								JSONArray methods = (JSONArray)testCase.get("methods");
								System.out.println(tName+"-"+executionName);
								System.out.println(parameterNames+"-"+parametervalues);
								System.out.println(methods);
								//add to testng
								testNG.addTest(tName+"-"+executionName);
								for(int pId=0;pId<parameterNames.size();pId++) {
									testNG.addTestParameter((String)parameterNames.get(pId),(String)parametervalues.get(pId));
								}
								List<String> includedMethods = new ArrayList<String>();
								for(int mId=0;mId<methods.size();mId++) {
									String method = (String)methods.get(mId);
									String methodClass= classMethods.get(method);
									System.out.println(method+"----"+methodClass);
									if(mId==methods.size()-1 || !((String)classMethods.get((String)methods.get(mId+1))).equals(methodClass)) {
										//next method is from different class
										includedMethods.add(method);
										testNG.addTestClass(methodClass, includedMethods);
										includedMethods = new ArrayList<String>();
								}else {
									//same class
									includedMethods.add(method);
								}
								}
								
								System.out.println("------------------");
							}
						}
						}
						testNG.run();
					}
				}
	}

}
